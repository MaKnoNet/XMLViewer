---
type: API Reference
title: XmlViewer.addMatchChangeListener(...)
description: Methode addMatchChangeListener von XmlViewer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/xmlviewer/XmlViewer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `@Override public Registration addMatchChangeListener(ComponentEventListener<MatchChangeEvent> listener)`


- `listener` — null-erlaubt: nicht direkt geprüft in `XmlViewer` selbst; delegiert an
  Vaadins `Composite.addListener(MatchChangeEvent.class, listener)`. Das Verhalten bei
  `null` liegt außerhalb dieser Klasse (Vaadin-Flow-internes `ComponentEventBus`, i.d.R.
  wirft es dort eine `NullPointerException`, aber das ist nicht durch `XmlViewer`
  verifizierbar/dokumentierbar, da der Aufruf reine Weiterleitung ist).
- Rückgabewert: `Registration`, laut Vaadin-Framework-Vertrag nie `null` — dient dem
  späteren Lösen der Registrierung (`registration.remove()`).
- Exceptions: keine explizit in `XmlViewer`; siehe oben zur Weiterleitung.

# Citations

[1] [XmlViewer (Übersicht)](./xml-viewer.md)
