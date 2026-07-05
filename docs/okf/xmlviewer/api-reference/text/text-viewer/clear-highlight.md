---
type: API Reference
title: TextViewer.clearHighlight(...)
description: Methode clearHighlight von TextViewer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/text/TextViewer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `public void clearHighlight()`


Keine Parameter. Entfernt `TextCssClasses.HIGHLIGHT` von allen in `highlightedLines` vermerkten
Zeilen-Elementen und leert die Menge. `void`-Rückgabe, keine Exceptions. Wird auch intern am Anfang
von `render()` aufgerufen, um Highlights eines alten Renderings zu verwerfen.

# Citations

[1] [TextViewer (Übersicht)](./text-viewer.md)
