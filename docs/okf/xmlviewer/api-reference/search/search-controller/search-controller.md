---
type: API Reference
title: SearchController
description: Zustandsbehafteter Controller, der Textsuche über SearchToken-Listen ermittelt, navigiert und Änderungen meldet; Zeichnen und Sichtbarmachen sind ausgelagert.
resource: web-common/src/main/java/de/makno/web/common/component/search/SearchController.java
tags: [api-reference, search, controller, stateful]
timestamp: '2026-07-07T10:00:00+02:00'
---

# Überblick


`SearchController` ist eine `final`, `Serializable` Klasse, die die Textsuche über die
durchsuchbaren [SearchToken](/api-reference/search/search-token/search-token.md)s einer Komponente ermittelt:
Treffer finden und zählen, zwischen ihnen navigieren, und jede Änderung an die
einbettende Komponente melden. Das **Zeichnen** der Treffer ist ausgelagert an einen
[SearchHighlightRenderer](/api-reference/search/search-highlight-renderer/search-highlight-renderer.md); das
**Sichtbarmachen** eines Treffers ist über `SearchToken#onReveal()` entkoppelt. Mehr zur
Architektur in [Geteilte Such-Engine](/architecture/search-engine.md); Konsumenten sind
[XmlViewer](/components/xmlviewer.md) und [TextViewer](/components/textviewer.md), meist
gesteuert über [SearchNavigator](/components/search-navigator.md).

**Thread-Safety: explizit NICHT thread-sicher.** Laut Klassen-Javadoc hält der Controller
veränderlichen Such-Zustand (`matches`, `currentMatchIndex`, `currentQuery`,
`caseSensitive`, `termSplitter` — keines dieser Felder ist `final`, keine Synchronisation
im Code) und gehört zu genau einer Komponente, also zu einer UI/Session. Zugriff darf nur
aus dem Session-Thread erfolgen. Es gibt keinerlei `synchronized`-Blöcke oder
Concurrency-Utilities im Code — die Aussage ist durch Abwesenheit jeglicher
Synchronisationsmechanik verifiziert.

# Felder

| Feld | Typ | Bedeutung | null-erlaubt |
|---|---|---|---|
| `serialVersionUID` | `private static final long` | Serialisierungs-Versionskonstante, Wert `1L` (verifiziert). | entfällt (primitiv `long`) |
| `DEFAULT_TERM_SPLITTER` | `public static final SearchTermSplitter` | Standard-Aufteilung: trennt an Whitespace (`\s+`), verwirft leere Begriffe; liefert `List.of()` bei `null`/blank Query. | nein — Lambda-Konstante, nie `null` |
| `tokens` | `private final List<SearchToken>` | Die durchsuchbaren Tokens der Komponente, im Konstruktor übergeben. | Feld selbst nie `null` (Konstruktorparameter wird ungeprüft übernommen — kein `requireNonNull`, siehe Konstruktor); Aufrufer muss eine gültige Liste liefern |
| `highlightRenderer` | `private final SearchHighlightRenderer` | Ausgelagerte Zeichenlogik, im Konstruktor übergeben. | wie `tokens` — ungeprüft übernommen |
| `onMatchChange` | `private final SerializableRunnable` | Callback, der nach jeder Zustandsänderung (Suche, Navigation, Löschen) ausgeführt wird. | wie `tokens` — ungeprüft übernommen |
| `matches` | `private List<TokenMatch>` | Aktuelle Trefferliste in Dokumentreihenfolge. Default `List.of()` (leer). | nein — immer mindestens eine leere Liste, nie `null` (Zuweisungen im Code verifiziert: stets `List.of()` oder `collectMatches(...)`-Ergebnis) |
| `currentMatchIndex` | `private int` | Index des aktuellen Treffers in `matches`, oder `-1` ohne aktiven Treffer. Default `-1`. | entfällt (primitiv) |
| `currentQuery` | `private String` | Zuletzt gesetzte Suchanfrage (Rohtext vor dem Splitten). Default `null`. | ja — `null` bedeutet „keine aktive Suche" (siehe `hasActiveQuery()`) |
| `caseSensitive` | `private boolean` | Ob die Suche Groß-/Kleinschreibung beachtet. Default `false`. | entfällt (primitiv) |
| `termSplitter` | `private SearchTermSplitter` | Aktuell verwendete Begriff-Aufteilung, austauschbar via `setTermSplitter(...)`. Default `DEFAULT_TERM_SPLITTER`. | nein — Setter erzwingt `Objects.requireNonNull(termSplitter, "termSplitter")`; Default ist ebenfalls nie `null` |

# Thread-Safety

**Explizit nicht thread-sicher** (verifiziert per Klassen-Javadoc und Code-Inspektion):
der Controller hält veränderlichen Such-Zustand in den nicht-`final`-Feldern `matches`,
`currentMatchIndex`, `currentQuery`, `caseSensitive` und `termSplitter`. Keines dieser
Felder ist synchronisiert, es gibt weder `synchronized`-Blöcke noch
Concurrency-Utilities im gesamten Quellcode (verifiziert durch Abwesenheit jeglicher
Synchronisationsmechanik). Der Controller gehört zu genau einer Komponente und damit
zu einer UI/Session; Zugriff ist ausschließlich aus dem Session-Thread vorgesehen.

# Serialisierung

`implements Serializable` mit explizit gesetztem
`private static final long serialVersionUID = 1L` (verifiziert). Alle Instanzfelder
sind entweder primitiv (`currentMatchIndex`, `caseSensitive`), `String`
(`currentQuery`) oder Typen aus dem eigenen, ebenfalls serialisierbaren `search`-Package
(`List<SearchToken>`, `SearchHighlightRenderer`, `SerializableRunnable`,
`SearchTermSplitter`) — konsistent mit dem Zweck, Vaadin-Session-Zustand
serialisierbar zu halten. Solange sich die Feldstruktur nicht ändert, bleibt die
`serialVersionUID` stabil kompatibel zu vorher serialisierten Session-Zuständen.

# equals/hashCode/toString

Keine dieser Methoden ist überschrieben (verifiziert: keine `equals`-/`hashCode`-/
`toString`-Deklaration im Quellcode) — es gilt die **Identitätssemantik von
`java.lang.Object`** (`==`-Vergleich, identitätsbasierter Hashcode, `toString()`
liefert Klassenname+Hashcode). Da je Komponente genau eine Instanz gehalten wird
(kein Sammlungs-/Vergleichs-Kontext), hat das in der Praxis keine Auswirkung.

# Vererbungshierarchie


**Vorwärts (eigene Deklaration):** `public final class SearchController implements
Serializable`.

- **Superklasse:** keine explizite (impliziter `Object`).
- **Interfaces:**
  - `java.io.Serializable` — JDK-Standard-Interface (Marker-Interface, keine Methoden), kein
    Projekt-Typ, daher kein Cross-Link. `SearchController` implementiert dieses **direkt**
    (nicht über ein eigenes projektinternes Interface), im Unterschied zu
    [SearchHighlightRenderer](/api-reference/search/search-highlight-renderer/search-highlight-renderer.md) oder
    [SearchTermSplitter](/api-reference/search/search-term-splitter/search-term-splitter.md), die selbst `Serializable`
    erweitern und dann von konkreten Typen implementiert werden.
- Die Klasse ist `final` — es kann keine Subklasse geben.

**Rückwärts (Abhängige):** Verifiziert per Grep auf `extends SearchController` über den
gesamten `web-common/src/main/java/de/makno/web/common/component/`-Baum — **kein Treffer**
(erwartungsgemäß, die Klasse ist `final`). `SearchController` wird von
[XmlViewer](/api-reference/xmlviewer/xml-viewer/xml-viewer.md) und [TextViewer](/api-reference/text/text-viewer/text-viewer.md) als
**Feld gehalten** (Komposition, nicht Vererbung) — siehe Überblick.

# Konstruktoren

- [siehe constructor.md](./constructor.md)

# Methoden

- [`getMatchCount()`](./get-match-count.md)
- [`getCurrentMatchIndex()`](./get-current-match-index.md)
- [`setCaseSensitive(boolean caseSensitive)`](./set-case-sensitive.md)
- [`setTermSplitter(SearchTermSplitter termSplitter)`](./set-term-splitter.md)
- [`search(String query)`](./search.md)
- [`nextMatch()`](./next-match.md)
- [`previousMatch()`](./previous-match.md)
- [`clearSearch()`](./clear-search.md)
- [`collectMatches(List<String> terms)`](./collect-matches.md) *(private)*
- [`revealMatches()`](./reveal-matches.md) *(private)*
- [`moveCurrentTo(int newIndex)`](./move-current-to.md) *(private)*
- [`revealOf(TokenMatch match)`](./reveal-of.md) *(private)*
- [`splitTerms(String query)`](./split-terms.md) *(private)*
- [`findMatchRanges(String text, List<String> terms)`](./find-match-ranges.md) *(private)*
- [`mergeOverlaps(List<int[]> sortedRanges)`](./merge-overlaps.md) *(private static)*
- [`hasActiveQuery()`](./has-active-query.md) *(private)*
- [`notifyChange()`](./notify-change.md) *(private)*

# Citations


[1] web-common/src/main/java/de/makno/web/common/component/search/SearchController.java
