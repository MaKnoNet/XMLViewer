---
type: API Reference
title: TextViewer.render(...)
description: Methode render von TextViewer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/text/TextViewer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `private void render()`


Package-interne Helfer-Methode (hier der Vollständigkeit halber dokumentiert, da sie zentrale Logik
enthält): Baut die Zeilen komplett neu auf. Bei leerem `text` (`text.isEmpty()`) wird
`lineElements`/`lineTexts` auf `List.of()` gesetzt und ein Empty-Placeholder (`TextCssClasses.EMPTY`)
gerendert; sonst werden Zeilen über `splitLines(text)` erzeugt, je Zeile ein `Div` mit optionalem
Gutter und einem `SearchToken` gebaut. Am Ende wird immer ein neuer `SearchController` mit den
aktuellen Tokens instanziiert, Case-Sensitivity und Splitter erneut gesetzt, der Highlight-Renderer
geleert und `fireSearchReset()` gefeuert. Kein Parameter, `void`, keine Exceptions.

# Citations

[1] [TextViewer (Übersicht)](./text-viewer.md)
