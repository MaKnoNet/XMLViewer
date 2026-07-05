---
type: API Reference
title: MatchChangeEvent
description: Vaadin-ComponentEvent, das eine MatchNavigable-Quelle bei jeder Änderung ihrer Suchtreffer oder Treffer-Navigation feuert — verifizierte Konstruktoren und Zugriffsmethoden.
resource: web-common/src/main/java/de/makno/web/common/component/navigation/MatchChangeEvent.java
tags: [api-reference, navigation, vaadin, event, search]
timestamp: '2026-07-07T10:00:00+02:00'
---

# Überblick


`MatchChangeEvent` ist eine `public class MatchChangeEvent extends
ComponentEvent<Component>`. Sie ist Teil des in
[SearchNavigator (Komponenten-Doku)](/components/search-navigator.md) beschriebenen
Zusammenspiels: implementierende Quellen wie [XmlViewer](/api-reference/xmlviewer/xml-viewer/xml-viewer.md),
[TextViewer](/api-reference/text/text-viewer/text-viewer.md) und `CodeViewer` feuern dieses Ereignis bei jeder
Änderung ihrer Suchtreffer/-navigation, und der
[SearchNavigator](/api-reference/navigation/search-navigator/search-navigator.md) hält darüber seine Anzeige synchron.

**Thread-Safety:** Die Instanz ist nach Konstruktion unveränderlich (alle drei Zusatzfelder
`final`, keine Setter) — insofern trivial thread-sicher zu lesen. Wie jedes Vaadin-`ComponentEvent`
wird sie jedoch innerhalb des Session-Locks der auslösenden Komponente erzeugt und verteilt;
kein eigenständiger Thread-Safety-Vertrag über den von Vaadins Event-Bus hinaus.

# Felder

| Feld | Typ | Bedeutung | null-erlaubt |
|---|---|---|---|
| `serialVersionUID` | `private static final long` | Serialisierungs-Versionskennung, explizit auf `1L` gesetzt (verifiziert). | entfällt (primitiv `long`) |
| `matchCount` | `private final int` | Anzahl der Treffer der aktuellen Suche, per Konstruktor gesetzt. | entfällt (primitiv `int`) |
| `currentMatchIndex` | `private final int` | 0-basierter Index des aktuellen Treffers, oder `-1`, wenn keiner aktiv ist. | entfällt (primitiv `int`) |
| `reset` | `private final boolean` | `true`, wenn die Suche wegen eines Inhaltswechsels (z. B. `setRoot`) zurückgesetzt wurde. Default über den kürzeren Konstruktor: `false`. | entfällt (primitiv `boolean`) |

# Thread-Safety

Kein eigenständiger Thread-Safety-Vertrag über den von Vaadins `ComponentEvent`-Mechanismus
hinaus (verifiziert: keine Synchronisation, keine `static`-Felder mit veränderlichem Zustand).
Die drei Zusatzfelder (`matchCount`, `currentMatchIndex`, `reset`) sind `final` und werden
ausschließlich im Konstruktor gesetzt — die Instanz ist nach Konstruktion unveränderlich und
damit trivial sicher zu lesen, auch aus mehreren Threads. Erzeugung und Verteilung erfolgen
jedoch, wie bei jedem Vaadin-`ComponentEvent`, innerhalb des Session-Locks der auslösenden
Komponente; die Instanz selbst erzwingt das nicht.

# Serialisierung

`MatchChangeEvent` ist transitiv `Serializable` (geerbt über `ComponentEvent<Component>`, das
selbst `Serializable` implementiert). Die Klasse deklariert einen expliziten
`serialVersionUID`-Wert von `1L` (verifiziert, Zeile 16). Da die Superklasse `ComponentEvent`
sowie der generische Parameter `Component` aus dem Vaadin-Flow-Framework stammen, hängt die
tatsächliche Serialisierbarkeit einer konkreten Instanz zusätzlich von der Serialisierbarkeit
der jeweiligen `source`-Komponente ab.

# equals/hashCode/toString

Keine dieser Methoden ist überschrieben (verifiziert: keine `equals`-, `hashCode`- oder
`toString`-Deklaration im Quellcode) — es gilt die **Identitätssemantik von
`java.lang.Object`** (`==`-Vergleich, identitätsbasierter Hashcode,
`toString()` liefert Klassenname+Hashcode), sofern `ComponentEvent` selbst keine dieser
Methoden überschreibt (im Rahmen dieser Verifikation nicht geprüft, da externer
Framework-Code).

# Vererbungshierarchie


**Vorwärts (eigene Deklaration):** `public class MatchChangeEvent extends
ComponentEvent<Component>`.

- **Superklasse:** `ComponentEvent<Component>` — Vaadin-Flow-Framework-Klasse
  (`com.vaadin.flow.component.ComponentEvent`), kein Projekt-Typ, daher kein Cross-Link.
  `ComponentEvent` selbst implementiert bereits `Serializable`, daher deklariert
  `MatchChangeEvent` dies nicht zusätzlich.
- **Interfaces:** keine direkt deklarierten (über `ComponentEvent` geerbt: `Serializable`,
  extern).

**Rückwärts (Abhängige):** Verifiziert per Grep auf `extends MatchChangeEvent` über den
gesamten `web-common/src/main/java/de/makno/web/common/component/`-Baum — **kein Treffer**.
Keine projektinternen Subklassen. `MatchChangeEvent` wird von
[XmlViewer](/api-reference/xmlviewer/xml-viewer/xml-viewer.md), [TextViewer](/api-reference/text/text-viewer/text-viewer.md) und
`CodeViewer` als **Instanz gefeuert** (Assoziation, nicht Vererbung) und vom
[SearchNavigator](/api-reference/navigation/search-navigator/search-navigator.md) über
`addMatchChangeListener` konsumiert.

# Konstruktoren

- [siehe constructor.md](./constructor.md)

# Methoden

- [``public int getMatchCount()``](./get-match-count.md)
- [``public int getCurrentMatchIndex()``](./get-current-match-index.md)
- [``public boolean isReset()``](./is-reset.md)

# Citations


[1] web-common/src/main/java/de/makno/web/common/component/navigation/MatchChangeEvent.java
