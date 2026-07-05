---
type: API Reference
title: CodeViewer.setSearchCaseSensitive(...)
description: Methode setSearchCaseSensitive von CodeViewer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/code/CodeViewer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `public void setSearchCaseSensitive(boolean caseSensitive)`


Primitive `boolean`. Setzt Feld; **wenn** `lastQuery` nicht leer/blank ist (`!lastQuery.isBlank()`),
wird `search(lastQuery)` erneut ausgeführt, um die aktive Suche mit der neuen Einstellung zu
wiederholen — deckt sich mit dem Javadoc "eine aktive Suche wird neu ausgeführt". `void`, keine
Exceptions.

# Citations

[1] [CodeViewer (Übersicht)](./code-viewer.md)
