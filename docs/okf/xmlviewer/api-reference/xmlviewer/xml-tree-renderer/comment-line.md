---
type: API Reference
title: XmlTreeRenderer.commentLine(...)
description: Methode commentLine von XmlTreeRenderer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/xmlviewer/XmlTreeRenderer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## commentLine (private)


```java
private Div commentLine(String text, Element owner, int depth)
```

- `text` (`String`) — null-erlaubt: ja, wird nur an `token(...)` durchgereicht (Normalisierung
  dort).
- `owner` (`Element`) — null-erlaubt: ja, nur durchgereicht.
- `depth` (`int`) — primitiv.
- Rückgabewert (`Div`) — nie `null`.
- Geworfene Exceptions: keine in dieser Methode selbst.

# Citations

[1] [XmlTreeRenderer (Übersicht)](./xml-tree-renderer.md)
