---
type: API Reference
title: TextViewer.newLine(...)
description: Methode newLine von TextViewer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/text/TextViewer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `private Div newLine(int index, String lineText)`


| Parameter | Typ | Verifikation |
|---|---|---|
| `index` | `int` | Nur zur Nummerierung verwendet (`Integer.toString(index + 1)`), keine Bereichsprüfung nötig, da nur intern mit gültigen Indizes aufgerufen. |
| `lineText` | `String` | Kein Null-Check; wird direkt in `new Span(lineText)` übergeben. Vaadins `Span`-Konstruktor akzeptiert laut Vaadin-API auch `null`/leere Strings; da `render()` diese Methode nur mit Einträgen aus `lineTexts` (nie `null`, siehe `splitLines`) aufruft, ist `null` hier praktisch ausgeschlossen. |

Rückgabewert: `Div`, nie `null`. Keine Exceptions.

# Citations

[1] [TextViewer (Übersicht)](./text-viewer.md)
