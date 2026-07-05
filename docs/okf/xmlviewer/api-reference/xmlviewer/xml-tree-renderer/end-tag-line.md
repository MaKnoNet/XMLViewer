---
type: API Reference
title: XmlTreeRenderer.endTagLine(...)
description: Methode endTagLine von XmlTreeRenderer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/xmlviewer/XmlTreeRenderer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## endTagLine (private)


```java
private Div endTagLine(Element element, int depth)
```

- `element` (`Element`) — null-erlaubt: nein; wird an `tag(element)` weitergereicht, das
  `element.getQualifiedName()` aufruft → NPE bei `null`.
- `depth` (`int`) — primitiv, kein Null-Fall.
- Rückgabewert (`Div`) — nie `null`.
- Geworfene Exceptions: implizite NPE bei `element == null` (über `tag(...)`).

# Citations

[1] [XmlTreeRenderer (Übersicht)](./xml-tree-renderer.md)
