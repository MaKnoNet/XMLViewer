---
type: API Reference
title: XmlTreeRenderer – Konstruktoren
description: Alle Konstruktoren von XmlTreeRenderer.
resource: web-common/src/main/java/de/makno/web/common/component/xmlviewer/XmlTreeRenderer.java
tags: [api-reference, constructor]
timestamp: '2026-07-08T09:00:00+02:00'
---


```java
XmlTreeRenderer(boolean collapsible)
```

- `collapsible` (`boolean`) — primitiver Typ, kann nicht `null` sein (nicht anwendbar/kein
  null-Fall). Steuert, ob Elemente mit Inhalt ein Aufklapp-Dreieck (`CssClasses.TOGGLE`)
  erhalten oder nur einen Einrückungs-Platzhalter (`newIndent()`).
- Keine Validierung nötig oder vorhanden (boolescher Wert kann nicht ungültig sein); der
  Konstruktor weist das Feld unverändert zu und initialisiert die vier `IdentityHashMap`-
  und die eine `ArrayList`-Instanzfelder über deren Feld-Initialisierer.
- Wirft nichts.

# Citations

[1] [XmlTreeRenderer (Übersicht)](./xml-tree-renderer.md)
