---
type: API Reference
title: TextViewer.splitLines(...)
description: Methode splitLines von TextViewer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/text/TextViewer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `private static List<String> splitLines(String text)`


| Parameter | Typ | null-erlaubt | Verifikation |
|---|---|---|---|
| `text` | `String` | **nein, nicht in der Praxis** | Kein expliziter Null-Check im Rumpf; ruft direkt `text.replace(...)` auf. Bei `text == null` würde eine `NullPointerException` an dieser Stelle geworfen. In der Praxis unkritisch, da `render()` diese Methode nur aufruft, wenn `text.isEmpty()` bereits `false` war — das Feld `text` der Klasse ist durch `setText`/den Konstruktor nie `null` (siehe oben). Wird also nie mit `null` aufgerufen, hat aber selbst **keine** Absicherung dagegen. |

Rückgabewert: `List<String>`, nie `null` (mindestens ein Element, da `String.split(..., -1)` bei
leerem String `[""]` liefert — praktisch aber ungenutzt, weil der Aufrufer den leeren Fall vorher
abfängt). CRLF/CR werden zu LF vereinheitlicht; `limit == -1` erhält abschließende Leerzeilen. Keine
deklarierten Exceptions; potenzielle `NullPointerException` bei `text == null` (nicht erreichbar über
den öffentlichen API-Pfad).

# Citations

[1] [TextViewer (Übersicht)](./text-viewer.md)
