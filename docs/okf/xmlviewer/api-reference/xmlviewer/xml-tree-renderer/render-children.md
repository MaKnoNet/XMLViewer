---
type: API Reference
title: XmlTreeRenderer.renderChildren(...)
description: Methode renderChildren von XmlTreeRenderer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/xmlviewer/XmlTreeRenderer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## renderChildren (private)


```java
private Div renderChildren(Element element, List<Content> meaningful, int depth)
```

- `element` (`Element`) — null-erlaubt: nein; wird als Schlüssel in `childContainers.put(element, children)`
  verwendet — `IdentityHashMap` erlaubt zwar `null`-Schlüssel, aber semantisch falsch;
  keine Exception, da `IdentityHashMap.put` `null`-Schlüssel toleriert.
- `meaningful` (`List<Content>`) — null-erlaubt: nein, nicht abgesichert; die `for`-Schleife
  (`for (Content content : meaningful)`) würde bei `null` eine `NullPointerException` werfen.
- `depth` (`int`) — primitiv, kein Null-Fall.
- Rückgabewert (`Div`) — nie `null`: Es wird immer ein neuer `Div` mit `CssClasses.CHILDREN`
  konstruiert und zurückgegeben.
- Geworfene Exceptions: implizite `NullPointerException` bei `meaningful == null`.
- Verwendet ein `switch`-Pattern-Matching über den `sealed`/bekannten `Content`-Typ (`Element`,
  `CDATA`, `Comment`, `Text`); der `default`-Zweig ("andere Knotentypen werden nicht
  dargestellt") behandelt z.&nbsp;B. `ProcessingInstruction`/`EntityRef` stillschweigend,
  ohne Exception oder Log — bewusstes Verschlucken unbekannter Inhaltstypen laut Kommentar.

# Citations

[1] [XmlTreeRenderer (Übersicht)](./xml-tree-renderer.md)
