---
type: API Reference
title: MatchNavigable.search(...)
description: Methode search von MatchNavigable - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/navigation/MatchNavigable.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `void search(String query)`


- **Parameter:** `query` (`String`) — null-erlaubt: **ja**. Verifizierungsbasis: Der Javadoc-Kommentar
  sagt explizit „Leerer/`null` Text löscht die Suche" — d. h. `null` ist ein gültiger, definierter
  Eingabewert auf Interface-Ebene. Da es sich um ein Interface ohne Methodenkörper handelt, kann das
  tatsächliche Null-Handling nur pro Implementierung verifiziert werden (hier nicht Gegenstand); der
  Vertrag selbst verlangt aber ausdrücklich Toleranz gegenüber `null`.
- **Rückgabewert:** keiner (`void`).
- **Exceptions:** Das Interface deklariert keine `throws`-Klausel und enthält (als Interface) keinen
  Methodenkörper, also keine `throw`-Anweisungen zu verifizieren. Laut Vertrag löst ein leerer oder
  `null`-Text die Suche, statt eine Exception zu werfen.
- **Semantik:** Sucht den angegebenen Text, markiert alle Treffer und springt zum ersten Treffer.

# Citations

[1] [MatchNavigable (Übersicht)](./match-navigable.md)
