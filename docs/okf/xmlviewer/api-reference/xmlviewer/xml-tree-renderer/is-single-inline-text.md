---
type: API Reference
title: XmlTreeRenderer.isSingleInlineText(...)
description: Methode isSingleInlineText von XmlTreeRenderer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/xmlviewer/XmlTreeRenderer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## isSingleInlineText (private static)


```java
private static boolean isSingleInlineText(List<Content> meaningful)
```

- `meaningful` (`List<Content>`) — null-erlaubt: nein, nicht abgesichert;
  `meaningful.size()` würde bei `null` eine NPE werfen.
- Rückgabewert (`boolean`) — primitiv, kann nicht `null` sein. `true`, wenn genau ein
  Inhaltselement vorhanden ist, das ein `Text`, aber **kein** `CDATA` ist (da `CDATA extends
  Text` in JDOM2, schließt die explizite `!(... instanceof CDATA)`-Prüfung CDATA-Knoten
  bewusst aus dem Inline-Pfad aus).
- Geworfene Exceptions: implizite NPE bei `meaningful == null`.

# Citations

[1] [XmlTreeRenderer (Übersicht)](./xml-tree-renderer.md)
