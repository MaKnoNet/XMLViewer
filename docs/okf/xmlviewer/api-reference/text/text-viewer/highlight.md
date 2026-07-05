---
type: API Reference
title: TextViewer.highlight(...)
description: Methode highlight von TextViewer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/text/TextViewer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `public void highlight(int line)`


| Parameter | Typ | null-erlaubt | Verifikation |
|---|---|---|---|
| `line` | `int` | n/a (primitiv) | Bereichsprüfung im Code: `if (line < 0 || line >= lineElements.size()) return;` (Zeile 150–152). |

Rückgabewert: `void`. Verhalten laut Javadoc **und** verifiziertem Code: Index außerhalb des
gültigen Bereichs wird **stillschweigend ignoriert** (kein Exception, kein Effekt) — deckt sich mit
der Javadoc-Aussage „Außerhalb des Bereichs liegende Indizes werden ignoriert." Innerhalb des
Bereichs wird `TextCssClasses.HIGHLIGHT` zur betroffenen Zeile hinzugefügt, der Index in
`highlightedLines` vermerkt (mehrere Zeilen können gleichzeitig hervorgehoben sein, bereits
markierte bleiben erhalten) und per `scrollTo(row)` in den sichtbaren Bereich gescrollt. Keine
Exceptions möglich.

# Citations

[1] [TextViewer (Übersicht)](./text-viewer.md)
