---
type: API Reference
title: SearchController.search(...)
description: Methode search von SearchController - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/search/SearchController.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `search(String query)` <a id="search"></a>


```java
public void search(String query)
```

Sucht alle Treffer von `query`, macht deren Tokens sichtbar, lässt sie zeichnen und
springt zum ersten Treffer. Mehrere durch Whitespace (bzw. den konfigurierten
[SearchTermSplitter](/api-reference/search/search-term-splitter/search-term-splitter.md)) getrennte Begriffe werden
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

# Citations

[1] [SearchController (Übersicht)](./search-controller.md)
