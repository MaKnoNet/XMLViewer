---
type: API Reference
title: XmlTreeRenderer.textLine(...)
description: Methode textLine von XmlTreeRenderer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/xmlviewer/XmlTreeRenderer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## textLine (private)


```java
private Div textLine(String text, Element owner, int depth)
```

- `text` (`String`) — null-erlaubt: ja im Sinne, dass diese Methode `text` nur an
  `token(...)` durchreicht, das `null` zu `""` normalisiert.
- `owner` (`Element`) — null-erlaubt: ja (wird nur durchgereicht, nicht dereferenziert
  in dieser Methode).
- `depth` (`int`) — primitiv.
- Rückgabewert (`Div`) — nie `null`.
- Geworfene Exceptions: keine in dieser Methode selbst.

# Citations

[1] [XmlTreeRenderer (Übersicht)](./xml-tree-renderer.md)
