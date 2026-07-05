---
type: API Reference
title: XmlViewer.nextMatch(...)
description: Methode nextMatch von XmlViewer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/xmlviewer/XmlViewer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `@Override public void nextMatch()`


- Keine Parameter. Delegiert an `searchController.nextMatch()`. Rückgabewert: keiner.
- Exceptions: keine (No-Op, wenn keine Treffer vorhanden — Guard `if (!matches.isEmpty())`
  im `SearchController`).

# Citations

[1] [XmlViewer (Übersicht)](./xml-viewer.md)
