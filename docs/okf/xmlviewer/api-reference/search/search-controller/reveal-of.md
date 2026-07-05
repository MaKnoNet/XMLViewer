---
type: API Reference
title: SearchController.revealOf(...)
description: Methode revealOf von SearchController - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/search/SearchController.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `revealOf(TokenMatch match)` (private) <a id="revealof"></a>


```java
private void revealOf(TokenMatch match)
```

Führt die Reveal-Aktion des Tokens aus, das den Treffer enthält.

- `match` (`TokenMatch`) — null-erlaubt: **nein, faktisch nicht geprüft**. Kein
  expliziter Null-Check; `match.tokenIndex()` würde bei `match == null` eine
  `NullPointerException` auslösen. Einziger Aufrufer ist `moveCurrentTo`, das stets ein
  nicht-null Element aus `matches.get(newIndex)` übergibt.
- **Rückgabewert:** `void`.
- **Geworfene Exceptions:** implizite `NullPointerException` bei `match == null` (siehe
  oben); `IndexOutOfBoundsException` aus `tokens.get(match.tokenIndex())`, falls
  `tokenIndex()` außerhalb der Grenzen von `tokens` liegt.

# Citations

[1] [SearchController (Übersicht)](./search-controller.md)
