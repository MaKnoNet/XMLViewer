---
type: API Reference
title: XmlTreeRenderer.render(...)
description: Methode render von XmlTreeRenderer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/xmlviewer/XmlTreeRenderer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## render


```java
RenderedTree render(Element root)
```
Paket-sichtbar (keine explizite Modifier-Angabe = package-private).

- `root` (`Element`) — null-erlaubt: **nein in der Praxis, aber nicht verifiziert im
  Code selbst.** Es gibt keinen expliziten Null-Check. Wird `null` übergeben, ruft
  `renderElement(null, container, 0)` unmittelbar `meaningfulContent(null)` auf, was
  `element.getContent()` auf einer `null`-Referenz aufruft → **`NullPointerException`**
  (implizit, nicht deklariert, nicht dokumentiert). Der einzige Aufrufer im Projekt
  (`XmlViewer.render()`) ruft `render(root)` nur im `else`-Zweig auf, wenn `root != null`
  bereits geprüft wurde — die Absicherung liegt beim Aufrufer, nicht in dieser Methode.
- Rückgabewert (`RenderedTree`) — nie `null`: Es wird immer ein neues `RenderedTree`-Objekt
  konstruiert und zurückgegeben (kein Pfad, der `null` liefert), vorausgesetzt die Methode
  kehrt überhaupt zurück (siehe NPE-Fall oben).
- Geworfene Exceptions: `NullPointerException` (implizit) bei `root == null`, siehe oben.
  Sonst keine explizit geworfenen Exceptions im Methodenkörper.

# Citations

[1] [XmlTreeRenderer (Übersicht)](./xml-tree-renderer.md)
