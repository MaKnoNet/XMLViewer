---
type: API Reference
title: XmlViewer
description: Vollständige, verifizierte Methodenreferenz der Vaadin-Komponente XmlViewer — Konstruktoren, alle öffentlichen und package-private Methoden mit Null-Verhalten, Rückgabewerten und Exceptions.
resource: web-common/src/main/java/de/makno/web/common/component/xmlviewer/XmlViewer.java
tags: [api-reference, xmlviewer, vaadin, search]
timestamp: '2026-07-07T10:00:00+02:00'
---

# Überblick


`XmlViewer` ist eine `public class XmlViewer extends Composite<Div> implements HasSize,
HasStyle, MatchNavigable`. Die narrative Beschreibung (Zweck, Rendering-Prinzip, Sicherheit)
steht bereits in [XmlViewer (Komponenten-Doku)](/components/xmlviewer.md) — diese Datei
konzentriert sich ausschließlich auf die erschöpfende, verifizierte Methodenreferenz.

**Thread-Safety (aus Javadoc übernommen und gegen den Code plausibilisiert):** Nicht
thread-safe. Eine Instanz gehört zu genau einer UI/Session; alle Felder (`root`,
`collapsible`, `tree`, `searchController`, `highlightedElements`, …) sind einfache
Instanzfelder ohne Synchronisation — die Klasse verlässt sich vollständig auf das
Vaadin-Session-Lock-Modell. `highlightedElements` ist ein `Collections.newSetFromMap(new
IdentityHashMap<>())`, also identitätsbasiert (kein `equals`-Vergleich der JDOM2-Elemente).

Implementiert [MatchNavigable](/api-reference/navigation/match-navigable/match-navigable.md); nutzbar mit
[SearchNavigator](/api-reference/navigation/search-navigator/search-navigator.md).

# Felder

| Feld | Typ | Bedeutung | null-erlaubt |
|---|---|---|---|
| `serialVersionUID` | `private static final long` | Explizite Serialisierungs-ID (`1L`). | entfällt (primitiv) |
| `EMPTY_TEXT` | `private static final String` | Platzhaltertext, wenn kein Wurzelelement gesetzt ist (`"Kein XML-Element gesetzt."`). | entfällt (String-Literal) |
| `root` | `Element` | Aktuell angezeigtes JDOM2-Wurzelelement, gesetzt via `setRoot(Element)`. | ja — Default `null` (kein Element gesetzt); `render()` behandelt `null` explizit (zeigt Platzhalter) |
| `collapsible` | `boolean` | Ob Ein-/Ausklapp-Dreiecke angezeigt werden. Default `true`. | entfällt (primitiv) |
| `searchCaseSensitive` | `boolean` | Ob die Suche Groß-/Kleinschreibung beachtet. Default `false`. | entfällt (primitiv) |
| `searchTermSplitter` | `SearchTermSplitter` | Zerlegt den Suchtext in einzeln hervorzuhebende Begriffe. Default `SearchController.DEFAULT_TERM_SPLITTER`. | nein — Setter erzwingt `Objects.requireNonNull(splitter, "splitter")`; Feld durch Default-Wert nie `null` |
| `tree` | `RenderedTree` | Ergebnis des letzten Render-Durchlaufs, neu gesetzt in jedem `render()`-Aufruf (auch als `emptyTree()`, falls `root == null`). | nein nach Konstruktion — Konstruktor ruft immer `render()` auf, das `tree` unbedingt zuweist (echter oder leerer Baum); vor dem ersten `render()`-Aufruf theoretisch `null`, tritt praktisch nie auf |
| `searchController` | `SearchController` | Steuert die programmatische Suche (Treffer, Navigation), neu erzeugt in jedem `render()`-Aufruf. | wie `tree` — nach Konstruktion praktisch nie `null` |
| `highlightRenderer` | `final SearchHighlightRenderer` | Zeichnet Suchtreffer im Frontend (CSS Custom Highlight API); konkrete Implementierung `FrontendSearchHighlighter`, mit `new FrontendSearchHighlighter(this)` initialisiert. | nein (final, sofort mit konkreter Instanz initialisiert) |
| `highlightedElements` | `final Set<Element>` | Identitätsbasierte Menge aller aktuell hervorgehobenen Elemente, mit `Collections.newSetFromMap(new IdentityHashMap<>())` initialisiert. | Feld selbst nie `null`; Elemente nie `null` (Methoden wie `highlight(Element)` prüfen `element == null` vor dem Hinzufügen) |

Zehn Felder insgesamt (zwei `static`-Konstanten + acht Instanzfelder). Kein Feld ist `static`
und veränderlich — die einzigen `static`-Felder (`serialVersionUID`, `EMPTY_TEXT`) sind `final`.

# Thread-Safety

**Nicht thread-safe** (aus Javadoc übernommen und gegen den Code plausibilisiert). Eine
Instanz gehört wie jede Vaadin-Komponente zu genau einer `UI`/Session; alle Methoden dürfen
nur aus dem an die Session gebundenen Thread (mit gehaltenem Session-Lock) aufgerufen werden.
Sämtliche Instanzfelder (`root`, `collapsible`, `searchCaseSensitive`, `searchTermSplitter`,
`tree`, `searchController`, `highlightRenderer`, `highlightedElements`) sind einfache
Instanzfelder ohne eigene Synchronisation — die Klasse verlässt sich vollständig auf das
Vaadin-Session-Lock-Modell, führt selbst keine `synchronized`-Blöcke oder
nebenläufigkeitssicheren Collections. `highlightedElements` ist zusätzlich identitätsbasiert
(`IdentityHashMap`-Backing), sodass JDOM2-`Element`-Instanzen per `==` statt `equals` verglichen
werden. Instanzen werden nicht zwischen Benutzern/Sessions geteilt — pro UI-Session eine eigene
`XmlViewer`-Instanz (single-use pro UI-Session/Request, kein Pooling).

# Serialisierung

**`Serializable`** (transitiv über `Composite<Div>`, das seinerseits Vaadin-Flow-`Component`
implementiert, welches `Serializable` implementiert — Standard-Vertrag aller Vaadin-Flow-
Komponenten für die Session-Serialisierung). Explizit gesetzte
`private static final long serialVersionUID = 1L;` (verifiziert im Quellcode) stabilisiert die
Kompatibilität über Code-Änderungen hinweg. Von den Instanzfeldern sind `root` (JDOM2
`Element`, serialisierbar), `searchTermSplitter` (funktionales Interface,
`@FunctionalInterface`-typisch serialisierbar auszulegen), `tree` (`RenderedTree`, selbst
`Serializable` mit eigener `serialVersionUID`) sowie `highlightedElements`
(`IdentityHashMap`-basiertes `Set`) alle mit-serialisierbar; `highlightRenderer` referenziert
`FrontendSearchHighlighter`, das wie andere Vaadin-Komponenten-Helfer für die
Session-Serialisierung ausgelegt ist.

# equals/hashCode/toString

Keine dieser Methoden ist in `XmlViewer` überschrieben (verifiziert: keine
`equals`/`hashCode`/`toString`-Deklaration im Quellcode) — es gilt die **geerbte
Identitätssemantik von `Composite`/`Component`** (Vaadin-Flow-Basisklassen definieren ebenfalls
keine wertbasierte `equals`/`hashCode`, sodass letztlich `java.lang.Object`s
Identitätsvergleich (`==`) greift; `toString()` liefert Klassenname+Hashcode, sofern auch
`Composite`/`Component` es nicht überschreiben). Da UI-Komponenten ohnehin nicht in
Werte-Sammlungen (z.&nbsp;B. `HashSet`) mit inhaltlicher Gleichheit verwendet werden, hat dies
in der Praxis geringe Relevanz.

# Vererbungshierarchie


**Vorwärts (eigene Deklaration):** `public class XmlViewer extends Composite<Div> implements
HasSize, HasStyle, MatchNavigable`.

- **Superklasse:** `Composite<Div>` — Vaadin-Flow-Framework-Klasse
  (`com.vaadin.flow.component.Composite`), kein Projekt-Typ, daher kein Cross-Link.
- **Interfaces:**
  - `HasSize` — Vaadin-Flow-Framework-Interface, extern.
  - `HasStyle` — Vaadin-Flow-Framework-Interface, extern.
  - [MatchNavigable](/api-reference/navigation/match-navigable/match-navigable.md) — projektinternes
    Entkopplungs-Interface aus `navigation`; `XmlViewer` implementiert dessen fünf Methoden
    (`search`, `nextMatch`, `previousMatch`, `getMatchCount`, `getCurrentMatchIndex`,
    `addMatchChangeListener`), um vom [SearchNavigator](/api-reference/navigation/search-navigator/search-navigator.md)
    gesteuert werden zu können.

**Rückwärts (Abhängige):** Verifiziert per Grep auf `extends XmlViewer` /
`implements ... XmlViewer` über den gesamten
`web-common/src/main/java/de/makno/web/common/component/`-Baum — **kein Treffer**. Keine
andere Klasse im Projekt erweitert `XmlViewer` oder implementiert es als Interface (es ist
ohnehin eine `class`, kein Interface). `XmlViewer` hat somit keine projektinternen
Subklassen.

# Konstruktoren

- [siehe constructor.md](./constructor.md)

# Methoden

- [`public void setRoot(Element root)`](./set-root.md)
- [`public Element getRoot()`](./get-root.md)
- [`public void highlight(Element element)`](./highlight.md)
- [`public void clearHighlight(Element element)`](./clear-highlight.md)
- [`public void expandAll()`](./expand-all.md)
- [`public void collapseAll()`](./collapse-all.md)
- [`public void setCollapsible(boolean collapsible)`](./set-collapsible.md)
- [`public boolean isCollapsible()`](./is-collapsible.md)
- [`public void search(String query)`](./search.md) *(implements `MatchNavigable`)*
- [`public void nextMatch()`](./next-match.md) *(implements `MatchNavigable`)*
- [`public void previousMatch()`](./previous-match.md) *(implements `MatchNavigable`)*
- [`public void clearSearch()`](./clear-search.md)
- [`public int getMatchCount()`](./get-match-count.md) *(implements `MatchNavigable`)*
- [`public int getCurrentMatchIndex()`](./get-current-match-index.md) *(implements `MatchNavigable`)*
- [`public void setSearchCaseSensitive(boolean caseSensitive)`](./set-search-case-sensitive.md)
- [`public boolean isSearchCaseSensitive()`](./is-search-case-sensitive.md)
- [`public void setSearchTermSplitter(SearchTermSplitter splitter)`](./set-search-term-splitter.md)
- [`public Registration addMatchChangeListener(ComponentEventListener<MatchChangeEvent> listener)`](./add-match-change-listener.md) *(implements `MatchNavigable`)*

# Citations


[1] web-common/src/main/java/de/makno/web/common/component/xmlviewer/XmlViewer.java
