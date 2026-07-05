---
type: API Reference
title: SearchController.clearSearch(...)
description: Methode clearSearch von SearchController - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/search/SearchController.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

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

# Citations

[1] [SearchController (Übersicht)](./search-controller.md)
