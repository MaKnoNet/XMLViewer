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
[XmlTreeRenderer.render(Element)](/api-reference/xml-tree-renderer.md) ist und implizit
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
[XmlViewer](/api-reference/xml-viewer.md). Kein geteilter veränderlicher Zustand zwischen
Sessions, da jede Session ihren eigenen `RenderedTree` erhält.

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
von [XmlTreeRenderer](/api-reference/xml-tree-renderer.md).render(Element) und wird von
[XmlViewer](/api-reference/xml-viewer.md) gehalten (Assoziation, nicht Vererbung).

# Konstruktoren

Kanonischer (impliziter) Record-Konstruktor:

```java
RenderedTree(
    Div root,
    Map<Element, Div> elementHeaders,
    Map<Element, Div> childContainers,
    Map<Element, Div> endTags,
    Map<Element, Span> toggles,
    List<SearchableToken> tokens)
```

Es gibt **keinen** kompakten Konstruktor (compact constructor) im Quellcode — der Record
verwendet ausschließlich den impliziten kanonischen Konstruktor, der die Werte 1:1 in die
Komponenten übernimmt. Es findet **keine Validierung** statt.

- `root` (`Div`) — null-erlaubt: ja, verifiziert durch Fehlen jeglicher Prüfung im Record
  und durch `XmlViewer.emptyTree()`, das zwar stets ein `new Div()` (nie `null`) übergibt,
  aber der Record selbst würde `null` klaglos akzeptieren.
- `elementHeaders` (`Map<Element, Div>`) — null-erlaubt: ja (keine Prüfung); wird
  intern von `XmlTreeRenderer` immer als (ggf. leere) `IdentityHashMap` bzw. von
  `XmlViewer.emptyTree()` als `Collections.emptyMap()` übergeben, nie als `null` beobachtet,
  aber der Konstruktor selbst prüft nicht.
- `childContainers` (`Map<Element, Div>`) — null-erlaubt: ja, gleiche Begründung wie oben.
- `endTags` (`Map<Element, Div>`) — null-erlaubt: ja, gleiche Begründung wie oben.
- `toggles` (`Map<Element, Span>`) — null-erlaubt: ja, gleiche Begründung wie oben.
- `tokens` (`List<SearchableToken>`) — null-erlaubt: ja (keine Prüfung); wird stets als
  (ggf. leere) `ArrayList` bzw. `List.of()` übergeben.

**Was bei ungültiger Eingabe passiert:** Nichts im Konstruktor selbst — kein `throw`,
keine `Objects.requireNonNull`-Aufrufe. Wird z.&nbsp;B. `null` für eine der Maps übergeben,
schlägt das nicht hier fehl, sondern erst später bei einem Zugriff über die generierten
Record-Zugriffsmethoden (z.&nbsp;B. `tree.elementHeaders().get(element)` in `XmlViewer`
würde eine `NullPointerException` werfen, wenn `elementHeaders` selbst `null` wäre).

# Methoden

`RenderedTree` deklariert selbst keine eigenen Methoden im Quelltext. Der Compiler
generiert für einen `record` automatisch:

- `Div root()` — Rückgabewert kann `null` sein, falls beim Aufbau `null` übergeben wurde
  (siehe oben); in der Praxis im Projekt immer ein echter `Div` (Wurzel-Container des
  gerenderten Baums oder des leeren Platzhalters).
- `Map<Element, Div> elementHeaders()` — Abbildung Element → Start-Tag-Zeile, für
  Highlight und Scroll. Rückgabewert kann theoretisch `null` sein (keine Prüfung),
  praktisch stets eine (ggf. leere) Map.
- `Map<Element, Div> childContainers()` — Abbildung Element → Kinder-Container, für
  Ein-/Ausklappen.
- `Map<Element, Div> endTags()` — Abbildung Element → End-Tag-Zeile (wird beim
  Zuklappen mit-versteckt).
- `Map<Element, Span> toggles()` — Abbildung Element → Aufklapp-Dreieck (Marker-Zustand
  aktualisieren).
- `List<SearchableToken> tokens()` — alle durchsuchbaren [SearchableToken](/api-reference/searchable-token.md)s
  in Dokumentreihenfolge.
- `equals(Object)`, `hashCode()`, `toString()` — Standard-Record-Implementierungen
  (feldbasierter Vergleich/Hash über alle sechs Komponenten; da `Div`/`Span` als
  Vaadin-Komponenten typischerweise Identitäts-`equals` verwenden, entspricht der
  Record-`equals` effektiv einem Identitätsvergleich der enthaltenen Komponenten).

Keine dieser Zugriffsmethoden wirft eine Exception; sie geben lediglich das gespeicherte
Feld zurück (kein zusätzlicher Code im Methodenkörper, da automatisch generiert).

# Citations

[1] web-common/src/main/java/de/makno/web/common/component/xmlviewer/RenderedTree.java
