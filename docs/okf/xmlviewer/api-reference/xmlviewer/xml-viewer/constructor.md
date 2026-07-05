---
type: API Reference
title: XmlViewer – Konstruktoren
description: Alle Konstruktoren von XmlViewer.
resource: web-common/src/main/java/de/makno/web/common/component/xmlviewer/XmlViewer.java
tags: [api-reference, constructor]
timestamp: '2026-07-08T09:00:00+02:00'
---


## `public XmlViewer()`

Keine Parameter. Setzt `getContent().addClassName(CssClasses.ROOT)` und ruft `render()` auf
(rendert den leeren Platzhalter, da `root == null`). Wirft nichts.

## `public XmlViewer(Element root)`

- `root` (`org.jdom2.Element`) — null-erlaubt: ja, verifiziert: Der Konstruktor ruft
  `this()` und dann `setRoot(root)` auf; `setRoot` prüft nicht auf `null` und setzt das Feld
  direkt (`this.root = root; render();`). Wird `null` übergeben, verhält sich die Instanz wie
  nach dem parameterlosen Konstruktor (leerer Platzhalter „Kein XML-Element gesetzt.").

**Was bei ungültiger Eingabe passiert:** Nichts — `null` ist ein gültiger, dokumentierter
Wert für „keine Anzeige".

# Citations

[1] [XmlViewer (Übersicht)](./xml-viewer.md)
