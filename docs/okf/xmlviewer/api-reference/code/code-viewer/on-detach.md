---
type: API Reference
title: CodeViewer.onDetach(...)
description: Methode onDetach von CodeViewer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/code/CodeViewer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `protected void onDetach(DetachEvent detachEvent)`


Vaadin-Lifecycle-Hook, `@Override`. Führt Best-effort-Cleanup aus: `getElement().executeJs(...)` ruft
`window.MaknoCodeViewer.destroy(this)` clientseitig auf, falls vorhanden (`&&`-Guard im JS selbst),
dann `super.onDetach(detachEvent)`.

| Parameter | Typ | null-erlaubt | Verifikation |
|---|---|---|---|
| `detachEvent` | `DetachEvent` | kein eigener Null-Check | Nur an `super.onDetach(...)` weitergereicht; Vaadin ruft nie mit `null` auf. |

`void`, keine expliziten Exceptions im `CodeViewer`-Rumpf. Laut Kommentar im Code ist dieser Aufruf
bewusst "Best-effort" — falls er beim Detach nicht mehr ankommt, räumt die JS-Registry detachte Hosts
ohnehin lazy per `isConnected`-Prüfung auf.

# Citations

[1] [CodeViewer (Übersicht)](./code-viewer.md)
