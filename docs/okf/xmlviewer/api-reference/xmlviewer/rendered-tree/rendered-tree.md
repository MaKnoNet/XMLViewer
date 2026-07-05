---
type: API Reference
title: RenderedTree
description: Package-private Record mit dem Ergebnis eines Render-Durchlaufs von XmlTreeRenderer — Wurzel-Div, identitätsbasierte Element-Abbildungen und die Token-Liste für die Suche.
resource: web-common/src/main/java/de/makno/web/common/component/xmlviewer/RenderedTree.java
tags: [api-reference, xmlviewer, record, rendering]
timestamp: '2026-07-07T10:00:00+02:00'
---

# Überblick


`RenderedTree` ist ein package-private `record`, das Rückgabewert von
[XmlTreeRenderer.render(Element)](/api-reference/xmlviewer/xml-tree-renderer/xml-tree-renderer.md) ist und implizit
`Serializable` implementiert (Klassendeklaration `implements Serializable`, plus explizite
`serialVersionUID = 1L`). Es bündelt den gerenderten Wurzel-`Div`-Container zusammen mit
vier identitätsbasierten `Element→Component`-Abbildungen (für Highlight, Ein-/Ausklappen)
und der Liste aller durchsuchbaren Tokens in Dokumentreihenfolge.

**Thread-Safety:** Der Record selbst ist unveränderlich (alle Komponenten sind `final`
Record-Komponenten, keine Setter). Die referenzierten `Map`-Instanzen sind jedoch
klassischerweise `IdentityHashMap`-Instanzen aus `XmlTreeRenderer` — diese Maps sind selbst
**nicht** thread-sicher und werden nach dem Rendern nur noch von genau einer
`XmlViewer`-Instanz gelesen/mutiert (Klapp-Zustand über `Div#setVisible`), passend zum
"nicht thread-safe, eine Instanz pro Session"-Vertrag von
[XmlViewer](/api-reference/xmlviewer/xml-viewer/xml-viewer.md). Kein geteilter veränderlicher Zustand zwischen
Sessions, da jede Session ihren eigenen `RenderedTree` erhält.

# Felder

Felder = Record-Komponenten, siehe [Konstruktor](./constructor.md) (`root`, `elementHeaders`,
`childContainers`, `endTags`, `toggles`, `tokens`).

# Thread-Safety

**Der Record selbst ist unveränderlich** (verifiziert: alle sechs Komponenten sind `final`
Record-Felder ohne Setter). Die referenzierten `Map`- und `List`-Instanzen sind jedoch
klassischerweise `IdentityHashMap`/`ArrayList`-Instanzen aus
[XmlTreeRenderer](/api-reference/xmlviewer/xml-tree-renderer/xml-tree-renderer.md) — diese Collections sind selbst
**nicht** synchronisiert. Nach dem Rendern werden sie nur noch von genau einer
[XmlViewer](/api-reference/xmlviewer/xml-viewer/xml-viewer.md)-Instanz gelesen/mutiert (Klapp-Zustand über
`Div#setVisible`), passend zum "nicht thread-safe, eine Instanz pro Session"-Vertrag von
`XmlViewer`. Kein geteilter veränderlicher Zustand zwischen Sessions, da jede Session ihren
eigenen `RenderedTree` mit eigenen Collection-Instanzen erhält.

# Serialisierung

**`Serializable`** (verifiziert: `implements Serializable` in der Record-Deklaration), mit
explizit gesetzter `private static final long serialVersionUID = 1L;`. Diese explizite ID
stabilisiert die Serialisierungskompatibilität über Code-Änderungen hinweg (kein automatisch
berechneter Hash, der bei jeder Feldänderung bricht). Die referenzierten Typen `Div`, `Span`
(Vaadin-Flow-Komponenten) sowie `Map`/`List` sind ihrerseits serialisierbar, sodass der
gesamte Record im Rahmen der Vaadin-Session-Serialisierung mitgeschrieben werden kann.

# equals/hashCode/toString

**Komponentenbasierte Semantik** — Records generieren `equals`/`hashCode`/`toString`
automatisch aus allen sechs Komponenten (`root`, `elementHeaders`, `childContainers`,
`endTags`, `toggles`, `tokens`). Keine dieser Methoden ist im Quellcode manuell überschrieben
(verifiziert). In der Praxis hat `equals`/`hashCode` hier wenig Relevanz, da `Div`/`Span`
(Vaadin-Komponenten) und die `IdentityHashMap`-Werte selbst keine sinnvolle Werte-Gleichheit
definieren (Komponenten vergleichen sich per Identität) — zwei `RenderedTree`-Instanzen sind
daher faktisch nur dann `equals`, wenn sie exakt dieselben Objektreferenzen bündeln.

# Vererbungshierarchie


**Vorwärts (eigene Deklaration):** `record RenderedTree(Div root, Map<Element, Div>
elementHeaders, Map<Element, Div> childContainers, Map<Element, Div> endTags, Map<Element,
Span> toggles, List<SearchableToken> tokens) implements Serializable` (package-private).

- **Superklasse:** implizit `java.lang.Record` — JDK-Typ, kein Cross-Link.
- **Interfaces:**
  - `java.io.Serializable` — JDK-Standard-Interface (Marker-Interface, keine Methoden), kein
    Projekt-Typ, daher kein Cross-Link.
- Records sind implizit `final` — es kann keine Subklasse geben.

**Rückwärts (Abhängige):** Verifiziert per Grep auf `extends RenderedTree` über den gesamten
`web-common/src/main/java/de/makno/web/common/component/`-Baum — **kein Treffer**
(erwartungsgemäß, Records können nicht erweitert werden). `RenderedTree` ist der Rückgabewert
von [XmlTreeRenderer](/api-reference/xmlviewer/xml-tree-renderer/xml-tree-renderer.md).render(Element) und wird von
[XmlViewer](/api-reference/xmlviewer/xml-viewer/xml-viewer.md) gehalten (Assoziation, nicht Vererbung).

# Konstruktoren

- [siehe constructor.md](./constructor.md)

# Methoden


# Citations


[1] web-common/src/main/java/de/makno/web/common/component/xmlviewer/RenderedTree.java
