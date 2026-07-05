---
type: API Reference
title: SearchController.moveCurrentTo(...)
description: Methode moveCurrentTo von SearchController - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/search/SearchController.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

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

# Citations

[1] [SearchController (Übersicht)](./search-controller.md)
