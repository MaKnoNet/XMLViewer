---
type: API Reference
title: SearchController.findMatchRanges(...)
description: Methode findMatchRanges von SearchController - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/search/SearchController.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `findMatchRanges(String text, List<String> terms)` (private) <a id="findmatchranges"></a>


```java
private List<int[]> findMatchRanges(String text, List<String> terms)
```

Findet alle Treffer-Intervalle aller Begriffe im Text, sortiert nach Start und mit
verschmolzenen Überlappungen (z.&nbsp;B. „EUR" und „EU"), sodass sich Bereiche nicht
überlagern. Der Abgleich erfolgt zeichenweise direkt auf dem unveränderten Originaltext
via `String#regionMatches(boolean, int, String, int, int)` (Ignorier-Groß-/Kleinschreibung
gesteuert über `!caseSensitive`) — dadurch bleiben Offsets auch bei
Lowercase-Längenänderungen (z.&nbsp;B. türkisches `İ`) korrekt im Originaltext verankert.

- `text` (`String`) — null-erlaubt: **nein, faktisch nicht geprüft**. Kein expliziter
  Null-Check; `text.length()` würde bei `text == null` eine `NullPointerException`
  auslösen. Einziger Aufrufer ist `collectMatches`, das `tokens.get(index).text()`
  übergibt — laut [SearchToken](/api-reference/search/search-token/search-token.md)-Konstruktor kann `text`
  dort nie `null` sein (kompakter Konstruktor prüft das), solange das Token selbst nicht
  `null` ist.
- `terms` (`List<String>`) — null-erlaubt: **nein, faktisch nicht erreichbar mit
  `null`**, aus denselben Gründen wie bei `collectMatches`. Leere Einträge (Länge 0)
  werden explizit übersprungen (`if (termLength == 0) { continue; }`).
- **Rückgabewert:** `List<int[]>`, niemals `null` (mindestens leere `ArrayList`); jedes
  `int[]`-Element hat genau zwei Einträge `{start, end}`.
- **Geworfene Exceptions:** implizite `NullPointerException` bei `text == null` (siehe
  oben); keine weiteren expliziten `throw`-Statements.

# Citations

[1] [SearchController (Übersicht)](./search-controller.md)
