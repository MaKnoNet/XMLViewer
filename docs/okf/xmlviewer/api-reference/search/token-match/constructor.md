---
type: API Reference
title: TokenMatch – Konstruktoren
description: Alle Konstruktoren von TokenMatch.
resource: web-common/src/main/java/de/makno/web/common/component/search/TokenMatch.java
tags: [api-reference, constructor]
timestamp: '2026-07-08T09:00:00+02:00'
---


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
  ([SearchController](/api-reference/search/search-controller/search-controller.md) erzeugt sie ausschließlich
  intern aus verifizierten Treffer-Bereichen).
- **Geworfene Exceptions:** keine.

# Citations

[1] [TokenMatch (Übersicht)](./token-match.md)
