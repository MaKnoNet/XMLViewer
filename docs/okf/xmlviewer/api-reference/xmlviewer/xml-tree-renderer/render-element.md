---
type: API Reference
title: XmlTreeRenderer.renderElement(...)
description: Methode renderElement von XmlTreeRenderer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/xmlviewer/XmlTreeRenderer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## renderElement (private)


```java
private void renderElement(Element element, Div container, int depth)
```

- `element` (`Element`) — null-erlaubt: nein, nicht abgesichert; `meaningfulContent(element)`
  ruft `element.getContent()` auf → NPE bei `null`.
- `container` (`Div`) — null-erlaubt: nein, nicht abgesichert; `container.add(header)` würde
  bei `null` eine NPE werfen. In der Praxis immer ein frisch erzeugter `Div`.
- `depth` (`int`) — primitiver Typ, kein Null-Fall. Negative Werte würden `prependRails`
  einfach 0-mal iterieren lassen (keine Exception, da die `for`-Schleife bei `i < depth`
  mit negativem `depth` sofort abbricht).
- Rückgabewert: `void`.
- Geworfene Exceptions: implizite `NullPointerException` bei `element == null` oder
  `container == null` (kein expliziter Check).
- Rekursiver Aufbau: baut die Start-Tag-Zeile, entscheidet zwischen "kein Inhalt"
  (selbstschließendes Tag), "einzelner Inline-Text" (Text direkt in der Start-Zeile) und
  "hat Kindknoten" (rekursiver Aufruf über `renderChildren` + separate End-Tag-Zeile).

# Citations

[1] [XmlTreeRenderer (Übersicht)](./xml-tree-renderer.md)
