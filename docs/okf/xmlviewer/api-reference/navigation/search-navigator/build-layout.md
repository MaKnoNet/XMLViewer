---
type: API Reference
title: SearchNavigator.buildLayout(...)
description: Methode buildLayout von SearchNavigator - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/navigation/SearchNavigator.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `private HorizontalLayout buildLayout()`


- Keine Parameter. Baut Suchfeld, Vor-/Zurück-Buttons und Label zu einem `HorizontalLayout`
  zusammen; registriert die internen Klick-/Wertänderungs-Listener, die ihrerseits
  `navigable.search(...)`, `navigable.previousMatch()`, `navigable.nextMatch()` aufrufen.
- Rückgabewert: `HorizontalLayout`, laut Konstruktionslogik nie `null` (immer frisch erzeugt
  und mit vier Kindkomponenten befüllt).
- Exceptions: keine explizit.

# Citations

[1] [SearchNavigator (Übersicht)](./search-navigator.md)
