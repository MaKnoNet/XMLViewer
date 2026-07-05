---
type: API Reference
title: SearchController.setCaseSensitive(...)
description: Methode setCaseSensitive von SearchController - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/search/SearchController.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

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

# Citations

[1] [SearchController (Übersicht)](./search-controller.md)
