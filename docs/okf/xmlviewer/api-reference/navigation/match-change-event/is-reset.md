---
type: API Reference
title: MatchChangeEvent.isReset(...)
description: Methode isReset von MatchChangeEvent - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/navigation/MatchChangeEvent.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `public boolean isReset()`


- Keine Parameter. Rückgabewert: `boolean`. `true` bedeutet laut Javadoc-Kommentar: die
  Suche wurde zurückgesetzt, weil der dargestellte Inhalt ersetzt wurde (z.&nbsp;B.
  `setRoot`/`setText`), nicht bloß, weil die aktuelle Eingabe keine Treffer hatte. Diese
  Unterscheidung wird ausschließlich vom jeweiligen Aufrufer (z.&nbsp;B. `XmlViewer.render()`
  ruft `fireSearchReset()` mit `reset = true` auf, `fireMatchChange()` mit dem
  dreiparametrigen Konstruktor, also `reset = false`) korrekt gepflegt — die Klasse selbst
  erzwingt diese Semantik nicht. Exceptions: keine.

# Citations

[1] [MatchChangeEvent (Übersicht)](./match-change-event.md)
