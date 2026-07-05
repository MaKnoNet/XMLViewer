---
type: API Reference
title: TextViewer.Test-Helfer(...)
description: Methode Test-Helfer von TextViewer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/text/TextViewer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## Paket-sichtbare Test-Helfer


Diese vier Methoden sind package-private (kein Modifier) und laut Kommentar im Quellcode
ausschließlich für Tests gedacht:

### `int lineCount()`

Keine Parameter. Gibt `lineElements.size()` zurück, `int`, nie `null` (primitiv). Keine Exceptions.

### `List<String> searchableTexts()`

Keine Parameter. Gibt eine **unveränderliche Kopie** (`List.copyOf(lineTexts)`) zurück — nie `null`.
`List.copyOf` wirft laut JDK-Doku eine `NullPointerException`, wenn die Quellliste `null`-Elemente
enthält; da `lineTexts` nur über `splitLines`/`List.of()` befüllt wird und beide keine `null`-Einträge
produzieren, ist das praktisch ausgeschlossen. Keine weiteren Exceptions.

### `Div lineOf(int line)`

| Parameter | Typ | Verifikation |
|---|---|---|
| `line` | `int` | **Keine Bereichsprüfung** (anders als `highlight(int)`!). Direkter Aufruf von `lineElements.get(line)`. |

**Geworfene Exception:** `IndexOutOfBoundsException`, wenn `line < 0` oder `line >= lineElements.size()`
— im Gegensatz zu `highlight(int)`, das ungültige Indizes stillschweigend ignoriert. Rückgabewert:
`Div`, nie `null` bei gültigem Index.

### `boolean isLineHighlighted(int line)`

| Parameter | Typ | Verifikation |
|---|---|---|
| `line` | `int` | Wie bei `lineOf`: keine eigene Bereichsprüfung, ruft `lineElements.get(line)` auf. |

**Geworfene Exception:** `IndexOutOfBoundsException` bei ungültigem Index (gleiches Muster wie
`lineOf`). Rückgabewert: `boolean`, prüft, ob die CSS-Klassenliste des Zeilen-Elements
`TextCssClasses.HIGHLIGHT` enthält.

# Citations

[1] [TextViewer (Übersicht)](./text-viewer.md)
