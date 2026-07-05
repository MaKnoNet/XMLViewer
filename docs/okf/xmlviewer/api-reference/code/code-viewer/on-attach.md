---
type: API Reference
title: CodeViewer.onAttach(...)
description: Methode onAttach von CodeViewer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/code/CodeViewer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `protected void onAttach(AttachEvent attachEvent)`


Vaadin-Lifecycle-Hook, `@Override`. Ruft `super.onAttach(attachEvent)` und dann
`callJs("create", text, effectiveLanguageId(), dark, wrap, showLineNumbers)` auf — baut den
clientseitigen Editor aus dem aktuellen Serverzustand neu auf.

| Parameter | Typ | null-erlaubt | Verifikation |
|---|---|---|---|
| `attachEvent` | `AttachEvent` | kein eigener Null-Check | Wird nur an `super.onAttach(...)` weitergereicht; Vaadin-Framework ruft diese Methode nie mit `null` auf. |

`void`, keine expliziten Exceptions im `CodeViewer`-Rumpf.

# Citations

[1] [CodeViewer (Übersicht)](./code-viewer.md)
