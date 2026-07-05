---
type: API Reference
title: CodeViewer
description: Vaadin-Flow-Komponente, dünner Wrapper um CodeMirror 6, für read-only Quelltextanzeige mit Syntax-Highlighting, Code-Falten und Theme-Umschaltung.
resource: web-common/src/main/java/de/makno/web/common/component/code/CodeViewer.java
tags: [api-reference, code, vaadin, codemirror, component]
timestamp: '2026-07-07T10:00:00+02:00'
---

# Überblick


`CodeViewer` (`public class CodeViewer extends Div implements MatchNavigable`) ist ein dünner
Vaadin-Wrapper um [CodeMirror 6](https://codemirror.net/) — siehe
[CodeViewer (Narrative)](/components/codeviewer.md) für die architektonische Einordnung. Der
CodeMirror-`EditorView` selbst lebt ausschließlich clientseitig; serverseitig hält die Komponente nur
serialisierbaren Zustand (Text, Sprache, Theme, Optionen, gespiegelter Treffer-Stand). Die Suche
selbst führt **CodeMirror aus** (nicht die geteilte Server-Such-Engine aus
[/architecture/search-engine.md](/architecture/search-engine.md)); `CodeViewer` implementiert
`MatchNavigable` nur, um Trefferanzahl/-index serverseitig zu spiegeln und mit dem
[SearchNavigator](/components/search-navigator.md) koppelbar zu sein.

**Zustand/Thread-Safety:** Die Klasse ist **mutable** (Felder für Text, Sprache, Theme, Wrap,
Zeilennummern, Case-Sensitivity, letzte Suchanfrage, gespiegelter Treffer-Stand). Laut Klassen-Javadoc
**nicht thread-safe** — wie jede Vaadin-Komponente an genau eine `UI`/Session gebunden, Methoden nur
aus dem Session-Thread aufrufen, eine Instanz pro UI. Keine Synchronisation im Code — konsistent mit
dem dokumentierten Vertrag (Schutz obliegt dem Vaadin-Session-Lock). `serialVersionUID = 1L` ist
explizit gesetzt.

# Felder

| Feld | Typ | Bedeutung | null-erlaubt |
|---|---|---|---|
| `serialVersionUID` | `private static final long` | `1L` — explizit gesetzte Serialisierungs-Version. | entfällt (primitiv) |
| `text` | `private String` | Angezeigter Quelltext, Default `""`. | nein — Setter/Konstruktor normalisieren `null` auf `""` |
| `language` | `private CodeLanguage` | Explizit gesetzte Sprache. | ja — `null` bedeutet Auto-Erkennung aus dem Inhalt via `CodeLanguageDetector.fromContent` (verifiziert in `effectiveLanguageId()`) |
| `dark` | `private boolean` | Dunkles Theme ein/aus, Default `false` (hell). | entfällt (primitiv) |
| `wrap` | `private boolean` | Zeilenumbruch ein/aus, Default `false`. | entfällt (primitiv) |
| `showLineNumbers` | `private boolean` | Zeilennummern-Spalte ein/aus, Default `true`. | entfällt (primitiv) |
| `searchCaseSensitive` | `private boolean` | Groß-/Kleinschreibungsabgleich der Suche, Default `false`. | entfällt (primitiv) |
| `lastQuery` | `private String` | Zuletzt ausgeführte Suchanfrage, Default `""`; wird bei `setSearchCaseSensitive` zum Re-Run genutzt. | nein — stets auf `""` statt `null` gesetzt |
| `matchCount` | `private int` | Vom Client (CodeMirror) gespiegelte Trefferanzahl, Default `0`. | entfällt (primitiv) |
| `currentMatchIndex` | `private int` | Vom Client gespiegelter 0-basierter aktueller Treffer-Index, Default `-1`. | entfällt (primitiv) |

# Thread-Safety

**Nicht thread-sicher** (laut Klassen-Javadoc verifiziert): wie jede Vaadin-Komponente an genau
eine `UI`/Session gebunden — Methoden dürfen nur aus dem Session-Thread aufgerufen werden, eine
Instanz pro UI/Request. Die Klasse ist mutable (Text, Sprache, Theme, Wrap, Zeilennummern,
Case-Sensitivity, letzte Suchanfrage, gespiegelter Treffer-Stand) und synchronisiert nichts
selbst — konsistent mit dem dokumentierten Vertrag, da der Schutz dem Vaadin-Session-Lock
obliegt.

# Serialisierung

**`Serializable`** (transitiv über `Div`/`Component`) mit explizit gesetzter
`private static final long serialVersionUID = 1L`. Laut Klassen-Javadoc ("Architektur")
bewusst so entworfen: Der CodeMirror-`EditorView` lebt nur clientseitig, serverseitig wird nur
der serialisierbare Zustand gehalten (Text, Sprache, Theme, Optionen, Treffer-Stand); bei jedem
`onAttach` wird der Editor daraus clientseitig neu aufgebaut. Damit übersteht die Komponente
Session-Clustering/Re-Attach ohne den nicht-serialisierbaren Editor-Zustand mitschleppen zu
müssen.

# equals/hashCode/toString

Keine dieser Methoden ist überschrieben (verifiziert: keine `equals`/`hashCode`/`toString`-
Deklaration im Quellcode) — es gilt die **Identitätssemantik der geerbten `Component`/`Object`-
Implementierung** (`==`-Vergleich, identitätsbasierter Hashcode). Konsistent mit dem
Single-Instance-pro-UI-Vertrag.

# Vererbungshierarchie


**Vorwärts (eigene Deklaration):** `public class CodeViewer extends Div implements
MatchNavigable`.

- **Superklasse:** `Div` — Vaadin-Flow-Framework-Klasse (`com.vaadin.flow.component.html.Div`),
  kein Projekt-Typ, daher kein Cross-Link. Im Unterschied zu `XmlViewer`/`TextViewer` erweitert
  `CodeViewer` `Div` direkt statt über `Composite<Div>` zu delegieren.
- **Interfaces:**
  - [MatchNavigable](/api-reference/navigation/match-navigable/match-navigable.md) — projektinternes
    Entkopplungs-Interface aus `navigation`; `CodeViewer` implementiert dessen Methoden, um
    Trefferanzahl/-index serverseitig zu spiegeln und mit dem
    [SearchNavigator](/api-reference/navigation/search-navigator/search-navigator.md) koppelbar zu sein (die eigentliche
    Suche führt CodeMirror clientseitig aus, siehe Überblick oben).

**Rückwärts (Abhängige):** Verifiziert per Grep auf `extends CodeViewer` /
`implements ... CodeViewer` über den gesamten
`web-common/src/main/java/de/makno/web/common/component/`-Baum — **kein Treffer**. Keine
andere Klasse im Projekt erweitert `CodeViewer`; keine projektinternen Subklassen.

# Konstruktoren

- [siehe constructor.md](./constructor.md)

# Methoden

- [``protected void onAttach(AttachEvent attachEvent)``](./on-attach.md)
- [``protected void onDetach(DetachEvent detachEvent)``](./on-detach.md)
- [``public void setText(String text)``](./set-text.md)
- [``public String getText()``](./get-text.md)
- [``public void setLanguage(CodeLanguage language)``](./set-language.md)
- [``public void setLanguageFromFileName(String fileName)``](./set-language-from-file-name.md)
- [``public CodeLanguage getLanguage()``](./get-language.md)
- [``private String effectiveLanguageId()``](./effective-language-id.md)
- [``public void setDark(boolean dark)``](./set-dark.md)
- [``public boolean isDark()``](./is-dark.md)
- [``public void setWrap(boolean wrap)``](./set-wrap.md)
- [``public boolean isWrap()``](./is-wrap.md)
- [``public void setShowLineNumbers(boolean showLineNumbers)``](./set-show-line-numbers.md)
- [``public boolean isShowLineNumbers()``](./is-show-line-numbers.md)
- [``public void foldAll()``](./fold-all.md)
- [``public void unfoldAll()``](./unfold-all.md)
- [`public void search(String query)`](./search.md) *(implements `MatchNavigable`)*
- [`public void nextMatch()`](./next-match.md) *(implements `MatchNavigable`)*
- [`public void previousMatch()`](./previous-match.md) *(implements `MatchNavigable`)*
- [`public void clearSearch()`](./clear-search.md)
- [`public int getMatchCount()`](./get-match-count.md) *(implements `MatchNavigable`)*
- [`public int getCurrentMatchIndex()`](./get-current-match-index.md) *(implements `MatchNavigable`)*
- [`public void setSearchCaseSensitive(boolean caseSensitive)`](./set-search-case-sensitive.md)
- [`public boolean isSearchCaseSensitive()`](./is-search-case-sensitive.md)
- [`public Registration addMatchChangeListener(ComponentEventListener<MatchChangeEvent> listener)`](./add-match-change-listener.md) *(implements `MatchNavigable`)*
- [``@ClientCallable public void onMatchChange(int count, int index)``](./on-match-change.md)
- [``private void fireSearchReset()``](./fire-search-reset.md)
- [``private void callJs(String function, Serializable... args)``](./call-js.md)

# Citations


[1] `web-common/src/main/java/de/makno/web/common/component/code/CodeViewer.java`
