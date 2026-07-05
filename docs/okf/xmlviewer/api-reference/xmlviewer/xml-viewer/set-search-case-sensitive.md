---
type: API Reference
title: XmlViewer.setSearchCaseSensitive(...)
description: Methode setSearchCaseSensitive von XmlViewer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/xmlviewer/XmlViewer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `public void setSearchCaseSensitive(boolean caseSensitive)`


- `caseSensitive` (`boolean`). Setzt das Feld und delegiert an
  `searchController.setCaseSensitive(caseSensitive)`, was bei geänderter Einstellung und
  aktiver Suche (`hasActiveQuery()`) die Suche automatisch neu ausführt.
- Rückgabewert: keiner. Exceptions: keine.

# Citations

[1] [XmlViewer (Übersicht)](./xml-viewer.md)
