---
type: Vaadin Component
title: TextViewer
description: Read-only Klartext-Anzeige mit Zeilennummern, Zeilen-Highlight und umschaltbarem Umbruch; nutzt dieselbe Such-Engine und denselben SearchNavigator wie XmlViewer.
resource: web-common/src/main/java/de/makno/web/common/component/text/TextViewer.java
tags: [component, vaadin, text, search]
timestamp: '2026-07-04T16:30:00+02:00'
---

# Überblick

`TextViewer` ist die Schwester-Komponente von [XmlViewer](/components/xmlviewer.md) für
unformatierten Klartext: Zeilennummern, Hervorheben einzelner Zeilen, umschaltbarer
Zeilenumbruch. Die Suche/Navigation ist identisch, weil beide Komponenten die geteilte
[Such-Engine](/architecture/search-engine.md) in `de.makno.web.common.component.search`
verwenden und `MatchNavigable` implementieren — der
[SearchNavigator](/components/search-navigator.md) funktioniert unverändert an beiden.

# Besonderheiten

- Suchtreffer werden wie beim XmlViewer über das geteilte Frontend-Modul
  `search/search-highlighter.js` (CSS Custom Highlight API) gezeichnet — kein DOM-Knoten pro
  Treffer, siehe [Frontend-Integration](/architecture/frontend-integration.md).
- `TextCssClasses` bündelt die CSS-Klassennamen der Komponente (Wurzel, Zeilen-Gutter,
  Zeileninhalt, Umbruch-Modifier); der `SEARCH_TOKEN`-Klassenname muss exakt dem
  `TOKEN_SELECTOR` in `search/search-highlighter.js` entsprechen, weil das Frontend darüber
  die Treffer-Knoten positionsbasiert findet.
- Demo unter `/text` (`app.TextDemoView` mit `app.SampleTextFactory`).

# Citations

[1] [README – Schwester-Komponente TextViewer](https://github.com/MaKnoNet/XMLViewer/blob/master/README.md)
