---
type: API Reference
title: TextViewer
description: Vaadin-Flow-Komponente für read-only mehrzeiligen Klartext mit Zeilennummern, Zeilen-Highlight, umschaltbarem Umbruch und geteilter Textsuche.
resource: web-common/src/main/java/de/makno/web/common/component/text/TextViewer.java
tags: [api-reference, text, vaadin, search, component]
timestamp: '2026-07-07T10:00:00+02:00'
---

# Überblick


`TextViewer` (`public class TextViewer extends Composite<Div> implements HasSize, HasStyle,
MatchNavigable`) ist eine Vaadin-Flow-Komponente, die mehrzeiligen Klartext read-only anzeigt. Sie
delegiert die Suchlogik vollständig an einen intern gehaltenen `SearchController`
(siehe [Such-Engine](/architecture/search-engine.md)) und implementiert `MatchNavigable`, wodurch sie
direkt vom [SearchNavigator](/components/search-navigator.md) gesteuert werden kann. Details zur
Narrative stehen in [/components/textviewer.md](/components/textviewer.md).

**Zustand/Thread-Safety:** Die Klasse ist **mutable** (Instanzfelder für Text, Zeilenumbruch,
Zeilennummern-Anzeige, Groß-/Kleinschreibungs-Flag, Splitter, gerenderte Zeilen-Elemente, Highlight-
Indizes und der `SearchController`). Laut Javadoc der Klasse **nicht thread-safe** — wie jede
Vaadin-Komponente an genau eine `UI`/Session gebunden; Methoden dürfen nur aus dem an die Session
gebundenen Thread (mit Session-Lock) aufgerufen werden, eine Instanz pro UI. Es gibt keine
`synchronized`-Blöcke oder sonstige Synchronisation im Code — das ist konsistent mit dieser
Dokumentation, da Nebenläufigkeitsschutz hier bewusst dem Vaadin-Session-Lock überlassen wird statt
selbst implementiert zu werden.

`serialVersionUID = 1L` ist explizit gesetzt (Komponente ist über `Composite`/`Div` serialisierbar,
relevant für Session-Clustering).

# Felder

| Feld | Typ | Bedeutung | null-erlaubt |
|---|---|---|---|
| `serialVersionUID` | `private static final long` | `1L` — explizit gesetzte Serialisierungs-Version. | entfällt (primitiv) |
| `EMPTY_TEXT` | `private static final String` | `"Kein Text gesetzt."` — Platzhaltertext, der gerendert wird, wenn kein Text gesetzt ist. | entfällt (primitiv, nie `null`) |
| `text` | `private String` | Angezeigter Klartext, Default `""`. | nein — Konstruktor/`setText(String)` normalisieren `null` auf `""` |
| `wrap` | `private boolean` | Zeilenumbruch ein/aus, Default `false` (kein Umbruch, lange Zeilen scrollen horizontal). | entfällt (primitiv) |
| `showLineNumbers` | `private boolean` | Zeilennummern-Spalte ein/aus, Default `false`. | entfällt (primitiv) |
| `searchCaseSensitive` | `private boolean` | Groß-/Kleinschreibungsabgleich der Suche, Default `false`. | entfällt (primitiv) |
| `searchTermSplitter` | `private SearchTermSplitter` | Zerlegt die Suchanfrage in einzeln hervorzuhebende Begriffe, Default `SearchController.DEFAULT_TERM_SPLITTER` (Trennung an Whitespace, leere Begriffe verworfen — verifiziert in `SearchController`). | nein — Setter `setSearchTermSplitter` erzwingt `Objects.requireNonNull` |
| `lineElements` | `private List<Div>` | Gerenderte Zeilen-Elemente (Index == Zeilenindex), für `highlight(int)`. Default `List.of()` (leer). | nein — stets mit `List.of()` oder befüllter `ArrayList` belegt, nie `null` |
| `lineTexts` | `private List<String>` | Klartext je gerenderter Zeile (Index == Zeilenindex). Default `List.of()` (leer). | nein — analog zu `lineElements` |
| `highlightedLines` | `private final Set<Integer>` | Indizes der aktuell per `highlight(int)` hervorgehobenen Zeilen; mit `new HashSet<>()` initialisiert. | nein — Feld selbst nie `null` (final, immer initialisiert); Elemente sind primitive Zeilenindizes |
| `searchController` | `private SearchController` | Kapselt die Suchlogik über die aktuellen Zeilen-Tokens; bei jedem `render()`-Aufruf neu erzeugt. | ja vor dem ersten `render()`-Aufruf im Konstruktor; danach nie mehr `null`, da `render()` immer eine neue Instanz zuweist |
| `highlightRenderer` | `private final SearchHighlightRenderer` | Zeichnet Suchtreffer im Frontend (CSS Custom Highlight API); konkret eine `FrontendSearchHighlighter`-Instanz mit stabiler Element-Referenz auf `this`. | nein — final, im Feld-Initialisierer gesetzt, nie `null` |

# Thread-Safety

**Nicht thread-sicher** (laut Klassen-Javadoc verifiziert): wie jede Vaadin-Komponente an genau
eine `UI`/Session gebunden — Methoden dürfen nur aus dem an die Session gebundenen Thread (mit
Session-Lock) aufgerufen werden, eine Instanz pro UI. Die Klasse ist mutable (Text,
Zeilenumbruch, Zeilennummern-Anzeige, Case-Sensitivity, Splitter, gerenderte Zeilen-Elemente,
Highlight-Indizes, `SearchController`) und synchronisiert nichts selbst (keine `synchronized`-
Blöcke oder sonstige Synchronisationsprimitive im Quellcode) — konsistent mit dem dokumentierten
Vertrag, da der Nebenläufigkeitsschutz bewusst dem Vaadin-Session-Lock überlassen wird statt
selbst implementiert zu werden.

# Serialisierung

**`Serializable`** (transitiv über `Composite<Div>`/`Component`) mit explizit gesetzter
`private static final long serialVersionUID = 1L`. Die Instanzfelder sind überwiegend einfache,
serialisierbare Typen (`String`, `boolean`, `List<Div>`, `Set<Integer>`); `Div`-Elemente in
`lineElements` sind über Vaadins `Component`-Hierarchie selbst serialisierbar. Der
`highlightRenderer` (`FrontendSearchHighlighter`) und der `searchController` referenzieren `this`
bzw. den aktuellen Zustand und werden bei jedem Rendering neu aufgebaut bzw. zugewiesen — im
Quellcode findet sich kein Hinweis auf eine bewusste `transient`-Markierung oder einen
`readObject`/`writeObject`-Sonderweg; die Serialisierbarkeit stützt sich auf die geerbte
Vaadin-Komponenten-Infrastruktur (Session-Clustering), wie es für Vaadin-Komponenten dieses
Projekts (siehe `CodeViewer`) üblich ist.

# equals/hashCode/toString

Keine dieser Methoden ist überschrieben (verifiziert: keine `equals`/`hashCode`/`toString`-
Deklaration im Quellcode) — es gilt die **Identitätssemantik der geerbten `Component`/`Object`-
Implementierung** (`==`-Vergleich, identitätsbasierter Hashcode). Konsistent mit dem
Single-Instance-pro-UI-Vertrag dieser Komponente.

# Vererbungshierarchie


**Vorwärts (eigene Deklaration):** `public class TextViewer extends Composite<Div> implements
HasSize, HasStyle, MatchNavigable`.

- **Superklasse:** `Composite<Div>` — Vaadin-Flow-Framework-Klasse
  (`com.vaadin.flow.component.Composite`), kein Projekt-Typ, daher kein Cross-Link.
- **Interfaces:**
  - `HasSize` — Vaadin-Flow-Framework-Interface, extern.
  - `HasStyle` — Vaadin-Flow-Framework-Interface, extern.
  - [MatchNavigable](/api-reference/navigation/match-navigable/match-navigable.md) — projektinternes
    Entkopplungs-Interface aus `navigation`; `TextViewer` implementiert dessen Methoden, um
    vom [SearchNavigator](/api-reference/navigation/search-navigator/search-navigator.md) gesteuert werden zu können.

**Rückwärts (Abhängige):** Verifiziert per Grep auf `extends TextViewer` /
`implements ... TextViewer` über den gesamten
`web-common/src/main/java/de/makno/web/common/component/`-Baum — **kein Treffer**. Keine
andere Klasse im Projekt erweitert `TextViewer`; keine projektinternen Subklassen.

# Konstruktoren

- [siehe constructor.md](./constructor.md)

# Methoden

- [``public void setText(String text)``](./set-text.md)
- [``public String getText()``](./get-text.md)
- [``public void setShowLineNumbers(boolean showLineNumbers)``](./set-show-line-numbers.md)
- [``public boolean isShowLineNumbers()``](./is-show-line-numbers.md)
- [``public void setWrap(boolean wrap)``](./set-wrap.md)
- [``public boolean isWrap()``](./is-wrap.md)
- [``public void highlight(int line)``](./highlight.md)
- [``public void clearHighlight()``](./clear-highlight.md)
- [`public void search(String query)`](./search.md) *(implements `MatchNavigable`)*
- [`public void nextMatch()`](./next-match.md) *(implements `MatchNavigable`)*
- [`public void previousMatch()`](./previous-match.md) *(implements `MatchNavigable`)*
- [``public void clearSearch()``](./clear-search.md)
- [`public int getMatchCount()`](./get-match-count.md) *(implements `MatchNavigable`)*
- [`public int getCurrentMatchIndex()`](./get-current-match-index.md) *(implements `MatchNavigable`)*
- [``public void setSearchCaseSensitive(boolean caseSensitive)``](./set-search-case-sensitive.md)
- [``public boolean isSearchCaseSensitive()``](./is-search-case-sensitive.md)
- [``public void setSearchTermSplitter(SearchTermSplitter splitter)``](./set-search-term-splitter.md)
- [`public Registration addMatchChangeListener(ComponentEventListener<MatchChangeEvent> listener)`](./add-match-change-listener.md) *(implements `MatchNavigable`)*
- [``private void render()``](./render.md)
- [``private Div newLine(int index, String lineText)``](./new-line.md)
- [``private Div newEmptyPlaceholder()``](./new-empty-placeholder.md)
- [``private static List<String> splitLines(String text)``](./split-lines.md)
- [``private void fireMatchChange()``](./fire-match-change.md)
- [``private void fireSearchReset()``](./fire-search-reset.md)
- [``private void scrollTo(Component target)``](./scroll-to.md)
- [`Paket-sichtbare Test-Helfer`](./test--helfer.md)

# Citations


[1] `web-common/src/main/java/de/makno/web/common/component/text/TextViewer.java`
