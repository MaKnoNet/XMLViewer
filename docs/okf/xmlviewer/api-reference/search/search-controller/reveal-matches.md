---
type: API Reference
title: SearchController.revealMatches(...)
description: Methode revealMatches von SearchController - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/search/SearchController.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `revealMatches()` (private) <a id="revealmatches"></a>


```java
private void revealMatches()
```

Führt die Reveal-Aktion jedes Tokens aus, das mindestens einen Treffer enthält. Tokens,
die sich dieselbe `SearchToken#onReveal()`-Instanz teilen (z.&nbsp;B. mehrere Tokens
desselben XML-Elements), werden über **Identität** (`IdentityHashMap`-basiertes Set)
dedupliziert — verifiziert im Code durch
`Collections.newSetFromMap(new IdentityHashMap<>())` — sodass ein Element nur einmal
aufklappt, auch wenn mehrere seiner Tokens Treffer enthalten.

- Keine Parameter.
- **Rückgabewert:** `void`.
- **Geworfene Exceptions:** keine expliziten; indirekt aus `Runnable::run`, falls ein
  `onReveal`-Callback wirft.

# Citations

[1] [SearchController (Übersicht)](./search-controller.md)
