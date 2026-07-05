---
type: API Reference
title: SearchHighlightRenderer
description: Schlanke, serialisierbare Abstraktion für das Zeichnen von Suchtreffern, entkoppelt von deren Ermittlung im SearchController.
resource: web-common/src/main/java/de/makno/web/common/component/search/SearchHighlightRenderer.java
tags: [api-reference, search, interface]
timestamp: '2026-07-07T10:00:00+02:00'
---

# Überblick

`SearchHighlightRenderer` ist ein `Serializable`-Interface mit drei Methoden, das das
**Zeichnen** von Suchtreffern von deren **Ermittlung** in
[SearchController](/api-reference/search-controller.md) trennt (Dependency Inversion). Die
Standardimplementierung [FrontendSearchHighlighter](/api-reference/frontend-search-highlighter.md)
lagert das Highlighting ins Frontend aus (CSS Custom Highlight API) — ohne zusätzlichen
DOM-Knoten oder Session-Heap pro Treffer. Tests können stattdessen einen aufzeichnenden
Renderer übergeben, ohne einen echten Client zu benötigen. Architekturkontext in
[Geteilte Such-Engine](/architecture/search-engine.md).

**Thread-Safety:** Das Interface selbst ist zustandslos; Thread-Safety-Eigenschaften
hängen von der konkreten Implementierung ab. `FrontendSearchHighlighter` etwa hält nur
eine unveränderliche Komponentenreferenz und ist an genau eine UI/Session gebunden.

# Methoden

## `render(List<TokenMatch> matches, int currentIndex)`

```java
void render(List<TokenMatch> matches, int currentIndex);
```

- `matches` (`List<TokenMatch>`) — null-erlaubt: **nicht spezifiziert im Interface**. Der
  Javadoc dokumentiert nur den Sonderfall „eine leere Liste ... entfernt vorhandene
  Markierungen", trifft aber keine Aussage zu `null`. Da es sich um eine abstrakte
  Methode ohne Body handelt, gibt es keinen Code, der dies erzwingt — es ist reine
  Implementierungssache. Der einzige Aufrufer im Projekt,
  [SearchController#search(String)](/api-reference/search-controller.md#search) und
  `clearSearch()`, übergibt immer ein nicht-null `List`-Objekt (im schlimmsten Fall
  `List.of()`), sodass `null` in der Praxis nicht auftritt.
- `currentIndex` (`int`) — null-erlaubt: entfällt (primitiver Typ). Laut Javadoc
  bedeutet `-1` „kein aktueller Treffer" (typischerweise korrespondierend mit leerem
  `matches`).
- **Rückgabewert:** `void`.
- **Geworfene Exceptions:** vom Interface keine deklariert; abhängig von der
  Implementierung (siehe [FrontendSearchHighlighter#render](/api-reference/frontend-search-highlighter.md#render)).

## `moveCurrent(int currentIndex)`

```java
void moveCurrent(int currentIndex);
```

- `currentIndex` (`int`) — null-erlaubt: entfällt (primitiver Typ). Verschiebt nur die
  Hervorhebung des aktuellen Treffers, ohne die Treffermenge neu zu übertragen (laut
  Javadoc; das Interface selbst kann das nicht erzwingen, es ist Vertrag für
  Implementierungen).
- **Rückgabewert:** `void`.
- **Geworfene Exceptions:** vom Interface keine deklariert.

## `clear()`

```java
void clear();
```

- Keine Parameter.
- **Rückgabewert:** `void`.
- **Geworfene Exceptions:** vom Interface keine deklariert.
- Entfernt laut Javadoc-Vertrag alle Such-Markierungen.

# Citations

[1] web-common/src/main/java/de/makno/web/common/component/search/SearchHighlightRenderer.java
