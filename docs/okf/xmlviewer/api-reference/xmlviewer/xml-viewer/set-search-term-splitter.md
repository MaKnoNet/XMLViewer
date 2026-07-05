---
type: API Reference
title: XmlViewer.setSearchTermSplitter(...)
description: Methode setSearchTermSplitter von XmlViewer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/xmlviewer/XmlViewer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `public void setSearchTermSplitter(SearchTermSplitter splitter)`


- `splitter` — **null-erlaubt: nein**, verifiziert durch
  `this.searchTermSplitter = Objects.requireNonNull(splitter, "splitter");` — dies ist ein
  expliziter Fail-Fast-Check.
- **Exceptions: wirft `NullPointerException` mit Nachricht `"splitter"`, wenn `splitter ==
  null`.** Dies ist im Javadoc **nicht** dokumentiert (der Javadoc-Kommentar der Methode
  erwähnt nur das Verhalten bei aktiver Suche, keine Null-Behandlung) — siehe Diskrepanz
  unten.
- Rückgabewert: keiner.

# Citations

[1] [XmlViewer (Übersicht)](./xml-viewer.md)
