---
type: API Reference
title: XmlViewer.getCurrentMatchIndex(...)
description: Methode getCurrentMatchIndex von XmlViewer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/xmlviewer/XmlViewer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `@Override public int getCurrentMatchIndex()`


- Keine Parameter. Rückgabewert: `int`, 0-basierter Index des aktuellen Treffers, oder `-1`,
  wenn keiner aktiv ist (verifiziert: `SearchController` initialisiert `currentMatchIndex = -1`
  und setzt ihn nur bei vorhandenen Treffern auf `>= 0`).
- Exceptions: keine.

# Citations

[1] [XmlViewer (Übersicht)](./xml-viewer.md)
