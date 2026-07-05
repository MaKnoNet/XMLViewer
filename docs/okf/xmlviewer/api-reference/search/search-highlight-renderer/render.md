---
type: API Reference
title: SearchHighlightRenderer.render(...)
description: Methode render von SearchHighlightRenderer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/search/SearchHighlightRenderer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `render(List<TokenMatch> matches, int currentIndex)`


```java
void render(List<TokenMatch> matches, int currentIndex);
```

- `matches` (`List<TokenMatch>`) — null-erlaubt: **nicht spezifiziert im Interface**. Der
  Javadoc dokumentiert nur den Sonderfall „eine leere Liste ... entfernt vorhandene
  Markierungen", trifft aber keine Aussage zu `null`. Da es sich um eine abstrakte
  Methode ohne Body handelt, gibt es keinen Code, der dies erzwingt — es ist reine
  Implementierungssache. Der einzige Aufrufer im Projekt,
  [SearchController#search(String)](/api-reference/search/search-controller/search.md) und
  `clearSearch()`, übergibt immer ein nicht-null `List`-Objekt (im schlimmsten Fall
  `List.of()`), sodass `null` in der Praxis nicht auftritt.
- `currentIndex` (`int`) — null-erlaubt: entfällt (primitiver Typ). Laut Javadoc
  bedeutet `-1` „kein aktueller Treffer" (typischerweise korrespondierend mit leerem
  `matches`).
- **Rückgabewert:** `void`.
- **Geworfene Exceptions:** vom Interface keine deklariert; abhängig von der
  Implementierung (siehe [FrontendSearchHighlighter#render](/api-reference/search/frontend-search-highlighter/render.md)).

# Citations

[1] [SearchHighlightRenderer (Übersicht)](./search-highlight-renderer.md)
