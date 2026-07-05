---
type: API Reference
title: SearchHighlightRenderer.moveCurrent(...)
description: Methode moveCurrent von SearchHighlightRenderer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/search/SearchHighlightRenderer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `moveCurrent(int currentIndex)`


```java
void moveCurrent(int currentIndex);
```

- `currentIndex` (`int`) — null-erlaubt: entfällt (primitiver Typ). Verschiebt nur die
  Hervorhebung des aktuellen Treffers, ohne die Treffermenge neu zu übertragen (laut
  Javadoc; das Interface selbst kann das nicht erzwingen, es ist Vertrag für
  Implementierungen).
- **Rückgabewert:** `void`.
- **Geworfene Exceptions:** vom Interface keine deklariert.

# Citations

[1] [SearchHighlightRenderer (Übersicht)](./search-highlight-renderer.md)
