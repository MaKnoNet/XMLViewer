---
type: API Reference
title: FrontendSearchHighlighter.toFlatCsv(...)
description: Methode toFlatCsv von FrontendSearchHighlighter - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/search/FrontendSearchHighlighter.java
tags: [api-reference, method]
timestamp: '2026-08-05T12:00:00+02:00'
---

## `toFlatCsv(List<TokenMatch> matches)` (package-private, `static`)

```java
static String toFlatCsv(List<TokenMatch> matches)
```

- `matches` (`List<TokenMatch>`) — null-erlaubt: **nein, kein expliziter Null-Check**. Der
  Aufruf `matches.size()` bei der Dimensionierung des `StringBuilder` löst bei `null` eine
  `NullPointerException` aus (verifiziert am Code, nicht aus dem Javadoc übernommen). Der
  einzige produktive Aufrufer ist `render(...)` in derselben Klasse, der die Liste
  unverändert vom [SearchController](/api-reference/search/search-controller/search-controller.md)
  durchreicht — dort immer nicht-`null` (ggf. `List.of()`).
- Iteriert über jeden `TokenMatch` und hängt `tokenIndex()`, `start()`, `end()` sequenziell
  an, getrennt durch `,` (Konstante `SEPARATOR`). Format:
  `tokenIndex,start,end,tokenIndex,start,end,…` — **kein** führendes und kein
  abschließendes Trennzeichen.
- **Rückgabewert:** `String`, niemals `null`. Bei leerer `matches`-Liste ist das Ergebnis
  der **leere String** `""` (nicht `null`, nicht `","`) — das Gegenstück
  `search-highlighter.js` bildet ihn über `parseFlat` auf ein leeres Array ab.
- **Geworfene Exceptions:** implizite `NullPointerException` bei `matches == null` (siehe
  oben); keine expliziten `throw`-Statements im Rumpf.
- **Warum `String` und kein JSON-Typ:** `String` ist der einzige `executeJs`-Parametertyp,
  den alle Vaadin-Generationen unverändert unterstützen. Vaadin 25 hat `elemental.json`
  entfernt (Ersatz: Jackson 3), Collections als Parameter kann erst Vaadin 25. Details und
  Begründung in
  [Vaadin-Versionsunabhängigkeit](/conventions/vaadin-versionsunabhaengigkeit.md).
- **Sichtbarkeit:** `static` und package-private statt `private` — damit ist das
  Draht-Format ohne Browser direkt unit-testbar (`FrontendSearchHighlighterTest`).

# Citations

[1] [FrontendSearchHighlighter (Übersicht)](./frontend-search-highlighter.md)
[2] [Vaadin-Versionsunabhängigkeit](/conventions/vaadin-versionsunabhaengigkeit.md)
