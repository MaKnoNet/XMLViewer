---
type: API Reference
title: CodeViewer – Konstruktoren
description: Alle Konstruktoren von CodeViewer.
resource: web-common/src/main/java/de/makno/web/common/component/code/CodeViewer.java
tags: [api-reference, constructor]
timestamp: '2026-07-08T09:00:00+02:00'
---


## `public CodeViewer()`

Parameterlos. Fügt die CSS-Klasse `CodeCssClasses.ROOT` hinzu. Alle Felder bleiben auf ihren
Default-Werten (`text = ""`, `language = null` → Auto-Erkennung, `dark = false`, `wrap = false`,
`showLineNumbers = true`, `searchCaseSensitive = false`). Keine Exceptions.

## `public CodeViewer(String text)`

| Parameter | Typ | null-erlaubt | Verifikation |
|---|---|---|---|
| `text` | `String` | ja | Ruft `this()` auf, dann `this.text = text == null ? "" : text;` (Zeile 77) — `null` wird explizit zu `""` normalisiert. |

Kein `throws`; keine Exception im Rumpf.

## `public CodeViewer(String text, CodeLanguage language)`

| Parameter | Typ | null-erlaubt | Verifikation |
|---|---|---|---|
| `text` | `String` | ja | Ruft `this(text)` auf — siehe oben, normalisiert `null` zu `""`. |
| `language` | `CodeLanguage` | **ja** | Zeile 82: `this.language = language;` — ungeprüfte Zuweisung, `null` wird direkt übernommen. Das ist semantisch korrekt und beabsichtigt: `null` bedeutet laut `getLanguage()`-Javadoc "Auto-Erkennung", nicht ein Fehlerzustand. |

Keine Exceptions im Rumpf.

# Citations

[1] [CodeViewer (Übersicht)](./code-viewer.md)
