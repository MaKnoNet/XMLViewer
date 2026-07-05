---
type: API Reference
title: CodeViewer.setLanguage(...)
description: Methode setLanguage von CodeViewer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/code/CodeViewer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `public void setLanguage(CodeLanguage language)`


| Parameter | Typ | null-erlaubt | Verifikation |
|---|---|---|---|
| `language` | `CodeLanguage` | **ja, explizit vorgesehen** | Zeile 123: ungeprüfte Zuweisung `this.language = language;`. Laut Javadoc aktiviert `null` "wieder die Auto-Erkennung aus dem Inhalt" — semantisch gültiger Wert, kein Fehlerfall. |

Ruft `callJs("setLanguage", effectiveLanguageId())` auf, wobei `effectiveLanguageId()` bei `language
== null` automatisch auf `CodeLanguageDetector.fromContent(text)` zurückfällt. `void`, keine
Exceptions.

# Citations

[1] [CodeViewer (Übersicht)](./code-viewer.md)
