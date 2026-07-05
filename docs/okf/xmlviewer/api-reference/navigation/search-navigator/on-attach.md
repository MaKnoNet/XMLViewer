---
type: API Reference
title: SearchNavigator.onAttach(...)
description: Methode onAttach von SearchNavigator - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/navigation/SearchNavigator.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `protected void onAttach(AttachEvent attachEvent)`


- `attachEvent` (`com.vaadin.flow.component.AttachEvent`) — null-erlaubt: nicht geprüft in
  dieser Methode; wird 1:1 an `super.onAttach(attachEvent)` weitergereicht (Vaadin-Framework-
  Vertrag, nicht Teil dieses Projekts).
- Verhalten: Ruft zuerst `super.onAttach(attachEvent)` auf. Ist `matchChangeRegistration ==
  null` (d.h. der Listener wurde zuvor beim Detach gelöst oder nie registriert), wird er neu
  registriert und die Anzeige mit dem aktuellen Treffer-Stand der `navigable`-Quelle
  synchronisiert. Ist bereits eine Registrierung vorhanden, passiert nichts (Guard
  `if (matchChangeRegistration == null)`).
- Rückgabewert: keiner (`void`, `protected`, überschreibt `Composite.onAttach`).
- Exceptions: keine explizit; abhängig von `super.onAttach` (Vaadin-Framework).

# Citations

[1] [SearchNavigator (Übersicht)](./search-navigator.md)
