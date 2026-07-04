---
type: Architecture Concept
title: Geteilte Such-Engine (component.search)
description: Eine Textsuche für alle Viewer — SearchController & Co. im Package de.makno.web.common.component.search, konsumiert über schlanke Abstraktionen statt konkreter Typen.
resource: web-common/src/main/java/de/makno/web/common/component/search/SearchController.java
tags: [architecture, search, dry, records]
timestamp: '2026-07-04T16:30:00+02:00'
---

# Überblick

Die Textsuche ist EINMAL implementiert und wird von [XmlViewer](/components/xmlviewer.md) und
[TextViewer](/components/textviewer.md) geteilt (DRY). [CodeViewer](/components/codeviewer.md)
implementiert dasselbe `MatchNavigable`-Interface, lässt aber CodeMirror selbst suchen.

# Schema

| Baustein | Aufgabe |
|---|---|
| `SearchController` | Treffer finden/zählen, navigieren, Reveal ausführen, Änderungen melden |
| `SearchToken` / `TokenMatch` | Records: durchsuchbares Token (Text + Reveal-Aktion) bzw. Treffer-Offset |
| `SearchHighlightRenderer` / `FrontendSearchHighlighter` | Treffer-Zeichnen entkoppelt; Standard lagert es ins Frontend aus |
| `SearchTermSplitter` | Funktionales Interface: Suchtext → Begriffe (Trennzeichen frei wählbar) |

# Entwurfsentscheidungen

- **Suche ohne Such-UI:** Die Komponenten enthalten bewusst keine Suchoberfläche; die
  Anwendung bindet ein eigenes Feld an oder nutzt den
  [SearchNavigator](/components/search-navigator.md).
- **Mehrere Begriffe = ODER-Suche**, Trennung standardmäßig an Whitespace, über
  `SearchTermSplitter` frei änderbar (z. B. Komma oder „nicht trennen").
- **Treffer-Rendering ist Strategie:** `SearchHighlightRenderer` entkoppelt das Zeichnen;
  der Standard (`FrontendSearchHighlighter`) verlagert es in den Browser — Details in
  [Frontend-Integration](/architecture/frontend-integration.md).

# Citations

[1] [README – Architektur-Tabelle, search-Package](https://github.com/MaKnoNet/XMLViewer/blob/master/README.md)
