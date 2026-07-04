---
type: Vaadin Component
title: SearchNavigator und MatchNavigable
description: Such-Pille (Eingabefeld + Treffer-Label + Vor/Zurück) und das entkoppelnde Interface MatchNavigable, über das alle Viewer-Komponenten suchbar werden.
resource: web-common/src/main/java/de/makno/web/common/component/navigation/SearchNavigator.java
tags: [component, vaadin, navigation, search, interface]
timestamp: '2026-07-04T16:30:00+02:00'
---

# Überblick

`SearchNavigator` ist die wiederverwendbare Such-UI: ein Eingabefeld, ein Treffer-Label
(Standardformat „12/66") und Vor-/Zurück-Buttons (nur bei Treffern aktiv). Er steuert ein
**`MatchNavigable`** — das zentrale Entkopplungs-Interface des Projekts: suchen, Treffer
durchlaufen, Stand abfragen. Konsumiert wird das Interface, nie der konkrete Komponententyp
(Dependency Inversion, siehe [Design-Regeln](/architecture/design-rules.md)).

Implementiert wird `MatchNavigable` von [XmlViewer](/components/xmlviewer.md),
[TextViewer](/components/textviewer.md) und [CodeViewer](/components/codeviewer.md).

# Schema

| Baustein | Aufgabe |
|---|---|
| `MatchNavigable` | Interface: suchen + Treffer durchlaufen + Stand abfragen |
| `MatchChangeEvent` | Event bei Änderung der Treffer/-navigation |
| `MatchLabelFormatter` | Funktionales Interface: Label-Format frei bestimmbar |
| `SearchNavigator` | Such-Pille; steuert ein beliebiges `MatchNavigable` |

# Examples

```java
XmlViewer viewer = new XmlViewer(wurzel);
SearchNavigator navigator = new SearchNavigator(viewer); // Suchfeld + „12/66" + ‹ / ›
add(navigator);
navigator.setLabelFormatter((count, position) -> "Treffer " + position + " von " + count);
```

# Citations

[1] [README – Architektur-Tabelle, navigation-Package](https://github.com/MaKnoNet/XMLViewer/blob/master/README.md)
