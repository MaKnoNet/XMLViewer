---
type: API Reference
title: SearchController.setTermSplitter(...)
description: Methode setTermSplitter von SearchController - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/search/SearchController.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

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

# Citations

[1] [SearchController (Übersicht)](./search-controller.md)
