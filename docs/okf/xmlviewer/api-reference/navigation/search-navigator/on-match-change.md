---
type: API Reference
title: SearchNavigator.onMatchChange(...)
description: Methode onMatchChange von SearchNavigator - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/navigation/SearchNavigator.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `private void onMatchChange(MatchChangeEvent event)`


- `event` (`de.makno.web.common.component.navigation.MatchChangeEvent`) — null-erlaubt:
  nicht geprüft in dieser Methode; wird nur intern von Vaadins Event-Bus als Listener-Callback
  aufgerufen (`event.isReset()`, `event.getMatchCount()`, `event.getCurrentMatchIndex()` — bei
  `event == null` würde eine `NullPointerException` bei `event.isReset()` auftreten, aber
  dieser Fall ist praktisch ausgeschlossen, da Vaadin niemals `null`-Events an Listener
  liefert).
- Verhalten: Ist `event.isReset()` wahr, wird das Suchfeld geleert (`searchField.clear()`) —
  laut Kommentar löst das ein leeres `search("")`-Ereignis in der Quelle aus, das über ein
  Folge-Event Zähler/Buttons zusätzlich zurücksetzt. Danach wird immer `update(...)` mit dem
  aktuellen Treffer-Stand aus dem Event aufgerufen.
- Rückgabewert: keiner. Exceptions: keine explizit (siehe `event == null`-Hinweis oben).

# Citations

[1] [SearchNavigator (Übersicht)](./search-navigator.md)
