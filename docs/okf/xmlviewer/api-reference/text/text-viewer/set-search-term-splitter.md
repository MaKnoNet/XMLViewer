---
type: API Reference
title: TextViewer.setSearchTermSplitter(...)
description: Methode setSearchTermSplitter von TextViewer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/text/TextViewer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `public void setSearchTermSplitter(SearchTermSplitter splitter)`


| Parameter | Typ | null-erlaubt | Verifikation |
|---|---|---|---|
| `splitter` | `SearchTermSplitter` | **nein** | Zeile 224: `this.searchTermSplitter = Objects.requireNonNull(splitter, "splitter");` — explizite Null-Prüfung. |

Rückgabewert: `void`. **Geworfene Exception:** `NullPointerException` (durch
`Objects.requireNonNull`) wenn `splitter == null` — Nachricht `"splitter"`. Dies ist der einzige
Setter der Klasse mit explizitem Null-Check; alle anderen `set*`-Methoden akzeptieren `null`
stillschweigend oder normalisieren es. Bei gültigem Splitter wird zusätzlich
`searchController.setTermSplitter(splitter)` aufgerufen — eine aktive Suche wird laut Javadoc "sofort
neu ausgeführt" (das eigentliche Neu-Ausführen passiert innerhalb von `SearchController`, außerhalb
des hier verifizierten Rumpfs).

# Citations

[1] [TextViewer (Übersicht)](./text-viewer.md)
