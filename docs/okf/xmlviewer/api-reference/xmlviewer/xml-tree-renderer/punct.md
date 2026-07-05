---
type: API Reference
title: XmlTreeRenderer.punct(...)
description: Methode punct von XmlTreeRenderer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/xmlviewer/XmlTreeRenderer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## punct (private static)


```java
private static Span punct(String text)
```

- `text` (`String`) — null-erlaubt: **nein, nicht abgesichert.** Anders als bei `token(...)`
  gibt es hier **keine** Null-Normalisierung. `new Span(text)` mit `text == null` — Vaadins
  `Span(String)`-Konstruktor akzeptiert laut Vaadin-Verhalten `null` typischerweise ohne
  eigene Prüfung (setzt intern leeren Text oder wirft, je nach Vaadin-Version, aber das ist
  außerhalb dieser Klasse); in diesem Quellcode selbst gibt es keinen Null-Check und keinen
  `throw`. Alle tatsächlichen Aufrufer im Code übergeben ausschließlich Literal-Strings
  (`"<"`, `">"`, `"/>"`, `"="`, `"\""`, `"</"`, `"<!--"`, `"-->"`, `"<![CDATA["`, `"]]>"`),
  nie `null` — das Risiko ist damit im Projekt praktisch nicht erreichbar, aber die Methode
  selbst validiert nichts.
- Rückgabewert (`Span`) — nie `null` (die Span-Konstruktion selbst schlägt für die
  verwendeten Literal-Argumente nie fehl).
- Geworfene Exceptions: keine explizit; theoretisch NPE-Weiterleitung durch Vaadin bei
  `null`-Argument, aber im Projekt nie mit `null` aufgerufen.

# Citations

[1] [XmlTreeRenderer (Übersicht)](./xml-tree-renderer.md)
