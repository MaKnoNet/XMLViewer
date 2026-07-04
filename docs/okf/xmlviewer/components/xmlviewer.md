---
type: Vaadin Component
title: XmlViewer
description: Vaadin-Flow-Komponente, die einen org.jdom2.Element-Baum als eingefärbte, einrückende Quelltext-Ansicht mit Klappen, Hervorheben und Suche rendert.
resource: web-common/src/main/java/de/makno/web/common/component/xmlviewer/XmlViewer.java
tags: [component, vaadin, xml, jdom2, search]
timestamp: '2026-07-04T16:30:00+02:00'
---

# Überblick

`XmlViewer` nimmt direkt ein `org.jdom2.Element` entgegen und stellt es wie das
Syntax-Highlighting eines Editors dar: Tag-Namen, Attributnamen, Attributwerte, Text und
Kommentare je andersfarbig, Element-Blöcke ein-/ausklappbar. Die Komponente ist Spring-frei
und implementiert [MatchNavigable](/components/search-navigator.md), sodass die Suchnavigation
ohne Kopplung an den konkreten Typ funktioniert.

Interne Verantwortlichkeiten sind getrennt: `XmlTreeRenderer` (rekursives Rendern in
`Div`/`Span`), die geteilte Such-Engine (siehe [Such-Engine](/architecture/search-engine.md)),
`CssClasses` (zentrale Klassennamen, keine Magic-Strings), schlankes `XmlViewer` als API.

# Schema

| Methode | Zweck |
|---|---|
| `new XmlViewer()` / `new XmlViewer(Element)` | Komponente, optional direkt mit Wurzelelement |
| `setRoot(Element)` | Wurzelelement setzen und neu rendern (`null` leert die Ansicht) |
| `highlight(Element)` / `clearHighlight()` | Element hervorheben (Identitätsvergleich!), Vorfahren aufklappen, hinscrollen |
| `expandAll()` / `collapseAll()` / `setCollapsible(boolean)` | Klapp-Verhalten |
| `search(String)` / `nextMatch()` / `previousMatch()` / `clearSearch()` | Textsuche mit umlaufender Navigation |
| `getMatchCount()` / `getCurrentMatchIndex()` / `addMatchChangeListener(...)` | Trefferstand + Events für externe Zähler |
| `setSearchCaseSensitive(boolean)` | Groß-/Kleinschreibung (Standard: aus) |
| `setSearchTermSplitter(SearchTermSplitter)` | Trennverhalten der Suchbegriffe frei bestimmbar (Standard: Whitespace) |

# Examples

```java
XmlViewer viewer = new XmlViewer(wurzel);
viewer.setSizeFull();
add(viewer);
viewer.highlight(einElement);
viewer.search("EUR");
```

# Interne Bausteine

`XmlTreeRenderer` liefert sein Ergebnis als `RenderedTree`-Record (Wurzel-`Div`, je eine
identitätsbasierte `Element→Div`-Abbildung für Start-Tag-Zeile/Kinder-Container/End-Tag-Zeile,
`Element→Span` für die Klapp-Dreiecke, plus die Liste der `SearchableToken`s in
Dokumentreihenfolge). `SearchableToken` bündelt den gerenderten `Span`, dessen Klartext separat
(die Suche zerlegt den Span-Inhalt temporär in Treffer-/Nicht-Treffer-Teilspans und stellt ihn
danach aus dem Klartext wieder her) und das besitzende `Element` (zum Aufklappen der Vorfahren
bei einem Treffer).

# Besonderheiten

- **Sicher:** Rendering über echte Vaadin-Komponenten (`setText`), XML-Sonderzeichen korrekt
  escaped, kein innerHTML/XSS.
- **Rendering-Prinzip:** pro `Element` eine Start-Tag-Zeile, ein eingerückter Kinder-Container,
  eine End-Tag-Zeile; `IdentityHashMap<Element, Div>` für `highlight`, Token-Liste für die Suche.
- **Styling komplett per CSS Custom Properties** (`--xmlviewer-*`), keine Farben im Java-Code —
  Details siehe [Frontend-Integration](/architecture/frontend-integration.md).

# Citations

[1] [README – API-Tabelle und Schnellstart](https://github.com/MaKnoNet/XMLViewer/blob/master/README.md)
