---
type: API Reference
title: SearchNavigator.registerMatchChangeListener(...)
description: Methode registerMatchChangeListener von SearchNavigator - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/navigation/SearchNavigator.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `private void registerMatchChangeListener()`


- Keine Parameter. Registriert `this::onMatchChange` als Listener bei
  `navigable.addMatchChangeListener(...)` und speichert die zurückgegebene `Registration`.
- Rückgabewert: keiner. Exceptions: keine explizit; abhängig vom Vertrag von
  `MatchNavigable.addMatchChangeListener` (siehe [MatchNavigable](/api-reference/navigation/match-navigable/match-navigable.md)).
- Hinweis: package-private wäre `private`, tatsächlich ist die Methode `private` (keine
  Paket-Sichtbarkeit) — der Vollständigkeit halber dennoch dokumentiert, da sie zentrales
  Verhalten steuert.

# Citations

[1] [SearchNavigator (Übersicht)](./search-navigator.md)
