---
type: API Reference
title: MatchChangeEvent.getCurrentMatchIndex(...)
description: Methode getCurrentMatchIndex von MatchChangeEvent - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/navigation/MatchChangeEvent.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `public int getCurrentMatchIndex()`


- Keine Parameter. Rückgabewert: `int`, 0-basierter Index des aktuellen Treffers, oder
  `-1`, wenn keiner aktiv ist (Bedeutung laut Javadoc-Kommentar; der Wert wird 1:1
  durchgereicht, die Konvention „-1 = kein Treffer" wird von den Aufrufern, nicht von dieser
  Klasse selbst, sichergestellt). Exceptions: keine.

# Citations

[1] [MatchChangeEvent (Übersicht)](./match-change-event.md)
