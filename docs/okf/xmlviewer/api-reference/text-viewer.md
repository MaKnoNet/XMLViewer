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

# Konstruktoren

## `public TextViewer()`

Parameterlos. Fügt dem inneren `Div` (`getContent()`) die CSS-Klasse `TextCssClasses.ROOT` hinzu und
ruft `render()` auf, was mit leerem Text (`text = ""`) den Empty-Placeholder rendert und einen neuen
`SearchController` mit leeren Tokens anlegt. Wirft keine Exception; kein Parameter, daher keine
Null-Prüfung nötig.

## `public TextViewer(String text)`

| Parameter | Typ | null-erlaubt | Verifikation |
|---|---|---|---|
| `text` | `String` | ja | Ruft `this()` und dann `setText(text)` auf; `setText` behandelt `null` explizit als leeren String (`text == null ? "" : text`, Zeile 102). Kein NPE möglich. |

Kein `throws`; wirft keine Exception im Rumpf.

# Methoden

## `public void setText(String text)`

| Parameter | Typ | null-erlaubt | Verifikation |
|---|---|---|---|
| `text` | `String` | ja | Zeile 102: `this.text = text == null ? "" : text;` — `null` wird explizit zu `""` normalisiert, exakt wie der Methoden-Javadoc verspricht. |

Rückgabewert: `void`. Löst intern `render()` aus (kompletter Re-Render inkl. neuem
`SearchController`, wodurch eine zuvor aktive Suche implizit zurückgesetzt wird — `fireSearchReset()`
wird am Ende von `render()` gefeuert). Keine deklarierten oder geworfenen Exceptions.

## `public String getText()`

Keine Parameter. Rückgabewert: `String`, **niemals `null`** — das Feld `text` wird nur über
`setText` gesetzt, welches `null` bereits auf `""` normalisiert, und im Konstruktor mit `""`
initialisiert. Keine Exceptions.

## `public void setShowLineNumbers(boolean showLineNumbers)`

Primitive `boolean`, keine Null-Frage. Setzt das Feld und ruft `render()` auf (kompletter
Re-Render, damit die Gutter-Spans hinzugefügt/entfernt werden). Keine Exceptions.

## `public boolean isShowLineNumbers()`

Keine Parameter, gibt `boolean` zurück (kein `null` möglich, primitiver Typ). Keine Exceptions.

## `public void setWrap(boolean wrap)`

Primitive `boolean`. Setzt das Feld und togglet **nur** die CSS-Klasse `TextCssClasses.WRAP` am
Wurzelelement (`getContent().getElement().getClassList().set(...)`) — **kein** Re-Render, wie der
Javadoc-Kommentar korrekt behauptet ("Kein Neu-Rendern nötig"). Keine Exceptions.

## `public boolean isWrap()`

Keine Parameter, `boolean`-Rückgabe. Keine Exceptions.

## `public void highlight(int line)`

| Parameter | Typ | null-erlaubt | Verifikation |
|---|---|---|---|
| `line` | `int` | n/a (primitiv) | Bereichsprüfung im Code: `if (line < 0 || line >= lineElements.size()) return;` (Zeile 150–152). |

Rückgabewert: `void`. Verhalten laut Javadoc **und** verifiziertem Code: Index außerhalb des
gültigen Bereichs wird **stillschweigend ignoriert** (kein Exception, kein Effekt) — deckt sich mit
der Javadoc-Aussage „Außerhalb des Bereichs liegende Indizes werden ignoriert." Innerhalb des
Bereichs wird `TextCssClasses.HIGHLIGHT` zur betroffenen Zeile hinzugefügt, der Index in
`highlightedLines` vermerkt (mehrere Zeilen können gleichzeitig hervorgehoben sein, bereits
markierte bleiben erhalten) und per `scrollTo(row)` in den sichtbaren Bereich gescrollt. Keine
Exceptions möglich.

## `public void clearHighlight()`

Keine Parameter. Entfernt `TextCssClasses.HIGHLIGHT` von allen in `highlightedLines` vermerkten
Zeilen-Elementen und leert die Menge. `void`-Rückgabe, keine Exceptions. Wird auch intern am Anfang
von `render()` aufgerufen, um Highlights eines alten Renderings zu verwerfen.

## `public void search(String query)` *(implements `MatchNavigable`)*

| Parameter | Typ | null-erlaubt | Verifikation |
|---|---|---|---|
| `query` | `String` | verhält sich wie null-erlaubt aus Sicht von `TextViewer`, da die Methode `query` ungeprüft an `searchController.search(query)` weiterreicht (Zeile 175) | Kein eigener Null-Check in `TextViewer`; das tatsächliche Verhalten bei `null` hängt von `SearchController.search(String)` ab (außerhalb des dokumentierten Scopes dieser Datei, siehe [Such-Engine](/architecture/search-engine.md)). Der Klassen-Javadoc von `TextViewer` behauptet „Leerer/`null`-Text löscht die Suche" — das ist eine Aussage über `SearchController`s Verhalten, nicht direkt im Rumpf von `TextViewer.search` verifizierbar. |

Rückgabewert: `void`. Keine Exceptions im Rumpf von `TextViewer` selbst.

## `public void nextMatch()` *(implements `MatchNavigable`)*

Keine Parameter. Delegiert an `searchController.nextMatch()`. `void`. Keine Exceptions im
`TextViewer`-Rumpf.

## `public void previousMatch()` *(implements `MatchNavigable`)*

Keine Parameter. Delegiert an `searchController.previousMatch()`. `void`. Keine Exceptions im
`TextViewer`-Rumpf.

## `public void clearSearch()`

Keine Parameter. Delegiert an `searchController.clearSearch()`. `void`. Keine Exceptions im
`TextViewer`-Rumpf.

## `public int getMatchCount()` *(implements `MatchNavigable`)*

Keine Parameter. Delegiert an `searchController.getMatchCount()`, gibt `int` zurück (kein `null`
möglich, primitiver Typ). Keine Exceptions im `TextViewer`-Rumpf.

## `public int getCurrentMatchIndex()` *(implements `MatchNavigable`)*

Keine Parameter. Delegiert an `searchController.getCurrentMatchIndex()`. Laut Javadoc „0-basierter
Index des aktuellen Treffers, oder `-1`, wenn keiner aktiv ist" — dieser Vertrag wird von
`SearchController` erfüllt, nicht in `TextViewer` selbst geprüft. `int`-Rückgabe, keine Exceptions.

## `public void setSearchCaseSensitive(boolean caseSensitive)`

Primitive `boolean`. Setzt Feld und ruft `searchController.setCaseSensitive(caseSensitive)` auf.
`void`, keine Exceptions.

## `public boolean isSearchCaseSensitive()`

Keine Parameter, `boolean`-Rückgabe. Keine Exceptions.

## `public void setSearchTermSplitter(SearchTermSplitter splitter)`

| Parameter | Typ | null-erlaubt | Verifikation |
|---|---|---|---|
| `splitter` | `SearchTermSplitter` | **nein** | Zeile 224: `this.searchTermSplitter = Objects.requireNonNull(splitter, "splitter");` — explizite Null-Prüfung. |

Rückgabewert: `void`. **Geworfene Exception:** `NullPointerException` (durch
`Objects.requireNonNull`) wenn `splitter == null` — Nachricht `"splitter"`. Dies ist der einzige
Setter der Klasse mit explizitem Null-Check; alle anderen `set*`-Methoden akzeptieren `null`
stillschweigend oder normalisieren es. Bei gültigem Splitter wird zusätzlich
`searchController.setTermSplitter(splitter)` aufgerufen — eine aktive Suche wird laut Javadoc "sofort
neu ausgeführt" (das eigentliche Neu-Ausführen passiert innerhalb von `SearchController`, außerhalb
des hier verifizierten Rumpfs).

## `public Registration addMatchChangeListener(ComponentEventListener<MatchChangeEvent> listener)` *(implements `MatchNavigable`)*

| Parameter | Typ | null-erlaubt | Verifikation |
|---|---|---|---|
| `listener` | `ComponentEventListener<MatchChangeEvent>` | im `TextViewer`-Rumpf nicht geprüft | Wird direkt an Vaadins `addListener(MatchChangeEvent.class, listener)` weitergereicht (geerbte Vaadin-Infrastruktur, kein eigener Null-Check in `TextViewer`). |

Rückgabewert: `Registration`, laut Vaadin-Vertrag nie `null` (Standardverhalten von
`Component.addListener`). Keine expliziten `throw`-Statements im `TextViewer`-Rumpf.

## `private void render()`

Package-interne Helfer-Methode (hier der Vollständigkeit halber dokumentiert, da sie zentrale Logik
enthält): Baut die Zeilen komplett neu auf. Bei leerem `text` (`text.isEmpty()`) wird
`lineElements`/`lineTexts` auf `List.of()` gesetzt und ein Empty-Placeholder (`TextCssClasses.EMPTY`)
gerendert; sonst werden Zeilen über `splitLines(text)` erzeugt, je Zeile ein `Div` mit optionalem
Gutter und einem `SearchToken` gebaut. Am Ende wird immer ein neuer `SearchController` mit den
aktuellen Tokens instanziiert, Case-Sensitivity und Splitter erneut gesetzt, der Highlight-Renderer
geleert und `fireSearchReset()` gefeuert. Kein Parameter, `void`, keine Exceptions.

## `private Div newLine(int index, String lineText)`

| Parameter | Typ | Verifikation |
|---|---|---|
| `index` | `int` | Nur zur Nummerierung verwendet (`Integer.toString(index + 1)`), keine Bereichsprüfung nötig, da nur intern mit gültigen Indizes aufgerufen. |
| `lineText` | `String` | Kein Null-Check; wird direkt in `new Span(lineText)` übergeben. Vaadins `Span`-Konstruktor akzeptiert laut Vaadin-API auch `null`/leere Strings; da `render()` diese Methode nur mit Einträgen aus `lineTexts` (nie `null`, siehe `splitLines`) aufruft, ist `null` hier praktisch ausgeschlossen. |

Rückgabewert: `Div`, nie `null`. Keine Exceptions.

## `private Div newEmptyPlaceholder()`

Keine Parameter. Baut das Platzhalter-`Div` mit dem festen Text `EMPTY_TEXT` ("Kein Text gesetzt.").
Rückgabewert: `Div`, nie `null`. Keine Exceptions.

## `private static List<String> splitLines(String text)`

| Parameter | Typ | null-erlaubt | Verifikation |
|---|---|---|---|
| `text` | `String` | **nein, nicht in der Praxis** | Kein expliziter Null-Check im Rumpf; ruft direkt `text.replace(...)` auf. Bei `text == null` würde eine `NullPointerException` an dieser Stelle geworfen. In der Praxis unkritisch, da `render()` diese Methode nur aufruft, wenn `text.isEmpty()` bereits `false` war — das Feld `text` der Klasse ist durch `setText`/den Konstruktor nie `null` (siehe oben). Wird also nie mit `null` aufgerufen, hat aber selbst **keine** Absicherung dagegen. |

Rückgabewert: `List<String>`, nie `null` (mindestens ein Element, da `String.split(..., -1)` bei
leerem String `[""]` liefert — praktisch aber ungenutzt, weil der Aufrufer den leeren Fall vorher
abfängt). CRLF/CR werden zu LF vereinheitlicht; `limit == -1` erhält abschließende Leerzeilen. Keine
deklarierten Exceptions; potenzielle `NullPointerException` bei `text == null` (nicht erreichbar über
den öffentlichen API-Pfad).

## `private void fireMatchChange()`

Keine Parameter. Feuert ein `MatchChangeEvent` mit aktuellem `getMatchCount()`/
`getCurrentMatchIndex()`. `void`, keine Exceptions.

## `private void fireSearchReset()`

Keine Parameter. Feuert ein `MatchChangeEvent` mit zusätzlichem `true`-Flag (Reset-Signalisierung
laut Javadoc-Kommentar). `void`, keine Exceptions.

## `private void scrollTo(Component target)`

| Parameter | Typ | Verifikation |
|---|---|---|
| `target` | `Component` | Kein expliziter Null-Check; ruft `target.getUI()` auf, was bei `target == null` eine `NullPointerException` würfe. Wird intern nur mit einem konkreten `Div` aus `lineElements` aufgerufen, nie mit `null`. |

Rückgabewert: `void`. Scrollt nur, wenn `target.getUI()` ein UI liefert (`Optional` ist nicht leer) —
im Unit-Test ohne Client-Anbindung ist das `Optional` leer, dann passiert nichts (kein Fehler). Keine
Exceptions im Normalfall.

## Paket-sichtbare Test-Helfer

Diese vier Methoden sind package-private (kein Modifier) und laut Kommentar im Quellcode
ausschließlich für Tests gedacht:

### `int lineCount()`

Keine Parameter. Gibt `lineElements.size()` zurück, `int`, nie `null` (primitiv). Keine Exceptions.

### `List<String> searchableTexts()`

Keine Parameter. Gibt eine **unveränderliche Kopie** (`List.copyOf(lineTexts)`) zurück — nie `null`.
`List.copyOf` wirft laut JDK-Doku eine `NullPointerException`, wenn die Quellliste `null`-Elemente
enthält; da `lineTexts` nur über `splitLines`/`List.of()` befüllt wird und beide keine `null`-Einträge
produzieren, ist das praktisch ausgeschlossen. Keine weiteren Exceptions.

### `Div lineOf(int line)`

| Parameter | Typ | Verifikation |
|---|---|---|
| `line` | `int` | **Keine Bereichsprüfung** (anders als `highlight(int)`!). Direkter Aufruf von `lineElements.get(line)`. |

**Geworfene Exception:** `IndexOutOfBoundsException`, wenn `line < 0` oder `line >= lineElements.size()`
— im Gegensatz zu `highlight(int)`, das ungültige Indizes stillschweigend ignoriert. Rückgabewert:
`Div`, nie `null` bei gültigem Index.

### `boolean isLineHighlighted(int line)`

| Parameter | Typ | Verifikation |
|---|---|---|
| `line` | `int` | Wie bei `lineOf`: keine eigene Bereichsprüfung, ruft `lineElements.get(line)` auf. |

**Geworfene Exception:** `IndexOutOfBoundsException` bei ungültigem Index (gleiches Muster wie
`lineOf`). Rückgabewert: `boolean`, prüft, ob die CSS-Klassenliste des Zeilen-Elements
`TextCssClasses.HIGHLIGHT` enthält.

# Cross-Referenzen

- [TextViewer (Narrative)](/components/textviewer.md)
- [Geteilte Such-Engine](/architecture/search-engine.md)
- [SearchNavigator und MatchNavigable](/components/search-navigator.md)
- [Frontend-Integration](/architecture/frontend-integration.md)
- [TextCssClasses](/api-reference/text-css-classes.md)

# Citations

[1] `web-common/src/main/java/de/makno/web/common/component/text/TextViewer.java`
