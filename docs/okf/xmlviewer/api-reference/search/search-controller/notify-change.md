---
type: API Reference
title: SearchController.notifyChange(...)
description: Methode notifyChange von SearchController - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/search/SearchController.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

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

# Citations

[1] [SearchController (Übersicht)](./search-controller.md)
