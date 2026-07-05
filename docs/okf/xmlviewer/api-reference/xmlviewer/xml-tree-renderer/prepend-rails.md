---
type: API Reference
title: XmlTreeRenderer.prependRails(...)
description: Methode prependRails von XmlTreeRenderer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/xmlviewer/XmlTreeRenderer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## prependRails (private)


```java
private void prependRails(Div line, int depth)
```

- `line` (`Div`) — null-erlaubt: nein, sofern `depth > 0`; `line.add(rail())` würde bei
  `line == null` eine NPE werfen. Bei `depth <= 0` durchläuft die Schleife null Iterationen
  und `line` wird nie dereferenziert — dann wäre `null` folgenlos, aber das ist ein
  Nebeneffekt der Schleifenbedingung, keine bewusste Absicherung.
- `depth` (`int`) — primitiv; negative Werte führen zu null Schleifendurchläufen (kein Fehler).
- Rückgabewert: `void`.
- Geworfene Exceptions: implizite NPE bei `line == null` und `depth > 0`.
- Fügt `depth`-mal eine `RAIL`-Zelle voran (vor Toggle/Marker/Inhalt aufgerufen → linksbündig),
  trägt Einrückung und die senkrechten Führungslinien der Vorfahren-Ebenen.

# Citations

[1] [XmlTreeRenderer (Übersicht)](./xml-tree-renderer.md)
