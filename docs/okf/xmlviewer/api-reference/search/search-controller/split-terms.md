---
type: API Reference
title: SearchController.splitTerms(...)
description: Methode splitTerms von SearchController - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/search/SearchController.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `splitTerms(String query)` (private) <a id="splitterms"></a>


```java
private List<String> splitTerms(String query)
```

Zerlegt die Eingabe über den (anpassbaren) [SearchTermSplitter](/api-reference/search/search-term-splitter/search-term-splitter.md);
leere Begriffe werden verworfen.

- `query` (`String`) — null-erlaubt: **ja**. Wird direkt an `termSplitter.split(query)`
  weitergereicht — die Nullbehandlung liegt vollständig beim konfigurierten Splitter
  (siehe [SearchTermSplitter](/api-reference/search/search-term-splitter/search-term-splitter.md)).
- Prüft den Rückgabewert von `termSplitter.split(query)` auf `null`: `if (terms == null)
  { return List.of(); }` — schützt den Controller also aktiv vor Fremdimplementierungen,
  die entgegen dem dokumentierten Vertrag `null` statt einer leeren Liste liefern.
  Andernfalls werden `null`- und leere Einträge herausgefiltert
  (`term != null && !term.isEmpty()`).
- **Rückgabewert:** `List<String>`, niemals `null`.
- **Geworfene Exceptions:** keine expliziten; indirekt aus `termSplitter.split(...)`,
  falls die konfigurierte Implementierung wirft.

# Citations

[1] [SearchController (Übersicht)](./search-controller.md)
