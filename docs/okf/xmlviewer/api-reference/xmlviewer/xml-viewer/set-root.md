---
type: API Reference
title: XmlViewer.setRoot(...)
description: Methode setRoot von XmlViewer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/xmlviewer/XmlViewer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `public void setRoot(Element root)`


- `root` — null-erlaubt: ja, verifiziert (`this.root = root;` ohne Prüfung). `null` leert die
  Ansicht (zeigt den Platzhalter „Kein XML-Element gesetzt.").
- Rückgabewert: keiner (`void`).
- Exceptions: keine im Methodenkörper; ruft intern `render()` auf, das seinerseits keine
  geprüften Exceptions deklariert.

# Citations

[1] [XmlViewer (Übersicht)](./xml-viewer.md)
