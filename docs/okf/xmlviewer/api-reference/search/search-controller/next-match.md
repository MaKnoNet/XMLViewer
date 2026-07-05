---
type: API Reference
title: SearchController.nextMatch(...)
description: Methode nextMatch von SearchController - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/search/SearchController.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `nextMatch()`


```java
public void nextMatch()
```

- Keine Parameter.
- Springt umlaufend (Modulo `matches.size()`) zum nächsten Treffer. Ist `matches` leer,
  passiert nichts (No-op, verifiziert durch `if (!matches.isEmpty())`-Guard).
- **Rückgabewert:** `void`.
- **Geworfene Exceptions:** keine expliziten; kein Divide-by-zero möglich, da der
  Modulo-Ausdruck nur bei nicht-leerem `matches` ausgeführt wird.

# Citations

[1] [SearchController (Übersicht)](./search-controller.md)
