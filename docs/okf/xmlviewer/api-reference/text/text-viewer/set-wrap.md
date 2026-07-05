---
type: API Reference
title: TextViewer.setWrap(...)
description: Methode setWrap von TextViewer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/text/TextViewer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `public void setWrap(boolean wrap)`


Primitive `boolean`. Setzt das Feld und togglet **nur** die CSS-Klasse `TextCssClasses.WRAP` am
Wurzelelement (`getContent().getElement().getClassList().set(...)`) — **kein** Re-Render, wie der
Javadoc-Kommentar korrekt behauptet ("Kein Neu-Rendern nötig"). Keine Exceptions.

# Citations

[1] [TextViewer (Übersicht)](./text-viewer.md)
