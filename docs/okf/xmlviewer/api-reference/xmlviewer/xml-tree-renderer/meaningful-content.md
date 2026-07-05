---
type: API Reference
title: XmlTreeRenderer.meaningfulContent(...)
description: Methode meaningfulContent von XmlTreeRenderer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/xmlviewer/XmlTreeRenderer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## meaningfulContent (private static)


```java
private static List<Content> meaningfulContent(Element element)
```

- `element` (`Element`) — null-erlaubt: nein, nicht abgesichert; `element.getContent()`
  würde bei `null` eine NPE werfen.
- Rückgabewert (`List<Content>`) — nie `null`; immer eine neue `ArrayList` (ggf. leer),
  nie das Ergebnis von `element.getContent()` selbst.
- Geworfene Exceptions: implizite NPE bei `element == null`.
- Filtert aus `element.getContent()` alle Knoten heraus, die dargestellt werden: `CDATA`
  immer, `Text` nur wenn nach `trim()` nicht leer, `Element` und `Comment` immer; alle
  anderen JDOM2-Inhaltstypen (`default`-Zweig im `switch`) werden verworfen.

# Citations

[1] [XmlTreeRenderer (Übersicht)](./xml-tree-renderer.md)
