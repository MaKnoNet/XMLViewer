---
type: API Reference
title: SearchNavigator.onDetach(...)
description: Methode onDetach von SearchNavigator - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/navigation/SearchNavigator.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `protected void onDetach(DetachEvent detachEvent)`


- `detachEvent` (`com.vaadin.flow.component.DetachEvent`) — null-erlaubt: nicht geprüft;
  wird 1:1 an `super.onDetach(detachEvent)` weitergereicht.
- Verhalten: Ist `matchChangeRegistration != null`, wird sie gelöst
  (`matchChangeRegistration.remove()`) und das Feld auf `null` gesetzt — **verifiziert
  deckungsgleich mit der Javadoc-Behauptung**, dass dies ein Session-Memory-Leak verhindert
  (die langlebige `navigable`-Quelle würde sonst eine Referenz auf die detachte Leiste über
  den registrierten Listener behalten). Danach `super.onDetach(detachEvent)`.
- Rückgabewert: keiner. Exceptions: keine explizit.

# Citations

[1] [SearchNavigator (Übersicht)](./search-navigator.md)
