---
type: API Reference
title: CodeViewer.setText(...)
description: Methode setText von CodeViewer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/code/CodeViewer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `public void setText(String text)`


| Parameter | Typ | null-erlaubt | Verifikation |
|---|---|---|---|
| `text` | `String` | **ja** | Zeile 109: `this.text = text == null ? "" : text;` — explizite Normalisierung, deckt sich mit Javadoc "`null` = leerer Text". |

Setzt zusätzlich `matchCount = 0`, `currentMatchIndex = -1`, `lastQuery = ""` zurück (Suchzustand wird
bei Textänderung verworfen), ruft `callJs("setDoc", this.text, effectiveLanguageId())` auf und feuert
`fireSearchReset()`. `void`, keine Exceptions.

# Citations

[1] [CodeViewer (Übersicht)](./code-viewer.md)
