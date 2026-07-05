---
type: API Reference
title: SearchController.collectMatches(...)
description: Methode collectMatches von SearchController - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/search/SearchController.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

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
  Bereich ein neues [`TokenMatch`](/api-reference/search/token-match/token-match.md).
- **Rückgabewert:** `List<TokenMatch>`, niemals `null` (mindestens leere `ArrayList`).
- **Geworfene Exceptions:** keine expliziten; `NullPointerException` möglich über
  `tokens.get(index).text()`, falls `tokens` `null` ist (siehe Konstruktor-Diskrepanz)
  oder ein Listenelement `null` ist.

# Citations

[1] [SearchController (Übersicht)](./search-controller.md)
