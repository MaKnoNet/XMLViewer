---
type: API Reference
title: CodeViewer.onMatchChange(...)
description: Methode onMatchChange von CodeViewer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/code/CodeViewer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `@ClientCallable public void onMatchChange(int count, int index)`


**Vom Client (CodeMirror-Glue) aufgerufener Callback** — laut Javadoc ein "interner Framework-Hook",
nicht zur direkten Verwendung durch Anwendungscode gedacht (`@ClientCallable` macht die Methode über
Vaadins RPC-Mechanismus vom Browser aus aufrufbar).

| Parameter | Typ | Verifikation |
|---|---|---|
| `count` | `int` | Primitiv, keine Validierung im Rumpf — wird ungeprüft in `matchCount` übernommen, auch bei z. B. negativen Werten. |
| `index` | `int` | Primitiv, keine Validierung im Rumpf — wird ungeprüft in `currentMatchIndex` übernommen. |

Setzt die beiden gespiegelten Felder und feuert ein `MatchChangeEvent`. `void`, keine Exceptions.
**Kein Wertebereich-Check:** Anders als z. B. `TextViewer.highlight(int)` prüft diese Methode die
übergebenen Werte nicht auf Plausibilität (z. B. `count >= 0`, `index >= -1`) — sie vertraut dem
Frontend-Glue vollständig. Das ist kein dokumentierter Vertragsbruch (der Javadoc verspricht keine
Validierung), aber erwähnenswert als Vertrauensgrenze zwischen Client und Server.

# Citations

[1] [CodeViewer (Übersicht)](./code-viewer.md)
