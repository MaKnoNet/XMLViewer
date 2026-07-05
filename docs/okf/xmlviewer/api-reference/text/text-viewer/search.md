---
type: API Reference
title: TextViewer.search(...)
description: Methode search von TextViewer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/text/TextViewer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `public void search(String query)` *(implements `MatchNavigable`)*


| Parameter | Typ | null-erlaubt | Verifikation |
|---|---|---|---|
| `query` | `String` | verhält sich wie null-erlaubt aus Sicht von `TextViewer`, da die Methode `query` ungeprüft an `searchController.search(query)` weiterreicht (Zeile 175) | Kein eigener Null-Check in `TextViewer`; das tatsächliche Verhalten bei `null` hängt von `SearchController.search(String)` ab (außerhalb des dokumentierten Scopes dieser Datei, siehe [Such-Engine](/architecture/search-engine.md)). Der Klassen-Javadoc von `TextViewer` behauptet „Leerer/`null`-Text löscht die Suche" — das ist eine Aussage über `SearchController`s Verhalten, nicht direkt im Rumpf von `TextViewer.search` verifizierbar. |

Rückgabewert: `void`. Keine Exceptions im Rumpf von `TextViewer` selbst.

# Citations

[1] [TextViewer (Übersicht)](./text-viewer.md)
