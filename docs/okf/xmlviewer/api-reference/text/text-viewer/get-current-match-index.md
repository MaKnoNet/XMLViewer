---
type: API Reference
title: TextViewer.getCurrentMatchIndex(...)
description: Methode getCurrentMatchIndex von TextViewer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/text/TextViewer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `public int getCurrentMatchIndex()` *(implements `MatchNavigable`)*


Keine Parameter. Delegiert an `searchController.getCurrentMatchIndex()`. Laut Javadoc „0-basierter
Index des aktuellen Treffers, oder `-1`, wenn keiner aktiv ist" — dieser Vertrag wird von
`SearchController` erfüllt, nicht in `TextViewer` selbst geprüft. `int`-Rückgabe, keine Exceptions.

# Citations

[1] [TextViewer (Übersicht)](./text-viewer.md)
