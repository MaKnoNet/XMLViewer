---
type: API Reference
title: XmlViewer.search(...)
description: Methode search von XmlViewer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/xmlviewer/XmlViewer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `@Override public void search(String query)`


- `query` — null-erlaubt: ja. Der Javadoc-Kommentar behauptet direkt: „Leerer/`null`-Text
  löscht die Suche." Verifiziert über die Delegation an
  [`SearchController.search(String)`](/api-reference/search/search-controller/search-controller.md): Dort wird
  `query` an `splitTerms` → `termSplitter.split(query)` weitergereicht. Der Standard-Splitter
  (`SearchController.DEFAULT_TERM_SPLITTER`) prüft selbst `if (query == null || query.isBlank())
  return List.of();` — bei leerer Trefferliste wird `matches = List.of()` gesetzt, was
  effektiv einer gelöschten Suche entspricht. **Die Javadoc-Behauptung ist zutreffend, aber nur
  weil der Standard-Splitter das selbst behandelt** — ein per `setSearchTermSplitter` gesetzter
  eigener Splitter, der bei `null` z.&nbsp;B. eine `NullPointerException` wirft, würde hier
  durchschlagen (siehe Diskrepanz-Hinweis unten).
- Rückgabewert: keiner.
- Exceptions: keine im `XmlViewer`-Methodenkörper selbst; abhängig vom aktiven
  `SearchTermSplitter` könnte ein defensiv nicht geschriebener eigener Splitter bei `null`
  scheitern (siehe [SearchTermSplitter](/api-reference/search/search-term-splitter/search-term-splitter.md)).

# Citations

[1] [XmlViewer (Übersicht)](./xml-viewer.md)
