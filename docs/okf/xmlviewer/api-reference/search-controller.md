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
durchsuchbaren [SearchToken](/api-reference/search-token.md)s einer Komponente ermittelt:
Treffer finden und zählen, zwischen ihnen navigieren, und jede Änderung an die
einbettende Komponente melden. Das **Zeichnen** der Treffer ist ausgelagert an einen
[SearchHighlightRenderer](/api-reference/search-highlight-renderer.md); das
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

# Konstruktoren

## `SearchController(List<SearchToken> tokens, SearchHighlightRenderer highlightRenderer, SerializableRunnable onMatchChange)`

```java
public SearchController(
        List<SearchToken> tokens, SearchHighlightRenderer highlightRenderer, SerializableRunnable onMatchChange) {
    this.tokens = tokens;
    this.highlightRenderer = highlightRenderer;
    this.onMatchChange = onMatchChange;
}
```

- `tokens` (`List<SearchToken>`) — null-erlaubt: **ja, faktisch (keine Prüfung)**. Der
  Konstruktor weist den Parameter ungeprüft direkt dem Feld `this.tokens` zu — kein
  `Objects.requireNonNull`, kein `if`-Check. Wird `null` übergeben, schlägt die erste
  Suche fehl: `collectMatches` iteriert per `tokens.size()`/`tokens.get(index)` über die
  Liste und würde eine `NullPointerException` erst **später**, beim ersten Aufruf von
  `search(...)`, auslösen — nicht sofort im Konstruktor. Dies ist eine **Diskrepanz** zum
  impliziten Vertrag ("gehört zu genau einer Komponente"), da nirgends im Konstruktor
  dokumentiert oder erzwungen wird, dass `tokens` nicht `null` sein darf.
- `highlightRenderer` (`SearchHighlightRenderer`) — null-erlaubt: **ja, faktisch (keine
  Prüfung)**. Ebenfalls ungeprüfte Direktzuweisung. `null` würde erst beim ersten Aufruf
  von `search(...)` (`highlightRenderer.render(...)`) oder `clearSearch()`
  (`highlightRenderer.clear()`) eine `NullPointerException` auslösen — nicht im
  Konstruktor.
- `onMatchChange` (`SerializableRunnable`) — null-erlaubt: **ja, faktisch (keine
  Prüfung)**. Ungeprüfte Direktzuweisung. `null` würde erst bei `notifyChange()`
  (`onMatchChange.run()`) eine `NullPointerException` auslösen, ausgelöst durch jeden
  Aufruf von `search(...)`, `clearSearch()` oder `moveCurrentTo(...)`.
- **Geworfene Exceptions:** keine — der Konstruktorbody besteht ausschließlich aus drei
  Feldzuweisungen ohne jede Prüfung.

Im Gegensatz dazu prüfen die Setter `setTermSplitter(...)` (siehe unten) ihren Parameter
sehr wohl per `Objects.requireNonNull` — die fehlende Prüfung im Konstruktor für die drei
oben genannten Parameter ist also eine bewusste oder unbewusste Inkonsistenz innerhalb
derselben Klasse.

# Methoden

## `getMatchCount()`

```java
public int getMatchCount()
```

- Keine Parameter.
- **Rückgabewert:** `int` — `matches.size()`, also Anzahl aktueller Treffer (`0`, wenn
  keine aktive Suche oder keine Treffer).
- **Geworfene Exceptions:** keine.

## `getCurrentMatchIndex()`

```java
public int getCurrentMatchIndex()
```

- Keine Parameter.
- **Rückgabewert:** `int` — Index des aktuellen Treffers in `matches`, oder `-1`, wenn
  keine Treffer vorhanden sind (Feld-Initialwert `-1`, wird in `search`/`clearSearch` bei
  leeren Treffern ebenfalls auf `-1` gesetzt).
- **Geworfene Exceptions:** keine.

## `setCaseSensitive(boolean caseSensitive)`

```java
public void setCaseSensitive(boolean caseSensitive)
```

- `caseSensitive` (`boolean`) — null-erlaubt: entfällt (primitiver Typ).
- Ist der neue Wert identisch zum aktuellen (`this.caseSensitive == caseSensitive`),
  kehrt die Methode sofort zurück (No-op, keine erneute Suche, kein `notifyChange()`).
  Andernfalls wird das Feld aktualisiert; ist eine Suche aktiv
  ([`hasActiveQuery()`](#hasactivequery) liefert `true`), wird `search(currentQuery)`
  erneut ausgeführt — dadurch werden Treffer, Reveal und Rendering mit der neuen
  Groß-/Kleinschreibungs-Regel neu aufgebaut.
- **Rückgabewert:** `void`.
- **Geworfene Exceptions:** keine expliziten `throw`-Statements; indirekt können
  Exceptions aus einer erneuten `search(...)`-Ausführung durchschlagen (siehe
  [`search`](#search)).

## `setTermSplitter(SearchTermSplitter termSplitter)`

```java
public void setTermSplitter(SearchTermSplitter termSplitter)
```

- `termSplitter` (`SearchTermSplitter`) — null-erlaubt: **nein**. Verifiziert durch
  `this.termSplitter = Objects.requireNonNull(termSplitter, "termSplitter");` — wirft
  `NullPointerException` mit Nachricht `"termSplitter"` bei `null`.
- Ist eine Suche aktiv (`hasActiveQuery()`), wird `search(currentQuery)` mit dem neuen
  Splitter erneut ausgeführt.
- **Rückgabewert:** `void`.
- **Geworfene Exceptions:** `NullPointerException`, wenn `termSplitter == null` (vor
  jeder anderen Wirkung geprüft — die Zuweisung schlägt sofort fehl, `search` wird in
  diesem Fall nicht mehr erreicht).

## `search(String query)` <a id="search"></a>

```java
public void search(String query)
```

Sucht alle Treffer von `query`, macht deren Tokens sichtbar, lässt sie zeichnen und
springt zum ersten Treffer. Mehrere durch Whitespace (bzw. den konfigurierten
[SearchTermSplitter](/api-reference/search-term-splitter.md)) getrennte Begriffe werden
einzeln gesucht (ODER-Verknüpfung, verifiziert: `collectMatches` sammelt Treffer über
alle `terms` gemeinsam in `findMatchRanges`).

- `query` (`String`) — null-erlaubt: **ja**. Wird ungeprüft `currentQuery` zugewiesen;
  die eigentliche Nullbehandlung passiert in [`splitTerms(query)`](#splitterms), das an
  `termSplitter.split(query)` delegiert. Für die Standardimplementierung
  [`DEFAULT_TERM_SPLITTER`](#default_term_splitter) liefert `query == null` eine leere
  Liste, wodurch `matches` auf `List.of()` und `currentMatchIndex` auf `-1` gesetzt wird
  — effektiv identisch zu `clearSearch()`. Bei einem benutzerdefinierten
  `SearchTermSplitter`, der `null` nicht abfängt, könnte hier eine
  `NullPointerException` aus fremdem Code entstehen — nicht aus `SearchController`
  selbst.
- Ablauf im Methodenbody: `currentQuery = query` → Begriffe über
  [`splitTerms`](#splitterms) ermitteln → bei leeren Begriffen `matches = List.of()`,
  sonst `matches = collectMatches(terms)` → `currentMatchIndex` auf `0` (falls Treffer)
  oder `-1` (falls keine) → [`revealMatches()`](#revealmatches) →
  `highlightRenderer.render(matches, currentMatchIndex)` → `notifyChange()`.
- **Rückgabewert:** `void`.
- **Geworfene Exceptions:** keine expliziten `throw`-Statements in `search` selbst.
  Indirekt möglich: `NullPointerException` aus `tokens.get(index).text()` in
  `collectMatches`, falls die `tokens`-Liste (siehe Konstruktor) `null` ist oder
  einzelne Einträge `null` sind (Records im Konstruktor prüfen `text`/`onReveal` selbst
  — ein `null`-Eintrag *in der Liste* ist aber möglich, wenn der Aufrufer eine Liste mit
  `null`-Elementen übergibt).

## `nextMatch()`

```java
public void nextMatch()
```

- Keine Parameter.
- Springt umlaufend (Modulo `matches.size()`) zum nächsten Treffer. Ist `matches` leer,
  passiert nichts (No-op, verifiziert durch `if (!matches.isEmpty())`-Guard).
- **Rückgabewert:** `void`.
- **Geworfene Exceptions:** keine expliziten; kein Divide-by-zero möglich, da der
  Modulo-Ausdruck nur bei nicht-leerem `matches` ausgeführt wird.

## `previousMatch()`

```java
public void previousMatch()
```

- Keine Parameter.
- Springt umlaufend zum vorherigen Treffer (`(currentMatchIndex - 1 + matches.size()) %
  matches.size()`, sodass auch bei `currentMatchIndex == 0` korrekt zum letzten Treffer
  gesprungen wird). Ist `matches` leer, No-op.
- **Rückgabewert:** `void`.
- **Geworfene Exceptions:** keine expliziten.

## `clearSearch()`

```java
public void clearSearch()
```

- Keine Parameter.
- Setzt `currentQuery = null`, `matches = List.of()`, `currentMatchIndex = -1`, ruft
  `highlightRenderer.clear()` und `notifyChange()` auf. Entfernt damit alle
  Such-Markierungen unbedingt (kein Guard wie bei `setCaseSensitive`).
- **Rückgabewert:** `void`.
- **Geworfene Exceptions:** keine expliziten; indirekt aus `highlightRenderer.clear()`
  oder `onMatchChange.run()`, falls diese Fremdimplementierungen werfen.

## `collectMatches(List<String> terms)` (private)

```java
private List<TokenMatch> collectMatches(List<String> terms)
```

Sammelt alle Treffer aller Begriffe in Dokumentreihenfolge (Token-Index, dann Offset).

- `terms` (`List<String>`) — null-erlaubt: **nein, faktisch nicht erreichbar mit
  `null`**. Einziger Aufrufer ist `search(...)`, das immer das (bereits gefilterte,
  nicht-null) Ergebnis von `splitTerms(query)` übergibt und den `null`/leer-Fall vorher
  über `terms.isEmpty()` abfängt, bevor `collectMatches` überhaupt aufgerufen wird.
- Iteriert über alle `tokens` (per Index) und ruft je Token
  [`findMatchRanges(text, terms)`](#findmatchranges) auf; erzeugt für jeden gefundenen
  Bereich ein neues [`TokenMatch`](/api-reference/token-match.md).
- **Rückgabewert:** `List<TokenMatch>`, niemals `null` (mindestens leere `ArrayList`).
- **Geworfene Exceptions:** keine expliziten; `NullPointerException` möglich über
  `tokens.get(index).text()`, falls `tokens` `null` ist (siehe Konstruktor-Diskrepanz)
  oder ein Listenelement `null` ist.

## `revealMatches()` (private) <a id="revealmatches"></a>

```java
private void revealMatches()
```

Führt die Reveal-Aktion jedes Tokens aus, das mindestens einen Treffer enthält. Tokens,
die sich dieselbe `SearchToken#onReveal()`-Instanz teilen (z.&nbsp;B. mehrere Tokens
desselben XML-Elements), werden über **Identität** (`IdentityHashMap`-basiertes Set)
dedupliziert — verifiziert im Code durch
`Collections.newSetFromMap(new IdentityHashMap<>())` — sodass ein Element nur einmal
aufklappt, auch wenn mehrere seiner Tokens Treffer enthalten.

- Keine Parameter.
- **Rückgabewert:** `void`.
- **Geworfene Exceptions:** keine expliziten; indirekt aus `Runnable::run`, falls ein
  `onReveal`-Callback wirft.

## `moveCurrentTo(int newIndex)` (private)

```java
private void moveCurrentTo(int newIndex)
```

- `newIndex` (`int`) — null-erlaubt: entfällt (primitiver Typ). Kein Bereichs-Check im
  Methodenbody selbst; die beiden Aufrufer (`nextMatch`, `previousMatch`) garantieren
  bereits einen gültigen Index über Modulo-Arithmetik unter der Bedingung, dass
  `matches` nicht leer ist.
- Setzt `currentMatchIndex = newIndex`, ruft [`revealOf(matches.get(newIndex))`](#revealof)
  auf (macht den neuen aktuellen Treffer sichtbar, auch bei zugeklappten Bereichen),
  dann `highlightRenderer.moveCurrent(currentMatchIndex)` und `notifyChange()`.
- **Rückgabewert:** `void`.
- **Geworfene Exceptions:** `IndexOutOfBoundsException` aus `matches.get(newIndex)`, falls
  `newIndex` außerhalb der Listengrenzen liegt (kann bei den aktuellen Aufrufern nicht
  auftreten, ist aber nicht durch einen expliziten Guard in dieser Methode selbst
  ausgeschlossen).

## `revealOf(TokenMatch match)` (private) <a id="revealof"></a>

```java
private void revealOf(TokenMatch match)
```

Führt die Reveal-Aktion des Tokens aus, das den Treffer enthält.

- `match` (`TokenMatch`) — null-erlaubt: **nein, faktisch nicht geprüft**. Kein
  expliziter Null-Check; `match.tokenIndex()` würde bei `match == null` eine
  `NullPointerException` auslösen. Einziger Aufrufer ist `moveCurrentTo`, das stets ein
  nicht-null Element aus `matches.get(newIndex)` übergibt.
- **Rückgabewert:** `void`.
- **Geworfene Exceptions:** implizite `NullPointerException` bei `match == null` (siehe
  oben); `IndexOutOfBoundsException` aus `tokens.get(match.tokenIndex())`, falls
  `tokenIndex()` außerhalb der Grenzen von `tokens` liegt.

## `splitTerms(String query)` (private) <a id="splitterms"></a>

```java
private List<String> splitTerms(String query)
```

Zerlegt die Eingabe über den (anpassbaren) [SearchTermSplitter](/api-reference/search-term-splitter.md);
leere Begriffe werden verworfen.

- `query` (`String`) — null-erlaubt: **ja**. Wird direkt an `termSplitter.split(query)`
  weitergereicht — die Nullbehandlung liegt vollständig beim konfigurierten Splitter
  (siehe [SearchTermSplitter](/api-reference/search-term-splitter.md)).
- Prüft den Rückgabewert von `termSplitter.split(query)` auf `null`: `if (terms == null)
  { return List.of(); }` — schützt den Controller also aktiv vor Fremdimplementierungen,
  die entgegen dem dokumentierten Vertrag `null` statt einer leeren Liste liefern.
  Andernfalls werden `null`- und leere Einträge herausgefiltert
  (`term != null && !term.isEmpty()`).
- **Rückgabewert:** `List<String>`, niemals `null`.
- **Geworfene Exceptions:** keine expliziten; indirekt aus `termSplitter.split(...)`,
  falls die konfigurierte Implementierung wirft.

## `findMatchRanges(String text, List<String> terms)` (private) <a id="findmatchranges"></a>

```java
private List<int[]> findMatchRanges(String text, List<String> terms)
```

Findet alle Treffer-Intervalle aller Begriffe im Text, sortiert nach Start und mit
verschmolzenen Überlappungen (z.&nbsp;B. „EUR" und „EU"), sodass sich Bereiche nicht
überlagern. Der Abgleich erfolgt zeichenweise direkt auf dem unveränderten Originaltext
via `String#regionMatches(boolean, int, String, int, int)` (Ignorier-Groß-/Kleinschreibung
gesteuert über `!caseSensitive`) — dadurch bleiben Offsets auch bei
Lowercase-Längenänderungen (z.&nbsp;B. türkisches `İ`) korrekt im Originaltext verankert.

- `text` (`String`) — null-erlaubt: **nein, faktisch nicht geprüft**. Kein expliziter
  Null-Check; `text.length()` würde bei `text == null` eine `NullPointerException`
  auslösen. Einziger Aufrufer ist `collectMatches`, das `tokens.get(index).text()`
  übergibt — laut [SearchToken](/api-reference/search-token.md)-Konstruktor kann `text`
  dort nie `null` sein (kompakter Konstruktor prüft das), solange das Token selbst nicht
  `null` ist.
- `terms` (`List<String>`) — null-erlaubt: **nein, faktisch nicht erreichbar mit
  `null`**, aus denselben Gründen wie bei `collectMatches`. Leere Einträge (Länge 0)
  werden explizit übersprungen (`if (termLength == 0) { continue; }`).
- **Rückgabewert:** `List<int[]>`, niemals `null` (mindestens leere `ArrayList`); jedes
  `int[]`-Element hat genau zwei Einträge `{start, end}`.
- **Geworfene Exceptions:** implizite `NullPointerException` bei `text == null` (siehe
  oben); keine weiteren expliziten `throw`-Statements.

## `mergeOverlaps(List<int[]> sortedRanges)` (private static)

```java
private static List<int[]> mergeOverlaps(List<int[]> sortedRanges)
```

Verschmilzt überlappende/anschließende Intervalle einer nach Start sortierten Liste.

- `sortedRanges` (`List<int[]>`) — null-erlaubt: **nein, faktisch nicht geprüft**. Kein
  expliziter Null-Check; eine erweiterte `for`-Schleife über `sortedRanges` würde bei
  `null` eine `NullPointerException` auslösen. Einziger Aufrufer ist
  `findMatchRanges`, das stets eine (ggf. leere) nicht-null `ArrayList` übergibt.
- Erwartet implizit, dass `sortedRanges` bereits nach Start-Offset sortiert ist (wird von
  `findMatchRanges` vor dem Aufruf per `ranges.sort(...)` sichergestellt) — diese Methode
  selbst prüft das nicht, sondern verlässt sich auf den Aufrufer-Vertrag.
- **Rückgabewert:** `List<int[]>`, niemals `null` (mindestens leere `ArrayList`).
- **Geworfene Exceptions:** implizite `NullPointerException` bei `sortedRanges == null`
  (siehe oben).

## `hasActiveQuery()` (private) <a id="hasactivequery"></a>

```java
private boolean hasActiveQuery()
```

- Keine Parameter.
- **Rückgabewert:** `boolean` — `true`, wenn `currentQuery != null && !currentQuery.isBlank()`.
- **Geworfene Exceptions:** keine.

## `notifyChange()` (private)

```java
private void notifyChange()
```

- Keine Parameter.
- Ruft `onMatchChange.run()` auf.
- **Rückgabewert:** `void`.
- **Geworfene Exceptions:** implizite `NullPointerException`, falls `onMatchChange ==
  null` (siehe Konstruktor-Diskrepanz oben — dieses Feld wird nie auf `null` geprüft und
  kann über den Konstruktor `null` werden). Ansonsten beliebige Exceptions aus dem
  aufrufenden `Runnable`, falls dieser wirft.

# Statische Konstante

## `DEFAULT_TERM_SPLITTER` <a id="default_term_splitter"></a>

```java
public static final SearchTermSplitter DEFAULT_TERM_SPLITTER = query -> {
    if (query == null || query.isBlank()) {
        return List.of();
    }
    return Arrays.stream(query.trim().split("\\s+"))
            .filter(term -> !term.isEmpty())
            .toList();
};
```

Standard-[SearchTermSplitter](/api-reference/search-term-splitter.md): trennt an
Whitespace, verwirft leere Begriffe.

- Nimmt `query` entgegen (`String`) — behandelt `null` explizit (`query == null`) und
  liefert dann `List.of()`, statt zu werfen. Leere/nur-Whitespace-Strings
  (`query.isBlank()`) werden ebenso behandelt.
- **Rückgabewert:** `List<String>`, niemals `null` — mindestens `List.of()`.
- **Geworfene Exceptions:** keine; die Lambda selbst enthält keine `throw`-Statements
  und verwendet nur Standardbibliotheksmethoden (`trim`, `split`, `stream`, `filter`,
  `toList`), die auf einem garantiert nicht-`null` `query` (nach dem Guard) nicht werfen.

# Citations

[1] web-common/src/main/java/de/makno/web/common/component/search/SearchController.java
