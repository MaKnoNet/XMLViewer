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

# Felder

Keine Felder (Interface). `MatchLabelFormatter` deklariert ausschließlich die abstrakte
Methode `format(int, int)`, keinerlei Zustand (verifiziert: keine Feld-Deklarationen im
Quellcode).

# Thread-Safety

Das Interface selbst schreibt keine Thread-Safety vor — es beschreibt nur den Methodenvertrag
`format(int, int)`. Da es sich um ein `@FunctionalInterface` handelt, hängt die tatsächliche
Thread-Safety von der jeweils übergebenen Lambda-/Methodenreferenz-Implementierung ab. Der im
Projekt verwendete `DEFAULT_LABEL_FORMATTER` in `SearchNavigator` ist eine zustandslose
Lambda-Instanz (`(matchCount, currentPosition) -> currentPosition + "/" + matchCount`) und
damit ohne weiteres aus mehreren Threads sicher aufrufbar; das ist jedoch eine Eigenschaft der
konkreten Implementierung, kein vom Interface erzwungener Vertrag.

# Serialisierung

`MatchLabelFormatter` erweitert `java.io.Serializable` (direkt, verifiziert Zeile 12) und
deklariert keinen eigenen `serialVersionUID`-Wert — als Interface ohne Felder ist das auch
nicht sinnvoll möglich; die `serialVersionUID`-Verantwortung liegt bei der jeweiligen
konkreten Lambda-/Klassen-Implementierung, die das Interface implementiert. Der Zweck der
`Serializable`-Erweiterung: eine vom Anwendungscode übergebene Formatter-Implementierung kann
mit der Vaadin-Session serialisiert werden (Passivierung/Cluster).

# equals/hashCode/toString

Kein Override möglich oder vorhanden — Interfaces können diese `Object`-Methoden nicht
implementieren (nur Default-Methoden wären möglich, hier keine vorhanden, verifiziert). Für
eine konkrete Implementierung (z. B. eine Lambda) gilt die **Identitätssemantik von
`java.lang.Object`**, sofern die Implementierung diese Methoden nicht selbst überschreibt —
Lambda-Ausdrücke tun dies nie.

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

# Konstruktoren


# Methoden

- [``String format(int matchCount, int currentPosition)``](./format.md)

# Citations


[1] web-common/src/main/java/de/makno/web/common/component/navigation/MatchLabelFormatter.java
