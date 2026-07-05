---
type: API Reference
title: TextViewer – Konstruktoren
description: Alle Konstruktoren von TextViewer.
resource: web-common/src/main/java/de/makno/web/common/component/text/TextViewer.java
tags: [api-reference, constructor]
timestamp: '2026-07-08T09:00:00+02:00'
---


## `public TextViewer()`

Parameterlos. Fügt dem inneren `Div` (`getContent()`) die CSS-Klasse `TextCssClasses.ROOT` hinzu und
ruft `render()` auf, was mit leerem Text (`text = ""`) den Empty-Placeholder rendert und einen neuen
`SearchController` mit leeren Tokens anlegt. Wirft keine Exception; kein Parameter, daher keine
Null-Prüfung nötig.

## `public TextViewer(String text)`

| Parameter | Typ | null-erlaubt | Verifikation |
|---|---|---|---|
| `text` | `String` | ja | Ruft `this()` und dann `setText(text)` auf; `setText` behandelt `null` explizit als leeren String (`text == null ? "" : text`, Zeile 102). Kein NPE möglich. |

Kein `throws`; wirft keine Exception im Rumpf.

# Citations

[1] [TextViewer (Übersicht)](./text-viewer.md)
