---
type: API Reference
title: CodeViewer.getMatchCount(...)
description: Methode getMatchCount von CodeViewer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/code/CodeViewer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `public int getMatchCount()` *(implements `MatchNavigable`)*


Keine Parameter. Rückgabewert: gespiegeltes Feld `matchCount` (`int`, initial `0`), **wird vom Client
per `onMatchChange` aktualisiert** — nicht sofort synchron nach `search(...)`. Kein `null` möglich
(primitiver Typ). Keine Exceptions.

# Citations

[1] [CodeViewer (Übersicht)](./code-viewer.md)
