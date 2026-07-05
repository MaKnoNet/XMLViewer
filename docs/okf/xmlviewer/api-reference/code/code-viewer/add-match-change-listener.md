---
type: API Reference
title: CodeViewer.addMatchChangeListener(...)
description: Methode addMatchChangeListener von CodeViewer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/code/CodeViewer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `public Registration addMatchChangeListener(ComponentEventListener<MatchChangeEvent> listener)` *(implements `MatchNavigable`)*


| Parameter | Typ | null-erlaubt | Verifikation |
|---|---|---|---|
| `listener` | `ComponentEventListener<MatchChangeEvent>` | im `CodeViewer`-Rumpf nicht geprüft | Direkt an Vaadins `addListener(MatchChangeEvent.class, listener)` weitergereicht (geerbte Framework-Infrastruktur, kein eigener Null-Check). |

Rückgabewert: `Registration`, laut Vaadin-Standardverhalten nie `null`. Keine expliziten
`throw`-Statements im `CodeViewer`-Rumpf.

# Citations

[1] [CodeViewer (Übersicht)](./code-viewer.md)
