---
type: API Reference
title: TextViewer.scrollTo(...)
description: Methode scrollTo von TextViewer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/text/TextViewer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `private void scrollTo(Component target)`


| Parameter | Typ | Verifikation |
|---|---|---|
| `target` | `Component` | Kein expliziter Null-Check; ruft `target.getUI()` auf, was bei `target == null` eine `NullPointerException` würfe. Wird intern nur mit einem konkreten `Div` aus `lineElements` aufgerufen, nie mit `null`. |

Rückgabewert: `void`. Scrollt nur, wenn `target.getUI()` ein UI liefert (`Optional` ist nicht leer) —
im Unit-Test ohne Client-Anbindung ist das `Optional` leer, dann passiert nichts (kein Fehler). Keine
Exceptions im Normalfall.

# Citations

[1] [TextViewer (Übersicht)](./text-viewer.md)
