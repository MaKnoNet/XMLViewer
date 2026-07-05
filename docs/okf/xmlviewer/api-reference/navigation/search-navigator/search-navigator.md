---
type: API Reference
title: SearchNavigator
description: Vollständige, verifizierte Methodenreferenz der eigenständigen Such-Leiste SearchNavigator — Konstruktor, Lifecycle-Methoden, öffentliche API mit Null-Verhalten und Exceptions.
resource: web-common/src/main/java/de/makno/web/common/component/navigation/SearchNavigator.java
tags: [api-reference, navigation, vaadin, search]
timestamp: '2026-07-07T10:00:00+02:00'
---

# Überblick


`SearchNavigator` ist eine `public class SearchNavigator extends Composite<Div>`. Die
narrative Beschreibung (Such-Pille, Aufbau, Dependency Inversion über
[MatchNavigable](/api-reference/navigation/match-navigable/match-navigable.md)) steht bereits in
[SearchNavigator (Komponenten-Doku)](/components/search-navigator.md) — diese Datei
konzentriert sich ausschließlich auf die erschöpfende, verifizierte Methodenreferenz.

**Thread-Safety (Javadoc-Aussage, gegen Code plausibilisiert):** Nicht thread-safe, eine
Instanz pro UI/Session. Verifiziert: alle Felder sind einfache, unsynchronisierte
Instanzfelder; `matchChangeRegistration` wird beim Detach explizit gelöst
(`onDetach`/`onAttach`), um ein Session-Memory-Leak zu vermeiden — das ist ein
dokumentiertes und im Code tatsächlich umgesetztes Verhalten (siehe unten).

# Felder

| Feld | Typ | Bedeutung | null-erlaubt |
|---|---|---|---|
| `serialVersionUID` | `private static final long` | Serialisierungs-Versionskennung, explizit auf `1L` gesetzt (verifiziert). | entfällt (primitiv `long`) |
| `CSS_CLASS` | `private static final String` | CSS-Klasse `"xmlviewer-search-navigator"` der Wurzel (`getContent()`). | nein (Konstante, Literal) |
| `FIELD_CSS_CLASS` | `private static final String` | CSS-Klasse `"xmlviewer-search-navigator-field"` des Suchfelds. | nein (Konstante, Literal) |
| `LABEL_CSS_CLASS` | `private static final String` | CSS-Klasse `"xmlviewer-search-navigator-label"` des Treffer-Labels. | nein (Konstante, Literal) |
| `FIELD_PLACEHOLDER` | `private static final String` | Platzhaltertext `"Suchen…"` des Suchfelds. | nein (Konstante, Literal) |
| `PREVIOUS_TOOLTIP` | `private static final String` | Tooltip `"Vorheriger Treffer"` des Zurück-Buttons. | nein (Konstante, Literal) |
| `NEXT_TOOLTIP` | `private static final String` | Tooltip `"Nächster Treffer"` des Vor-Buttons. | nein (Konstante, Literal) |
| `DEFAULT_LABEL_FORMATTER` | `private static final MatchLabelFormatter` | Standardformat „aktuell/gesamt" (z. B. „12/66"), zustandslose Lambda-Instanz. | nein (Konstante, Lambda nie `null`) |
| `navigable` | `private final MatchNavigable` | Die gesteuerte Such-Quelle, per Konstruktor gesetzt. | nein — Konstruktor erzwingt `Objects.requireNonNull(navigable, "navigable")` |
| `searchField` | `private final TextField` | Eingabefeld für den Suchtext. | nein (final, direkt mit `new TextField()` initialisiert) |
| `positionLabel` | `private final Span` | Zeigt den formatierten Treffer-Stand an. | nein (final, direkt mit `new Span()` initialisiert) |
| `previousButton` | `private final Button` | Icon-Button „vorheriger Treffer". | nein (final, über `createIconButton(...)` initialisiert) |
| `nextButton` | `private final Button` | Icon-Button „nächster Treffer". | nein (final, über `createIconButton(...)` initialisiert) |
| `labelFormatter` | `private MatchLabelFormatter` | Aktuell aktives Label-Format, änderbar via `setLabelFormatter(...)`. Default `DEFAULT_LABEL_FORMATTER`. | nein — `setLabelFormatter` erzwingt `Objects.requireNonNull`; Default ist nie `null` |
| `matchChangeRegistration` | `private Registration` | Bindung an die Treffer-Events der Quelle; in `onDetach` gelöst, in `onAttach` bei Bedarf neu gesetzt. | ja — `null` nach `onDetach` bzw. vor der ersten Registrierung; Zustand wird per `if (matchChangeRegistration == null)`-Prüfung in `onAttach` ausgewertet |

# Thread-Safety

**Nicht thread-sicher, eine Instanz pro UI/Session** (verifiziert). Alle Instanzfelder sind
einfache, unsynchronisierte Felder ohne `volatile`/Locking; die Klasse erbt von
`Composite<Div>` und unterliegt damit Vaadins Single-Session-/Single-Thread-Modell — Methoden
dürfen nur aus dem UI-Thread mit gehaltenem Session-Lock aufgerufen werden. `SearchNavigator`
ist eine Vaadin-Komponente und daher **single-use pro UI-Session/Request**: eine Instanz wird
für genau eine `MatchNavigable`-Quelle innerhalb einer Session konstruiert und nicht zwischen
Sessions oder Threads geteilt. Das veränderliche Feld `matchChangeRegistration` wird gezielt
beim `onDetach` gelöscht (`.remove()` + `null`-Zuweisung) und bei erneutem `onAttach` neu
gesetzt — verifiziertes, im Code tatsächlich umgesetztes Verhalten zur Vermeidung eines
Session-Memory-Leaks über den Komponenten-Lebenszyklus hinweg (Zeilen 74-94).

# Serialisierung

`SearchNavigator` ist transitiv `Serializable` (geerbt über `Composite<Div>`, das über
`Component` `Serializable` implementiert). Die Klasse deklariert einen expliziten
`serialVersionUID`-Wert von `1L` (verifiziert, Zeile 36). Zu beachten: Das Feld `navigable`
ist vom Typ `MatchNavigable`, welches selbst `Serializable` erweitert (siehe
[MatchNavigable](/api-reference/navigation/match-navigable/match-navigable.md)) — die tatsächliche Serialisierbarkeit
einer `SearchNavigator`-Instanz hängt somit zusätzlich von der konkreten, zur Laufzeit
injizierten `MatchNavigable`-Implementierung ab. Das Feld `matchChangeRegistration`
(`com.vaadin.flow.shared.Registration`) ist ebenfalls `Serializable` (Vaadin-Framework-Typ).

# equals/hashCode/toString

Keine dieser Methoden ist überschrieben (verifiziert: keine `equals`-, `hashCode`- oder
`toString`-Deklaration im Quellcode) — es gilt die **Identitätssemantik von
`java.lang.Object`** (`==`-Vergleich, identitätsbasierter Hashcode, `toString()` liefert
Klassenname+Hashcode), sofern `Composite` selbst keine dieser Methoden überschreibt (im Rahmen
dieser Verifikation nicht geprüft, da externer Framework-Code). Für eine UI-Komponente, die
nie in Sammlungen mit Werte-Gleichheit verwendet wird, hat das in der Praxis geringe Relevanz.

# Vererbungshierarchie


**Vorwärts (eigene Deklaration):** `public class SearchNavigator extends Composite<Div>`.

- **Superklasse:** `Composite<Div>` — Vaadin-Flow-Framework-Klasse
  (`com.vaadin.flow.component.Composite`), kein Projekt-Typ, daher kein Cross-Link.
- **Interfaces:** keine. Im Unterschied zu `XmlViewer`/`TextViewer`/`CodeViewer` implementiert
  `SearchNavigator` selbst **nicht** `MatchNavigable` — es ist der Konsument dieses
  Interfaces (hält eine `MatchNavigable`-Referenz als Konstruktorparameter/Feld, siehe
  [MatchNavigable](/api-reference/navigation/match-navigable/match-navigable.md)), keine Implementierung davon. Das ist
  die im Überblick erwähnte Dependency Inversion: `SearchNavigator` programmiert gegen das
  Interface, ohne selbst Teil von dessen Vererbungshierarchie zu sein.

**Rückwärts (Abhängige):** Verifiziert per Grep auf `extends SearchNavigator` über den
gesamten `web-common/src/main/java/de/makno/web/common/component/`-Baum — **kein Treffer**.
Keine projektinternen Subklassen.

# Konstruktoren

- [siehe constructor.md](./constructor.md)

# Methoden

- [``protected void onAttach(AttachEvent attachEvent)``](./on-attach.md)
- [``protected void onDetach(DetachEvent detachEvent)``](./on-detach.md)
- [``private void registerMatchChangeListener()``](./register-match-change-listener.md)
- [``private void onMatchChange(MatchChangeEvent event)``](./on-match-change.md)
- [``public void setLabelFormatter(MatchLabelFormatter labelFormatter)``](./set-label-formatter.md)
- [``private HorizontalLayout buildLayout()``](./build-layout.md)
- [``private static Button createIconButton(VaadinIcon icon, String tooltip)``](./create-icon-button.md)
- [``private void update(int matchCount, int currentMatchIndex)``](./update.md)
- [``private void setButtonsEnabled(boolean enabled)``](./set-buttons-enabled.md)

# Citations


[1] web-common/src/main/java/de/makno/web/common/component/navigation/SearchNavigator.java
