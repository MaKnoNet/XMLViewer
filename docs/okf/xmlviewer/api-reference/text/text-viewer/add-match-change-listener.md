---
type: API Reference
title: TextViewer.addMatchChangeListener(...)
description: Methode addMatchChangeListener von TextViewer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/text/TextViewer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `public Registration addMatchChangeListener(ComponentEventListener<MatchChangeEvent> listener)` *(implements `MatchNavigable`)*


| Parameter | Typ | null-erlaubt | Verifikation |
|---|---|---|---|
| `listener` | `ComponentEventListener<MatchChangeEvent>` | im `TextViewer`-Rumpf nicht geprüft | Wird direkt an Vaadins `addListener(MatchChangeEvent.class, listener)` weitergereicht (geerbte Vaadin-Infrastruktur, kein eigener Null-Check in `TextViewer`). |

Rückgabewert: `Registration`, laut Vaadin-Vertrag nie `null` (Standardverhalten von
`Component.addListener`). Keine expliziten `throw`-Statements im `TextViewer`-Rumpf.

# Citations

[1] [TextViewer (Übersicht)](./text-viewer.md)
