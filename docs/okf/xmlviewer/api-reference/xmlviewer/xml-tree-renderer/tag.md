---
type: API Reference
title: XmlTreeRenderer.tag(...)
description: Methode tag von XmlTreeRenderer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/xmlviewer/XmlTreeRenderer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## tag (private)


```java
private Span tag(Element element)
```

- `element` (`Element`) — null-erlaubt: nein; `element.getQualifiedName()` wirft eine
  `NullPointerException`, wenn `element == null`.
- Rückgabewert (`Span`) — nie `null` (sofern keine Exception geworfen wird); liefert das
  Ergebnis von `token(CssClasses.TAG, ..., element)`, das stets einen `Span` konstruiert.
- Geworfene Exceptions: implizite NPE bei `element == null`.

# Citations

[1] [XmlTreeRenderer (Übersicht)](./xml-tree-renderer.md)
