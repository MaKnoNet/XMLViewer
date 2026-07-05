---
type: API Reference
title: XmlViewer.clearHighlight(...)
description: Methode clearHighlight von XmlViewer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/xmlviewer/XmlViewer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `public void clearHighlight(Element element)`


- `element` — null-erlaubt: ja, verifiziert durch `if (element == null) { return; }` als
  erste Zeile.
- Unbekannte (nicht im Baum enthaltene) Elemente: `header` ist dann `null`, die
  `removeClassName`-Anweisung wird übersprungen (`if (header != null) {...}`), aber
  `highlightedElements.remove(element)` wird trotzdem ausgeführt (No-Op, falls nicht enthalten).
- Rückgabewert: keiner.
- Exceptions: keine.

## `public void clearHighlight()`


- Keine Parameter.
- Entfernt die Hervorhebung aller aktuell hervorgehobenen Elemente und leert
  `highlightedElements`.
- Rückgabewert: keiner.
- Exceptions: keine.

# Citations

[1] [XmlViewer (Übersicht)](./xml-viewer.md)
