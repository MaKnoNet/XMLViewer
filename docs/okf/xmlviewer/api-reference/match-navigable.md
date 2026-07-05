---
type: API Reference
title: MatchNavigable
description: Zentrales Entkopplungs-Interface für durchsuchbare, treffer-navigierbare Quellen (Dependency Inversion zwischen SearchNavigator und den konkreten Viewer-Komponenten).
resource: web-common/src/main/java/de/makno/web/common/component/navigation/MatchNavigable.java
tags: [api-reference, navigation, interface, dependency-inversion]
timestamp: '2026-07-07T10:00:00+02:00'
---

# Überblick

`MatchNavigable` ist ein reines Interface (kein Zustand, keine Implementierung) und das
zentrale Entkopplungs-Element des `navigation`-Pakets: Es abstrahiert „eine Quelle mit
durchsuchbarem/durchlaufbarem Treffer-Stand" von der konkreten UI-Komponente. Konsument ist
[SearchNavigator](/components/search-navigator.md), der ausschließlich gegen dieses Interface
programmiert und den konkreten Komponententyp nicht kennt (Dependency Inversion, siehe
[Design-Regeln](/architecture/design-rules.md)).

Da das Interface `Serializable` erweitert, kann eine `SearchNavigator`-Instanz ihre
`MatchNavigable`-Quelle mit der Vaadin-Session serialisieren (Passivierung/Cluster). Das
Interface selbst schreibt keine Thread-Safety vor — es beschreibt nur den Vertrag; die
tatsächliche Thread-Safety hängt von der jeweiligen Implementierung ab. Implementierungen sind
in der Praxis Vaadin-Komponenten und folgen damit deren Single-Session-/Single-Thread-Modell
(nur aus dem UI-Thread mit Session-Lock aufrufen).

Bekannte Implementierungen im Projekt (verifiziert per Grep auf `implements MatchNavigable`
bzw. Referenz auf das Interface):
[XmlViewer](/components/xmlviewer.md), [TextViewer](/components/textviewer.md) und
[CodeViewer](/components/codeviewer.md).

Da `MatchNavigable` ein Interface ohne Implementierung ist, gibt es keine Konstruktoren zu
dokumentieren.

# Methoden

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

## `void nextMatch()`

- **Parameter:** keine.
- **Rückgabewert:** keiner (`void`).
- **Exceptions:** keine deklariert; kein Methodenkörper auf Interface-Ebene zu prüfen.
- **Semantik:** Springt zum nächsten Suchtreffer, umlaufend (nach dem letzten Treffer wieder beim ersten).

## `void previousMatch()`

- **Parameter:** keine.
- **Rückgabewert:** keiner (`void`).
- **Exceptions:** keine deklariert.
- **Semantik:** Springt zum vorherigen Suchtreffer, umlaufend (vor dem ersten Treffer wieder beim letzten).

## `int getMatchCount()`

- **Parameter:** keine.
- **Rückgabewert:** `int` — Anzahl der aktuellen Suchtreffer. Primitiver Typ, kann nicht `null` sein.
- **Exceptions:** keine deklariert.

## `int getCurrentMatchIndex()`

- **Parameter:** keine.
- **Rückgabewert:** `int` — 0-basierter Index des aktuellen Treffers, oder `-1`, wenn kein Treffer aktiv
  ist. Primitiver Typ, kann nicht `null` sein; `-1` ist der definierte Sentinel-Wert für „kein aktiver
  Treffer".
- **Exceptions:** keine deklariert.

## `Registration addMatchChangeListener(ComponentEventListener<MatchChangeEvent> listener)`

- **Parameter:** `listener` (`ComponentEventListener<MatchChangeEvent>`) — null-erlaubt: nicht durch das
  Interface spezifiziert (kein Javadoc-Hinweis, kein `@Nullable`/`@NonNull`). Da es sich um ein Interface
  handelt, ist das tatsächliche Verhalten bei `null` implementierungsabhängig und hier nicht verifizierbar.
- **Rückgabewert:** `Registration` (Vaadin-Typ). Der Javadoc macht keine Aussage über Nullability des
  Rückgabewerts; üblich für Vaadin-`Registration`-APIs ist ein nicht-`null`-Rückgabewert, mit dem der
  Aufrufer die Registrierung später lösen kann (siehe `SearchNavigator.onDetach`, das den Rückgabewert
  ungeprüft in einem Feld hält und `.remove()` darauf aufruft).
- **Exceptions:** keine deklariert.
- **Semantik:** Registriert einen Listener, der bei jeder Änderung der Suchtreffer oder der
  Treffer-Navigation gefeuert wird (siehe [MatchChangeEvent](/api-reference/match-change-event.md)).

# Citations

[1] web-common/src/main/java/de/makno/web/common/component/navigation/MatchNavigable.java
