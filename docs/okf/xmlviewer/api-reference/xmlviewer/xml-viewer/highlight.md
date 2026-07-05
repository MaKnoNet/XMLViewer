---
type: API Reference
title: XmlViewer.highlight(...)
description: Methode highlight von XmlViewer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/xmlviewer/XmlViewer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `public void highlight(Element element)`


- `element` — null-erlaubt: ja, verifiziert durch expliziten Guard: `if (element == null) {
  return; }` als erste Zeile — `null` wird still ignoriert, kein Fehler.
- Zusätzliches Verhalten: Ist `element` nicht `null`, aber im aktuell gerenderten Baum
  unbekannt (`tree.elementHeaders().get(element)` liefert `null`, z.&nbsp;B. weil `element`
  aus einem anderen JDOM2-Dokument stammt oder kein Element des aktuellen `root`-Baums ist),
  wird ebenfalls still zurückgekehrt (`if (header == null) { return; }`) — **kein** Fehler,
  auch wenn das Element „falsch" ist. Andernfalls: Vorfahren aufklappen (`expandTo`), CSS-Klasse
  `HIGHLIGHT` setzen, zur Menge `highlightedElements` hinzufügen, hinscrollen.
- Rückgabewert: keiner (`void`).
- Exceptions: keine.

# Citations

[1] [XmlViewer (Übersicht)](./xml-viewer.md)
