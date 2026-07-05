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

# Konstruktoren

## `public CodeViewer()`

Parameterlos. Fügt die CSS-Klasse `CodeCssClasses.ROOT` hinzu. Alle Felder bleiben auf ihren
Default-Werten (`text = ""`, `language = null` → Auto-Erkennung, `dark = false`, `wrap = false`,
`showLineNumbers = true`, `searchCaseSensitive = false`). Keine Exceptions.

## `public CodeViewer(String text)`

| Parameter | Typ | null-erlaubt | Verifikation |
|---|---|---|---|
| `text` | `String` | ja | Ruft `this()` auf, dann `this.text = text == null ? "" : text;` (Zeile 77) — `null` wird explizit zu `""` normalisiert. |

Kein `throws`; keine Exception im Rumpf.

## `public CodeViewer(String text, CodeLanguage language)`

| Parameter | Typ | null-erlaubt | Verifikation |
|---|---|---|---|
| `text` | `String` | ja | Ruft `this(text)` auf — siehe oben, normalisiert `null` zu `""`. |
| `language` | `CodeLanguage` | **ja** | Zeile 82: `this.language = language;` — ungeprüfte Zuweisung, `null` wird direkt übernommen. Das ist semantisch korrekt und beabsichtigt: `null` bedeutet laut `getLanguage()`-Javadoc "Auto-Erkennung", nicht ein Fehlerzustand. |

Keine Exceptions im Rumpf.

# Methoden

## `protected void onAttach(AttachEvent attachEvent)`

Vaadin-Lifecycle-Hook, `@Override`. Ruft `super.onAttach(attachEvent)` und dann
`callJs("create", text, effectiveLanguageId(), dark, wrap, showLineNumbers)` auf — baut den
clientseitigen Editor aus dem aktuellen Serverzustand neu auf.

| Parameter | Typ | null-erlaubt | Verifikation |
|---|---|---|---|
| `attachEvent` | `AttachEvent` | kein eigener Null-Check | Wird nur an `super.onAttach(...)` weitergereicht; Vaadin-Framework ruft diese Methode nie mit `null` auf. |

`void`, keine expliziten Exceptions im `CodeViewer`-Rumpf.

## `protected void onDetach(DetachEvent detachEvent)`

Vaadin-Lifecycle-Hook, `@Override`. Führt Best-effort-Cleanup aus: `getElement().executeJs(...)` ruft
`window.MaknoCodeViewer.destroy(this)` clientseitig auf, falls vorhanden (`&&`-Guard im JS selbst),
dann `super.onDetach(detachEvent)`.

| Parameter | Typ | null-erlaubt | Verifikation |
|---|---|---|---|
| `detachEvent` | `DetachEvent` | kein eigener Null-Check | Nur an `super.onDetach(...)` weitergereicht; Vaadin ruft nie mit `null` auf. |

`void`, keine expliziten Exceptions im `CodeViewer`-Rumpf. Laut Kommentar im Code ist dieser Aufruf
bewusst "Best-effort" — falls er beim Detach nicht mehr ankommt, räumt die JS-Registry detachte Hosts
ohnehin lazy per `isConnected`-Prüfung auf.

## `public void setText(String text)`

| Parameter | Typ | null-erlaubt | Verifikation |
|---|---|---|---|
| `text` | `String` | **ja** | Zeile 109: `this.text = text == null ? "" : text;` — explizite Normalisierung, deckt sich mit Javadoc "`null` = leerer Text". |

Setzt zusätzlich `matchCount = 0`, `currentMatchIndex = -1`, `lastQuery = ""` zurück (Suchzustand wird
bei Textänderung verworfen), ruft `callJs("setDoc", this.text, effectiveLanguageId())` auf und feuert
`fireSearchReset()`. `void`, keine Exceptions.

## `public String getText()`

Keine Parameter. Rückgabewert: `String`, **nie `null`** (Feld wird nur über `setText`/Konstruktoren
gesetzt, die `null` stets zu `""` normalisieren; initialer Default ist `""`). Keine Exceptions.

## `public void setLanguage(CodeLanguage language)`

| Parameter | Typ | null-erlaubt | Verifikation |
|---|---|---|---|
| `language` | `CodeLanguage` | **ja, explizit vorgesehen** | Zeile 123: ungeprüfte Zuweisung `this.language = language;`. Laut Javadoc aktiviert `null` "wieder die Auto-Erkennung aus dem Inhalt" — semantisch gültiger Wert, kein Fehlerfall. |

Ruft `callJs("setLanguage", effectiveLanguageId())` auf, wobei `effectiveLanguageId()` bei `language
== null` automatisch auf `CodeLanguageDetector.fromContent(text)` zurückfällt. `void`, keine
Exceptions.

## `public void setLanguageFromFileName(String fileName)`

| Parameter | Typ | null-erlaubt | Verifikation |
|---|---|---|---|
| `fileName` | `String` | **ja** | Wird ungeprüft an `CodeLanguageDetector.fromFileName(fileName)` weitergereicht, welches `null` explizit behandelt (gibt `CodeLanguage.PLAIN` zurück, siehe [CodeLanguageDetector](/api-reference/code-language-detector.md)). Kein eigener Null-Check in `CodeViewer`, aber auch keine Gefahr einer NPE, da der Detector `null` selbst abfängt. |

Delegiert an `setLanguage(...)`. `void`, keine Exceptions.

## `public CodeLanguage getLanguage()`

Keine Parameter. Rückgabewert: `CodeLanguage`, **kann `null` sein** — und zwar genau dann, wenn keine
Sprache explizit über `setLanguage`/`setLanguageFromFileName`/den dreiparametrigen Konstruktor gesetzt
wurde (Default-Zustand) oder `setLanguage(null)` zuletzt aufgerufen wurde. `null` bedeutet semantisch
"Auto-Erkennung aus dem Inhalt aktiv", nicht "kein Wert vorhanden" im fehlerhaften Sinn. Keine
Exceptions.

## `private String effectiveLanguageId()`

Package-interne Helfer-Methode: `CodeLanguage resolved = language != null ? language :
CodeLanguageDetector.fromContent(text);` gibt dann `resolved.cm6Id()` zurück. Keine Parameter.
Rückgabewert: `String`, nie `null` (da `cm6Id()` von `CodeLanguage` nie `null` liefert, siehe
[CodeLanguage](/api-reference/code-language.md)). Keine Exceptions.

## `public void setDark(boolean dark)`

Primitive `boolean`. Setzt Feld, ruft `callJs("setTheme", dark)` auf. `void`, keine Exceptions.

## `public boolean isDark()`

Keine Parameter, `boolean`-Rückgabe. Keine Exceptions.

## `public void setWrap(boolean wrap)`

Primitive `boolean`. Setzt Feld, ruft `callJs("setWrap", wrap)` auf. `void`, keine Exceptions.

## `public boolean isWrap()`

Keine Parameter, `boolean`-Rückgabe. Keine Exceptions.

## `public void setShowLineNumbers(boolean showLineNumbers)`

Primitive `boolean`. Setzt Feld, ruft `callJs("setLineNumbers", showLineNumbers)` auf. `void`, keine
Exceptions.

## `public boolean isShowLineNumbers()`

Keine Parameter, `boolean`-Rückgabe. Keine Exceptions.

## `public void foldAll()`

Keine Parameter. Ruft `callJs("foldAll")` auf (klappt alle faltbaren Blöcke im Editor zu — die
eigentliche Faltlogik liegt clientseitig in CodeMirror). `void`, keine Exceptions im
`CodeViewer`-Rumpf.

## `public void unfoldAll()`

Keine Parameter. Ruft `callJs("unfoldAll")` auf. `void`, keine Exceptions im `CodeViewer`-Rumpf.

## `public void search(String query)` *(implements `MatchNavigable`)*

| Parameter | Typ | null-erlaubt | Verifikation |
|---|---|---|---|
| `query` | `String` | **ja** | Zeile 197: `lastQuery = query == null ? "" : query;` — explizite Normalisierung. |

Ruft `callJs("search", lastQuery, searchCaseSensitive)` auf. Laut Klassen-/Methoden-Javadoc meldet
CodeMirror die Trefferzählung **asynchron** über `onMatchChange(int, int)` zurück — `getMatchCount()`
liefert also nicht sofort nach `search(...)` den aktualisierten Wert, sondern erst nach dem
Client-Roundtrip. `void`, keine Exceptions im `CodeViewer`-Rumpf.

## `public void nextMatch()` *(implements `MatchNavigable`)*

Keine Parameter. Ruft `callJs("move", 1)` auf. `void`, keine Exceptions im `CodeViewer`-Rumpf.

## `public void previousMatch()` *(implements `MatchNavigable`)*

Keine Parameter. Ruft `callJs("move", -1)` auf. `void`, keine Exceptions im `CodeViewer`-Rumpf.

## `public void clearSearch()`

Keine Parameter. Setzt `lastQuery = ""`, ruft `callJs("clearSearch")` auf. `void`, keine Exceptions.

## `public int getMatchCount()` *(implements `MatchNavigable`)*

Keine Parameter. Rückgabewert: gespiegeltes Feld `matchCount` (`int`, initial `0`), **wird vom Client
per `onMatchChange` aktualisiert** — nicht sofort synchron nach `search(...)`. Kein `null` möglich
(primitiver Typ). Keine Exceptions.

## `public int getCurrentMatchIndex()` *(implements `MatchNavigable`)*

Keine Parameter. Rückgabewert: gespiegeltes Feld `currentMatchIndex` (`int`, initial `-1`, laut
Javadoc "oder `-1`" wenn kein Treffer aktiv). Kein `null` möglich. Keine Exceptions.

## `public void setSearchCaseSensitive(boolean caseSensitive)`

Primitive `boolean`. Setzt Feld; **wenn** `lastQuery` nicht leer/blank ist (`!lastQuery.isBlank()`),
wird `search(lastQuery)` erneut ausgeführt, um die aktive Suche mit der neuen Einstellung zu
wiederholen — deckt sich mit dem Javadoc "eine aktive Suche wird neu ausgeführt". `void`, keine
Exceptions.

## `public boolean isSearchCaseSensitive()`

Keine Parameter, `boolean`-Rückgabe. Keine Exceptions.

## `public Registration addMatchChangeListener(ComponentEventListener<MatchChangeEvent> listener)` *(implements `MatchNavigable`)*

| Parameter | Typ | null-erlaubt | Verifikation |
|---|---|---|---|
| `listener` | `ComponentEventListener<MatchChangeEvent>` | im `CodeViewer`-Rumpf nicht geprüft | Direkt an Vaadins `addListener(MatchChangeEvent.class, listener)` weitergereicht (geerbte Framework-Infrastruktur, kein eigener Null-Check). |

Rückgabewert: `Registration`, laut Vaadin-Standardverhalten nie `null`. Keine expliziten
`throw`-Statements im `CodeViewer`-Rumpf.

## `@ClientCallable public void onMatchChange(int count, int index)`

**Vom Client (CodeMirror-Glue) aufgerufener Callback** — laut Javadoc ein "interner Framework-Hook",
nicht zur direkten Verwendung durch Anwendungscode gedacht (`@ClientCallable` macht die Methode über
Vaadins RPC-Mechanismus vom Browser aus aufrufbar).

| Parameter | Typ | Verifikation |
|---|---|---|
| `count` | `int` | Primitiv, keine Validierung im Rumpf — wird ungeprüft in `matchCount` übernommen, auch bei z. B. negativen Werten. |
| `index` | `int` | Primitiv, keine Validierung im Rumpf — wird ungeprüft in `currentMatchIndex` übernommen. |

Setzt die beiden gespiegelten Felder und feuert ein `MatchChangeEvent`. `void`, keine Exceptions.
**Kein Wertebereich-Check:** Anders als z. B. `TextViewer.highlight(int)` prüft diese Methode die
übergebenen Werte nicht auf Plausibilität (z. B. `count >= 0`, `index >= -1`) — sie vertraut dem
Frontend-Glue vollständig. Das ist kein dokumentierter Vertragsbruch (der Javadoc verspricht keine
Validierung), aber erwähnenswert als Vertrauensgrenze zwischen Client und Server.

## `private void fireSearchReset()`

Keine Parameter. Feuert ein `MatchChangeEvent` mit `getMatchCount()`, `getCurrentMatchIndex()` und
zusätzlichem `true`-Flag (Reset-Signalisierung). `void`, keine Exceptions.

## `private void callJs(String function, Serializable... args)`

Zentrale Helfer-Methode für alle Client-Aufrufe.

| Parameter | Typ | null-erlaubt | Verifikation |
|---|---|---|---|
| `function` | `String` | **nein, ungeprüft** | Wird direkt in einen `StringBuilder` eingefügt (`.append(function)`); bei `null` würde `StringBuilder.append(String)` den Text `"null"` einfügen (JDK-Standardverhalten, keine Exception) — das erzeugte JavaScript wäre dann fehlerhaft (`window.MaknoCodeViewer.null(this)`), aber es gibt keine Exception im Java-Code selbst. Praktisch irrelevant, da `callJs` nur intern mit festen String-Literalen aufgerufen wird. |
| `args` | `Serializable...` | Varargs-Array selbst nie `null` bei normalem Aufruf (auch `callJs("foldAll")` erzeugt ein leeres `Serializable[0]`); einzelne `null`-Elemente sind erlaubt, da nur die Array-Länge (`args.length`) zum Bauen des Platzhalter-Strings verwendet wird, nicht die Werte selbst geprüft werden | Die eigentlichen Werte werden unverändert an `getElement().executeJs(js.toString(), args)` übergeben — Vaadins `executeJs` akzeptiert `null`-Argumente (werden im JS zu `null`/`undefined`). |

Ablauf: Bricht früh ab (`return`), wenn `getUI().isEmpty()` (keine gebundene UI, z. B. im Unit-Test
ohne Client) — dann passiert **nichts**, kein Fehler. Sonst wird ein JS-Aufrufstring
`"window.MaknoCodeViewer.<function>(this, $0, $1, ...)"` zusammengebaut und über
`getElement().executeJs(...)` ausgeführt. `void`, keine Exceptions im Java-Rumpf.

# Cross-Referenzen

- [CodeViewer (Narrative)](/components/codeviewer.md)
- [CodeLanguage](/api-reference/code-language.md)
- [CodeLanguageDetector](/api-reference/code-language-detector.md)
- [CodeCssClasses](/api-reference/code-css-classes.md)
- [SearchNavigator und MatchNavigable](/components/search-navigator.md)
- [Frontend-Integration](/architecture/frontend-integration.md)

# Citations

[1] `web-common/src/main/java/de/makno/web/common/component/code/CodeViewer.java`
