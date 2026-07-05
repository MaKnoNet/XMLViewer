---
type: API Reference
title: FrontendSearchHighlighter.toFlatArray(...)
description: Methode toFlatArray von FrontendSearchHighlighter - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/search/FrontendSearchHighlighter.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `toFlatArray(List<TokenMatch> matches)` (package-private/private Hilfsmethode)


```java
private JsonArray toFlatArray(List<TokenMatch> matches)
```

- `matches` (`List<TokenMatch>`) — null-erlaubt: **nein, faktisch nicht geprüft**. Kein
  expliziter Null-Check; die erweiterte `for`-Schleife über `matches` würde bei `null`
  eine `NullPointerException` auslösen. Aufrufer ist ausschließlich `render(...)` in
  dieser Klasse (siehe oben).
- Iteriert über jeden `TokenMatch` in `matches` und schreibt `tokenIndex()`, `start()`,
  `end()` sequenziell in ein `JsonArray` (Format: `[tokenIndex, start, end, tokenIndex,
  start, end, …]`).
- **Rückgabewert:** `JsonArray`, niemals `null` — auch bei leerer `matches`-Liste wird ein
  (leeres) `JsonArray`-Objekt über `Json.createArray()` erzeugt und zurückgegeben.
- **Geworfene Exceptions:** implizite `NullPointerException`, falls `matches == null`
  (siehe oben).

# Citations

[1] [FrontendSearchHighlighter (Übersicht)](./frontend-search-highlighter.md)
