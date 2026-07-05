---
type: API Reference
title: XmlViewer.setCollapsible(...)
description: Methode setCollapsible von XmlViewer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/xmlviewer/XmlViewer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `public void setCollapsible(boolean collapsible)`


- `collapsible` (`boolean`, kein Null-Fall möglich, primitiver Typ).
- Setzt das Feld und rendert **komplett neu** (`render()` — der gesamte Baum wird
  weggeworfen und neu aufgebaut; alle Highlight-/Klapp-Zustände gehen verloren, da `render()`
  am Anfang `clearHighlight()` aufruft und den Content leert).
- Rückgabewert: keiner.
- Exceptions: keine.

# Citations

[1] [XmlViewer (Übersicht)](./xml-viewer.md)
