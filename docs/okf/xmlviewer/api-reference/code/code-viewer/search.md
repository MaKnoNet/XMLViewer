---
type: API Reference
title: CodeViewer.search(...)
description: Methode search von CodeViewer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/code/CodeViewer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `public void search(String query)` *(implements `MatchNavigable`)*


| Parameter | Typ | null-erlaubt | Verifikation |
|---|---|---|---|
| `query` | `String` | **ja** | Zeile 197: `lastQuery = query == null ? "" : query;` — explizite Normalisierung. |

Ruft `callJs("search", lastQuery, searchCaseSensitive)` auf. Laut Klassen-/Methoden-Javadoc meldet
CodeMirror die Trefferzählung **asynchron** über `onMatchChange(int, int)` zurück — `getMatchCount()`
liefert also nicht sofort nach `search(...)` den aktualisierten Wert, sondern erst nach dem
Client-Roundtrip. `void`, keine Exceptions im `CodeViewer`-Rumpf.

# Citations

[1] [CodeViewer (Übersicht)](./code-viewer.md)
