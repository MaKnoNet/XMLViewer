---
type: API Reference
title: SearchableToken
description: Package-private Record, der einen durchsuchbaren Text-Span mit seinem Klartext und dem besitzenden Element bündelt.
resource: web-common/src/main/java/de/makno/web/common/component/xmlviewer/SearchableToken.java
tags: [api-reference, xmlviewer, record, search]
timestamp: '2026-07-07T10:00:00+02:00'
---

# Überblick


`SearchableToken` ist ein package-private `record`, der einen gerenderten `Span`
zusammen mit dessen ursprünglichem Klartext und dem besitzenden `org.jdom2.Element`
bündelt. Er implementiert `Serializable` (explizite `serialVersionUID = 1L`). Der
Klartext wird bewusst getrennt vom `Span` gehalten, weil die Suchlogik den Span-Inhalt
temporär in mehrere Treffer-/Nicht-Treffer-Teilspans zerlegt und danach aus `text` wieder
herstellt — `text` ist damit die "Quelle der Wahrheit", der `Span`-Inhalt ist ein
veränderlicher Rendering-Zustand.

**Thread-Safety:** Der Record ist unveränderlich (keine Setter, alle Komponenten `final`).
Der referenzierte `Span` ist eine Vaadin-Komponente und damit an die Session/den
UI-Thread des [XmlViewer](/api-reference/xmlviewer/xml-viewer/xml-viewer.md) gebunden, der ihn erzeugt hat —
kein geteilter Zustand zwischen Sessions.

# Felder

Felder = Record-Komponenten, siehe [Konstruktor](./constructor.md) (`span`, `text`, `owner`).

# Thread-Safety

**Der Record selbst ist unveränderlich** (verifiziert: alle drei Komponenten sind `final`
Record-Felder ohne Setter). Der referenzierte `Span` ist eine Vaadin-Flow-Komponente und damit
an die Session/den UI-Thread des erzeugenden [XmlViewer](/api-reference/xmlviewer/xml-viewer/xml-viewer.md)
gebunden — der `Span`-Inhalt selbst ist veränderlicher Rendering-Zustand (wird von der
Suchlogik temporär in Treffer-/Nicht-Treffer-Teilspans zerlegt), aber diese Mutation erfolgt
stets im an die Session gebundenen Thread, nie parallel. Kein geteilter Zustand zwischen
Sessions, da jede Session ihre eigenen `SearchableToken`-Instanzen mit eigenen `Span`-Objekten
erhält.

# Serialisierung

**`Serializable`** (verifiziert: `implements Serializable` in der Record-Deklaration), mit
explizit gesetzter `private static final long serialVersionUID = 1L;`. Diese explizite ID
stabilisiert die Serialisierungskompatibilität über Code-Änderungen hinweg. Der referenzierte
`Span` (Vaadin-Flow-Komponente) sowie `String` und `Element` (JDOM2) sind ihrerseits
serialisierbar, sodass der gesamte Record im Rahmen der Vaadin-Session-Serialisierung
mitgeschrieben werden kann.

# equals/hashCode/toString

**Komponentenbasierte Semantik** — Records generieren `equals`/`hashCode`/`toString`
automatisch aus allen drei Komponenten (`span`, `text`, `owner`). Keine dieser Methoden ist im
Quellcode manuell überschrieben (verifiziert). In der Praxis hat `equals`/`hashCode` hier wenig
Relevanz für den `span`/`owner`-Anteil, da Vaadin-`Span`-Komponenten und JDOM2-`Element`-Objekte
keine sinnvolle Werte-Gleichheit definieren (Vergleich faktisch über Identität) — zwei
`SearchableToken`-Instanzen sind daher faktisch nur dann `equals`, wenn `span` und `owner`
exakt dieselben Objektreferenzen sind (unabhängig vom textuell identischen `text`).

# Vererbungshierarchie


**Vorwärts (eigene Deklaration):** `record SearchableToken(Span span, String text, Element
owner) implements Serializable` (package-private).

- **Superklasse:** implizit `java.lang.Record` — JDK-Typ, kein Cross-Link.
- **Interfaces:**
  - `java.io.Serializable` — JDK-Standard-Interface (Marker-Interface, keine Methoden), kein
    Projekt-Typ, daher kein Cross-Link.
- Records sind implizit `final` — es kann keine Subklasse geben.

**Rückwärts (Abhängige):** Verifiziert per Grep auf `extends SearchableToken` über den
gesamten `web-common/src/main/java/de/makno/web/common/component/`-Baum — **kein Treffer**
(erwartungsgemäß, Records können nicht erweitert werden). `SearchableToken` wird von
[XmlTreeRenderer](/api-reference/xmlviewer/xml-tree-renderer/xml-tree-renderer.md) erzeugt und ist Teil der Token-Liste
in [RenderedTree](/api-reference/xmlviewer/rendered-tree/rendered-tree.md) (Assoziation, nicht Vererbung).

# Konstruktoren

- [siehe constructor.md](./constructor.md)

# Methoden


# Citations


[1] web-common/src/main/java/de/makno/web/common/component/xmlviewer/SearchableToken.java
