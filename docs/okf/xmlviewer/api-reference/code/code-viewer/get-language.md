---
type: API Reference
title: CodeViewer.getLanguage(...)
description: Methode getLanguage von CodeViewer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/code/CodeViewer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `public CodeLanguage getLanguage()`


Keine Parameter. Rückgabewert: `CodeLanguage`, **kann `null` sein** — und zwar genau dann, wenn keine
Sprache explizit über `setLanguage`/`setLanguageFromFileName`/den dreiparametrigen Konstruktor gesetzt
wurde (Default-Zustand) oder `setLanguage(null)` zuletzt aufgerufen wurde. `null` bedeutet semantisch
"Auto-Erkennung aus dem Inhalt aktiv", nicht "kein Wert vorhanden" im fehlerhaften Sinn. Keine
Exceptions.

# Citations

[1] [CodeViewer (Übersicht)](./code-viewer.md)
