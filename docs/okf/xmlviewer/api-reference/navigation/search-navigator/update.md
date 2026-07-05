---
type: API Reference
title: SearchNavigator.update(...)
description: Methode update von SearchNavigator - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/navigation/SearchNavigator.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `private void update(int matchCount, int currentMatchIndex)`


- `matchCount` (`int`), `currentMatchIndex` (`int`) — primitive Typen, kein Null-Fall,
  keine Wertebereichsprüfung.
- Verhalten: berechnet `currentPosition` (1-basiert, `0` wenn `matchCount <= 0`), setzt den
  Label-Text über `labelFormatter.format(matchCount, currentPosition)` und schaltet die
  Buttons aktiv/inaktiv (`setButtonsEnabled(matchCount > 0)`).
- Rückgabewert: keiner.
- Exceptions: keine explizit in dieser Methode; hängt vom aktiven `labelFormatter` ab (ein
  eigener, per `setLabelFormatter` gesetzter Formatter könnte theoretisch selbst eine
  Exception werfen, das liegt aber außerhalb der Kontrolle von `SearchNavigator`).

# Citations

[1] [SearchNavigator (Übersicht)](./search-navigator.md)
