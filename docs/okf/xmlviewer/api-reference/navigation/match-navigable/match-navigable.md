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

# Felder

Keine Felder (Interface). `MatchNavigable` deklariert ausschließlich fünf abstrakte Methoden
(`search`, `nextMatch`, `previousMatch`, `getMatchCount`, `getCurrentMatchIndex`) sowie
`addMatchChangeListener`, keinerlei Zustand (verifiziert: keine Feld-Deklarationen im
Quellcode).

# Thread-Safety

Das Interface selbst schreibt keine Thread-Safety vor — es beschreibt nur den Methodenvertrag.
Die tatsächliche Thread-Safety hängt von der jeweiligen Implementierung ab. Implementierungen
sind laut Javadoc (Zeile 16-17) „in der Praxis ohnehin Vaadin-Komponenten" und folgen damit
deren Single-Session-/Single-Thread-Modell: nur aus dem UI-Thread mit Session-Lock aufrufen,
eine Instanz nicht zwischen Sessions teilen. Das ist eine dokumentierte Erwartung an
Implementierer, kein vom Interface selbst erzwungener Vertrag.

# Serialisierung

`MatchNavigable` erweitert `java.io.Serializable` (direkt, verifiziert Zeile 19) und deklariert
keinen eigenen `serialVersionUID`-Wert — als Interface ohne Felder ist das auch nicht sinnvoll
möglich; die `serialVersionUID`-Verantwortung liegt bei der jeweiligen implementierenden Klasse
(z. B. `XmlViewer`, `TextViewer`, `CodeViewer`). Zweck der `Serializable`-Erweiterung laut
Javadoc (Zeile 15-17): eine `SearchNavigator`-Instanz kann ihre `MatchNavigable`-Quelle mit der
Vaadin-Session serialisieren (Passivierung/Cluster).

# equals/hashCode/toString

Kein Override möglich oder vorhanden — Interfaces können diese `Object`-Methoden nicht
implementieren (nur Default-Methoden wären möglich, hier keine vorhanden, verifiziert). Für
eine konkrete Implementierung gilt die **Identitätssemantik von `java.lang.Object`**, sofern
die jeweilige implementierende Klasse (`XmlViewer`, `TextViewer`, `CodeViewer`) diese Methoden
nicht selbst überschreibt — das wird hier nicht mitverifiziert, da außerhalb des Scopes dieser
Datei.

# Vererbungshierarchie


**Vorwärts (eigene Deklaration):** `public interface MatchNavigable extends Serializable`.

- **Erweitertes Interface:** `java.io.Serializable` — JDK-Standard-Interface (Marker-Interface,
  keine Methoden), kein Projekt-Typ, daher kein Cross-Link. Ermöglicht, dass eine
  `SearchNavigator`-Instanz ihre `MatchNavigable`-Quelle mit der Vaadin-Session
  serialisiert (siehe Überblick).

**Rückwärts (Abhängige):** Verifiziert per Grep auf `implements ... MatchNavigable` über den
gesamten `web-common/src/main/java/de/makno/web/common/component/`-Baum — **drei Treffer**,
alle Konsumenten des zentralen Entkopplungs-Interfaces:

- [XmlViewer](/api-reference/xmlviewer/xml-viewer/xml-viewer.md) — `public class XmlViewer extends Composite<Div>
  implements HasSize, HasStyle, MatchNavigable`.
- [TextViewer](/api-reference/text/text-viewer/text-viewer.md) — `public class TextViewer extends Composite<Div>
  implements HasSize, HasStyle, MatchNavigable`.
- [CodeViewer](/api-reference/code/code-viewer/code-viewer.md) — `public class CodeViewer extends Div implements
  MatchNavigable`.

Damit ist `MatchNavigable` das mit Abstand am weitesten verbreitete projektinterne Interface
in diesem Package-Baum — die zentrale Dependency-Inversion-Nahtstelle zwischen
[SearchNavigator](/api-reference/navigation/search-navigator/search-navigator.md) und den drei konkreten Viewer-Komponenten.

# Konstruktoren


# Methoden

- [``void search(String query)``](./search.md)
- [``void nextMatch()``](./next-match.md)
- [``void previousMatch()``](./previous-match.md)
- [``int getMatchCount()``](./get-match-count.md)
- [``int getCurrentMatchIndex()``](./get-current-match-index.md)
- [``Registration addMatchChangeListener(ComponentEventListener<MatchChangeEvent> listener)``](./add-match-change-listener.md)

# Citations


[1] web-common/src/main/java/de/makno/web/common/component/navigation/MatchNavigable.java
