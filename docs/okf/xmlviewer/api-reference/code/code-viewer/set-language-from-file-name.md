---
type: API Reference
title: CodeViewer.setLanguageFromFileName(...)
description: Methode setLanguageFromFileName von CodeViewer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/code/CodeViewer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `public void setLanguageFromFileName(String fileName)`


| Parameter | Typ | null-erlaubt | Verifikation |
|---|---|---|---|
| `fileName` | `String` | **ja** | Wird ungeprüft an `CodeLanguageDetector.fromFileName(fileName)` weitergereicht, welches `null` explizit behandelt (gibt `CodeLanguage.PLAIN` zurück, siehe [CodeLanguageDetector](/api-reference/code/code-language-detector/code-language-detector.md)). Kein eigener Null-Check in `CodeViewer`, aber auch keine Gefahr einer NPE, da der Detector `null` selbst abfängt. |

Delegiert an `setLanguage(...)`. `void`, keine Exceptions.

# Citations

[1] [CodeViewer (Übersicht)](./code-viewer.md)
