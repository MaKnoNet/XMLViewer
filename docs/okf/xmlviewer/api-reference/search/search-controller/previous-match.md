---
type: API Reference
title: SearchController.previousMatch(...)
description: Methode previousMatch von SearchController - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/search/SearchController.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `previousMatch()`


```java
public void previousMatch()
```

- Keine Parameter.
- Springt umlaufend zum vorherigen Treffer (`(currentMatchIndex - 1 + matches.size()) %
  matches.size()`, sodass auch bei `currentMatchIndex == 0` korrekt zum letzten Treffer
  gesprungen wird). Ist `matches` leer, No-op.
- **Rückgabewert:** `void`.
- **Geworfene Exceptions:** keine expliziten.

# Citations

[1] [SearchController (Übersicht)](./search-controller.md)
