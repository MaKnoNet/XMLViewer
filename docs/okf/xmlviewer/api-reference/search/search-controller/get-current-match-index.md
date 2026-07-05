---
type: API Reference
title: SearchController.getCurrentMatchIndex(...)
description: Methode getCurrentMatchIndex von SearchController - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/search/SearchController.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `getCurrentMatchIndex()`


```java
public int getCurrentMatchIndex()
```

- Keine Parameter.
- **Rückgabewert:** `int` — Index des aktuellen Treffers in `matches`, oder `-1`, wenn
  keine Treffer vorhanden sind (Feld-Initialwert `-1`, wird in `search`/`clearSearch` bei
  leeren Treffern ebenfalls auf `-1` gesetzt).
- **Geworfene Exceptions:** keine.

# Citations

[1] [SearchController (Übersicht)](./search-controller.md)
