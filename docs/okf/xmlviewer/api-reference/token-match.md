---
type: API Reference
title: TokenMatch
description: Record für einen einzelnen Suchtreffer als Zeichen-Bereich innerhalb eines durchsuchbaren Tokens.
resource: web-common/src/main/java/de/makno/web/common/component/search/TokenMatch.java
tags: [api-reference, search, record]
timestamp: '2026-07-07T10:00:00+02:00'
---

# Überblick

`TokenMatch` ist ein `Serializable` **Record** mit den drei `int`-Komponenten
`tokenIndex`, `start` und `end`. Er beschreibt einen Suchtreffer rein als
Offset-Deskriptor — ohne Bezug auf konkrete UI-Knoten — und wird von
[SearchController](/api-reference/search-controller.md) erzeugt und an einen
[SearchHighlightRenderer](/api-reference/search-highlight-renderer.md) übergeben, der ihn
im Frontend zeichnet. Mehr zur Rolle im Gesamtbild in
[Geteilte Such-Engine](/architecture/search-engine.md).

`tokenIndex` referenziert die Position in der Token-Liste (= Dokumentreihenfolge der
`.search-token`-Spans im DOM), `start` ist der 0-basierte, inklusive Start-Offset im
Token-Text, `end` der exklusive End-Offset.

**Thread-Safety:** Records sind unveränderliche Value-Types mit ausschließlich
primitiven (`int`) Feldern — trivial thread-sicher, beliebig teilbar.

# Vererbungshierarchie

**Vorwärts (eigene Deklaration):** `public record TokenMatch(int tokenIndex, int start, int
end) implements Serializable`.

- **Superklasse:** implizit `java.lang.Record` — JDK-Typ, kein Cross-Link.
- **Interfaces:**
  - `java.io.Serializable` — JDK-Standard-Interface (Marker-Interface, keine Methoden), kein
    Projekt-Typ, daher kein Cross-Link.
- Records sind implizit `final` — es kann keine Subklasse geben.

**Rückwärts (Abhängige):** Verifiziert per Grep auf `extends TokenMatch` über den gesamten
`web-common/src/main/java/de/makno/web/common/component/`-Baum — **kein Treffer**
(erwartungsgemäß, Records können nicht erweitert werden). `TokenMatch` wird von
[SearchController](/api-reference/search-controller.md) erzeugt und an einen
[SearchHighlightRenderer](/api-reference/search-highlight-renderer.md) übergeben
(Assoziation, nicht Vererbung).

# Konstruktoren

## Kanonischer Konstruktor (implizit)

`TokenMatch` deklariert **keinen** kompakten Konstruktor — es existiert nur der von Java
implizit erzeugte kanonische Konstruktor mit den drei Parametern
`int tokenIndex, int start, int end` in Deklarationsreihenfolge.

- `tokenIndex` (`int`) — null-erlaubt: **entfällt** (primitiver Typ, kann nicht `null`
  sein).
- `start` (`int`) — null-erlaubt: **entfällt** (primitiver Typ).
- `end` (`int`) — null-erlaubt: **entfällt** (primitiver Typ).
- **Keine Validierung:** Weder Bereichsprüfung (z.&nbsp;B. `start >= 0`,
  `end >= start`) noch sonstige Invarianten werden geprüft — verifiziert durch
  Abwesenheit eines kompakten Konstruktors im Quellcode. Ein `TokenMatch` mit
  `start > end` oder negativen Werten lässt sich also unbeanstandet konstruieren; die
  Verantwortung für sinnvolle Offsets liegt beim Aufrufer
  ([SearchController](/api-reference/search-controller.md) erzeugt sie ausschließlich
  intern aus verifizierten Treffer-Bereichen).
- **Geworfene Exceptions:** keine.

# Methoden

## `tokenIndex()` / `start()` / `end()` (implizite Accessor-Methoden)

Von Record automatisch erzeugte Zugriffsmethoden für die drei Komponenten.

- **Rückgabewert:** jeweils `int` (primitiv, kann nicht `null` sein).
- Werfen nichts.

# Citations

[1] web-common/src/main/java/de/makno/web/common/component/search/TokenMatch.java
