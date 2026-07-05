---
type: API Reference
title: MatchLabelFormatter
description: Funktionales Interface zur freien Formatierung des Treffer-Labels im SearchNavigator.
resource: web-common/src/main/java/de/makno/web/common/component/navigation/MatchLabelFormatter.java
tags: [api-reference, navigation, functional-interface]
timestamp: '2026-07-07T10:00:00+02:00'
---

# Überblick

`MatchLabelFormatter` ist ein mit `@FunctionalInterface` annotiertes, `Serializable`
funktionales Interface mit genau einer abstrakten Methode (`format`). Es erlaubt der
einbettenden Anwendung, das Anzeigeformat des Treffer-Labels im
[SearchNavigator](/components/search-navigator.md) frei zu bestimmen (z. B. „12/66",
„Treffer 12 von 66" oder lokalisierte Varianten), ohne die Komponente selbst zu ändern —
ein Beispiel für die im Projekt bevorzugte Entkopplung über kleine funktionale Interfaces
statt konkreter Implementierungen (siehe [Design-Regeln](/architecture/design-rules.md)).

Die Klasse ist zustandslos (keine Felder) — Thread-Safety-Fragen stellen sich nur für die
jeweilige Lambda-/Methoden-Implementierung, die die Anwendung übergibt (z. B. der
`DEFAULT_LABEL_FORMATTER` in `SearchNavigator`, eine zustandslose Lambda-Ausdruck-Instanz).

Da es sich um ein Interface ohne Implementierung handelt, gibt es keine Konstruktoren zu
dokumentieren.

# Vererbungshierarchie

**Vorwärts (eigene Deklaration):** `public interface MatchLabelFormatter extends
Serializable`.

- **Erweitertes Interface:** `java.io.Serializable` — JDK-Standard-Interface (Marker-Interface,
  keine Methoden), kein Projekt-Typ, daher kein Cross-Link. Ermöglicht, dass eine
  vom Anwendungscode übergebene Lambda-Implementierung mit der Vaadin-Session serialisiert
  werden kann.

**Rückwärts (Abhängige):** Verifiziert per Grep auf `implements ... MatchLabelFormatter` bzw.
`extends MatchLabelFormatter` über den gesamten
`web-common/src/main/java/de/makno/web/common/component/`-Baum — **kein Treffer** durch eine
benannte Klasse. Es gibt keine projektinterne Klasse, die dieses funktionale Interface
formal implementiert; `SearchNavigator` hält lediglich eine anonyme Lambda-Instanz als
`DEFAULT_LABEL_FORMATTER`-Konstante (Verwendung als Wert, keine Vererbungsbeziehung — siehe
[Verwendungskontext](#verwendungskontext) unten).

# Methoden

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

# Verwendungskontext

`SearchNavigator.setLabelFormatter(MatchLabelFormatter)` nimmt eine Implementierung entgegen
und ruft `format(matchCount, currentPosition)` bei jeder Treffer-Änderung erneut auf (siehe
[SearchNavigator](/api-reference/search-navigator.md#methoden), Methode `update`). Der
Standard-Formatter erzeugt das Format „aktuell/gesamt" (z. B. „12/66").

# Citations

[1] web-common/src/main/java/de/makno/web/common/component/navigation/MatchLabelFormatter.java
