---
type: API Reference
title: SearchNavigator.createIconButton(...)
description: Methode createIconButton von SearchNavigator - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/navigation/SearchNavigator.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `private static Button createIconButton(VaadinIcon icon, String tooltip)`


- `icon` (`com.vaadin.flow.component.icon.VaadinIcon`) — null-erlaubt: nicht geprüft in
  dieser Methode; `icon.create()` würde bei `icon == null` eine `NullPointerException`
  werfen (Aufruf einer Instanzmethode auf `null`). Alle tatsächlichen Aufrufer im Code
  übergeben feste, nie-`null`e Konstanten (`VaadinIcon.CHEVRON_LEFT`/`CHEVRON_RIGHT`).
- `tooltip` (`String`) — null-erlaubt: ja (keine Prüfung); `button.setTooltipText(tooltip)`
  toleriert laut Vaadin-Framework-Vertrag `null` (entfernt den Tooltip) — dieses Verhalten
  liegt aber außerhalb dieser Klasse und wird hier nicht weiter verifiziert.
- Rückgabewert: `Button`, nie `null` (frisch konstruiert).
- Exceptions: `NullPointerException` bei `icon == null` (siehe oben), in der Praxis nie
  beobachtet, da nur Konstanten übergeben werden.

# Citations

[1] [SearchNavigator (Übersicht)](./search-navigator.md)
