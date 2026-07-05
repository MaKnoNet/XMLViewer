---
type: API Reference
title: SearchController.mergeOverlaps(...)
description: Methode mergeOverlaps von SearchController - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/search/SearchController.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `mergeOverlaps(List<int[]> sortedRanges)` (private static)


```java
private static List<int[]> mergeOverlaps(List<int[]> sortedRanges)
```

Verschmilzt überlappende/anschließende Intervalle einer nach Start sortierten Liste.

- `sortedRanges` (`List<int[]>`) — null-erlaubt: **nein, faktisch nicht geprüft**. Kein
  expliziter Null-Check; eine erweiterte `for`-Schleife über `sortedRanges` würde bei
  `null` eine `NullPointerException` auslösen. Einziger Aufrufer ist
  `findMatchRanges`, das stets eine (ggf. leere) nicht-null `ArrayList` übergibt.
- Erwartet implizit, dass `sortedRanges` bereits nach Start-Offset sortiert ist (wird von
  `findMatchRanges` vor dem Aufruf per `ranges.sort(...)` sichergestellt) — diese Methode
  selbst prüft das nicht, sondern verlässt sich auf den Aufrufer-Vertrag.
- **Rückgabewert:** `List<int[]>`, niemals `null` (mindestens leere `ArrayList`).
- **Geworfene Exceptions:** implizite `NullPointerException` bei `sortedRanges == null`
  (siehe oben).

# Citations

[1] [SearchController (Übersicht)](./search-controller.md)
