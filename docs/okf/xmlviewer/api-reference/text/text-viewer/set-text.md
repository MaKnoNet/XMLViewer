---
type: API Reference
title: TextViewer.setText(...)
description: Methode setText von TextViewer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/text/TextViewer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `public void setText(String text)`


| Parameter | Typ | null-erlaubt | Verifikation |
|---|---|---|---|
| `text` | `String` | ja | Zeile 102: `this.text = text == null ? "" : text;` — `null` wird explizit zu `""` normalisiert, exakt wie der Methoden-Javadoc verspricht. |

Rückgabewert: `void`. Löst intern `render()` aus (kompletter Re-Render inkl. neuem
`SearchController`, wodurch eine zuvor aktive Suche implizit zurückgesetzt wird — `fireSearchReset()`
wird am Ende von `render()` gefeuert). Keine deklarierten oder geworfenen Exceptions.

# Citations

[1] [TextViewer (Übersicht)](./text-viewer.md)
