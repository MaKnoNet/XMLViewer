---
type: API Reference
title: XmlTreeRenderer.newToggle(...)
description: Methode newToggle von XmlTreeRenderer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/xmlviewer/XmlTreeRenderer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## newToggle (private)


```java
private Span newToggle(Element element)
```

- `element` (`Element`) — null-erlaubt: ja im Sinne, dass diese Methode `element` nur
  als Schlüssel in `toggles.put(element, toggle)` verwendet; `IdentityHashMap` erlaubt
  `null`-Schlüssel, keine Exception.
- Rückgabewert (`Span`) — nie `null`; leerer `Span` mit CSS-Klasse `TOGGLE` (sichtbares
  Zeichen kommt aus CSS `::before`).
- Geworfene Exceptions: keine.

# Citations

[1] [XmlTreeRenderer (Übersicht)](./xml-tree-renderer.md)
