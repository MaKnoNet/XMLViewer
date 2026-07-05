---
type: API Reference
title: CodeViewer.effectiveLanguageId(...)
description: Methode effectiveLanguageId von CodeViewer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/code/CodeViewer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `private String effectiveLanguageId()`


Package-interne Helfer-Methode: `CodeLanguage resolved = language != null ? language :
CodeLanguageDetector.fromContent(text);` gibt dann `resolved.cm6Id()` zurück. Keine Parameter.
Rückgabewert: `String`, nie `null` (da `cm6Id()` von `CodeLanguage` nie `null` liefert, siehe
[CodeLanguage](/api-reference/code/code-language/code-language.md)). Keine Exceptions.

# Citations

[1] [CodeViewer (Übersicht)](./code-viewer.md)
