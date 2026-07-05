---
type: API Reference
title: MatchLabelFormatter.format(...)
description: Methode format von MatchLabelFormatter - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/navigation/MatchLabelFormatter.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `String format(int matchCount, int currentPosition)`


Einzige abstrakte Methode des funktionalen Interfaces (keine `default`- oder
`static`-Methoden vorhanden — verifiziert: die Datei enthält ausschließlich diese eine
Methodendeklaration).

- **Parameter:**
  - `matchCount` (`int`) — Anzahl der Treffer, `0` wenn keine vorhanden. Primitiver Typ, kann
    nicht `null` sein; null-erlaubt entfällt (kein Referenztyp).
  - `currentPosition` (`int`) — 1-basierte Position des aktuellen Treffers, `0` wenn kein
    Treffer aktiv ist. Primitiver Typ, kann nicht `null` sein.
- **Rückgabewert:** `String` — der anzuzeigende Text. Der Javadoc macht keine explizite Aussage
  zur Nullability des Rückgabewerts; da es sich um ein Interface ohne Methodenkörper handelt,
  ist das tatsächliche Verhalten (ob eine Implementierung `null` zurückgeben darf/kann)
  implementierungsabhängig und hier nicht verifizierbar. Der im Projekt genutzte
  Default-Formatter in `SearchNavigator`
  (`(matchCount, currentPosition) -> currentPosition + "/" + matchCount`) liefert durch
  String-Konkatenation immer einen nicht-`null`-Wert.
- **Exceptions:** Das Interface deklariert keine `throws`-Klausel; kein Methodenkörper auf
  Interface-Ebene zu prüfen.

# Citations

[1] [MatchLabelFormatter (Übersicht)](./match-label-formatter.md)
