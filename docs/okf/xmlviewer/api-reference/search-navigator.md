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
[MatchNavigable](/api-reference/match-navigable.md)) steht bereits in
[SearchNavigator (Komponenten-Doku)](/components/search-navigator.md) — diese Datei
konzentriert sich ausschließlich auf die erschöpfende, verifizierte Methodenreferenz.

**Thread-Safety (Javadoc-Aussage, gegen Code plausibilisiert):** Nicht thread-safe, eine
Instanz pro UI/Session. Verifiziert: alle Felder sind einfache, unsynchronisierte
Instanzfelder; `matchChangeRegistration` wird beim Detach explizit gelöst
(`onDetach`/`onAttach`), um ein Session-Memory-Leak zu vermeiden — das ist ein
dokumentiertes und im Code tatsächlich umgesetztes Verhalten (siehe unten).

# Vererbungshierarchie

**Vorwärts (eigene Deklaration):** `public class SearchNavigator extends Composite<Div>`.

- **Superklasse:** `Composite<Div>` — Vaadin-Flow-Framework-Klasse
  (`com.vaadin.flow.component.Composite`), kein Projekt-Typ, daher kein Cross-Link.
- **Interfaces:** keine. Im Unterschied zu `XmlViewer`/`TextViewer`/`CodeViewer` implementiert
  `SearchNavigator` selbst **nicht** `MatchNavigable` — es ist der Konsument dieses
  Interfaces (hält eine `MatchNavigable`-Referenz als Konstruktorparameter/Feld, siehe
  [MatchNavigable](/api-reference/match-navigable.md)), keine Implementierung davon. Das ist
  die im Überblick erwähnte Dependency Inversion: `SearchNavigator` programmiert gegen das
  Interface, ohne selbst Teil von dessen Vererbungshierarchie zu sein.

**Rückwärts (Abhängige):** Verifiziert per Grep auf `extends SearchNavigator` über den
gesamten `web-common/src/main/java/de/makno/web/common/component/`-Baum — **kein Treffer**.
Keine projektinternen Subklassen.

# Konstruktoren

## `public SearchNavigator(MatchNavigable navigable)`

- `navigable` (`de.makno.web.common.component.navigation.MatchNavigable`) — **null-erlaubt:
  nein**, verifiziert durch `this.navigable = Objects.requireNonNull(navigable,
  "navigable");` als erste Anweisung im Konstruktor.

**Was bei ungültiger Eingabe passiert:** Wirft `NullPointerException` mit Nachricht
`"navigable"`, wenn `navigable == null`. Danach wird die CSS-Klasse gesetzt, das Layout
aufgebaut (`buildLayout()`), der Treffer-Listener registriert
(`registerMatchChangeListener()`) und die Anzeige initial synchronisiert
(`update(navigable.getMatchCount(), navigable.getCurrentMatchIndex())` — ruft also sofort
zwei Methoden auf der übergebenen `navigable`-Instanz auf; wäre `navigable` trotz fehlendem
Guard `null` gewesen, wäre der Fehler ohnehin spätestens hier als `NullPointerException`
sichtbar geworden, aber der explizite `requireNonNull` sorgt für eine klarere, frühere
Fehlermeldung).

# Methoden

## `protected void onAttach(AttachEvent attachEvent)`

- `attachEvent` (`com.vaadin.flow.component.AttachEvent`) — null-erlaubt: nicht geprüft in
  dieser Methode; wird 1:1 an `super.onAttach(attachEvent)` weitergereicht (Vaadin-Framework-
  Vertrag, nicht Teil dieses Projekts).
- Verhalten: Ruft zuerst `super.onAttach(attachEvent)` auf. Ist `matchChangeRegistration ==
  null` (d.h. der Listener wurde zuvor beim Detach gelöst oder nie registriert), wird er neu
  registriert und die Anzeige mit dem aktuellen Treffer-Stand der `navigable`-Quelle
  synchronisiert. Ist bereits eine Registrierung vorhanden, passiert nichts (Guard
  `if (matchChangeRegistration == null)`).
- Rückgabewert: keiner (`void`, `protected`, überschreibt `Composite.onAttach`).
- Exceptions: keine explizit; abhängig von `super.onAttach` (Vaadin-Framework).

## `protected void onDetach(DetachEvent detachEvent)`

- `detachEvent` (`com.vaadin.flow.component.DetachEvent`) — null-erlaubt: nicht geprüft;
  wird 1:1 an `super.onDetach(detachEvent)` weitergereicht.
- Verhalten: Ist `matchChangeRegistration != null`, wird sie gelöst
  (`matchChangeRegistration.remove()`) und das Feld auf `null` gesetzt — **verifiziert
  deckungsgleich mit der Javadoc-Behauptung**, dass dies ein Session-Memory-Leak verhindert
  (die langlebige `navigable`-Quelle würde sonst eine Referenz auf die detachte Leiste über
  den registrierten Listener behalten). Danach `super.onDetach(detachEvent)`.
- Rückgabewert: keiner. Exceptions: keine explizit.

## `private void registerMatchChangeListener()`

- Keine Parameter. Registriert `this::onMatchChange` als Listener bei
  `navigable.addMatchChangeListener(...)` und speichert die zurückgegebene `Registration`.
- Rückgabewert: keiner. Exceptions: keine explizit; abhängig vom Vertrag von
  `MatchNavigable.addMatchChangeListener` (siehe [MatchNavigable](/api-reference/match-navigable.md)).
- Hinweis: package-private wäre `private`, tatsächlich ist die Methode `private` (keine
  Paket-Sichtbarkeit) — der Vollständigkeit halber dennoch dokumentiert, da sie zentrales
  Verhalten steuert.

## `private void onMatchChange(MatchChangeEvent event)`

- `event` (`de.makno.web.common.component.navigation.MatchChangeEvent`) — null-erlaubt:
  nicht geprüft in dieser Methode; wird nur intern von Vaadins Event-Bus als Listener-Callback
  aufgerufen (`event.isReset()`, `event.getMatchCount()`, `event.getCurrentMatchIndex()` — bei
  `event == null` würde eine `NullPointerException` bei `event.isReset()` auftreten, aber
  dieser Fall ist praktisch ausgeschlossen, da Vaadin niemals `null`-Events an Listener
  liefert).
- Verhalten: Ist `event.isReset()` wahr, wird das Suchfeld geleert (`searchField.clear()`) —
  laut Kommentar löst das ein leeres `search("")`-Ereignis in der Quelle aus, das über ein
  Folge-Event Zähler/Buttons zusätzlich zurücksetzt. Danach wird immer `update(...)` mit dem
  aktuellen Treffer-Stand aus dem Event aufgerufen.
- Rückgabewert: keiner. Exceptions: keine explizit (siehe `event == null`-Hinweis oben).

## `public void setLabelFormatter(MatchLabelFormatter labelFormatter)`

- `labelFormatter` (`de.makno.web.common.component.navigation.MatchLabelFormatter`) —
  **null-erlaubt: nein**, verifiziert durch `this.labelFormatter =
  Objects.requireNonNull(labelFormatter, "labelFormatter");` als erste Anweisung.

**Was bei ungültiger Eingabe passiert:** Wirft `NullPointerException` mit Nachricht
`"labelFormatter"`, wenn `labelFormatter == null`. Bei gültiger Eingabe wird die Anzeige
sofort mit dem aktuellen Treffer-Stand aktualisiert (`update(navigable.getMatchCount(),
navigable.getCurrentMatchIndex())`).

- Rückgabewert: keiner.

## `private HorizontalLayout buildLayout()`

- Keine Parameter. Baut Suchfeld, Vor-/Zurück-Buttons und Label zu einem `HorizontalLayout`
  zusammen; registriert die internen Klick-/Wertänderungs-Listener, die ihrerseits
  `navigable.search(...)`, `navigable.previousMatch()`, `navigable.nextMatch()` aufrufen.
- Rückgabewert: `HorizontalLayout`, laut Konstruktionslogik nie `null` (immer frisch erzeugt
  und mit vier Kindkomponenten befüllt).
- Exceptions: keine explizit.

## `private static Button createIconButton(VaadinIcon icon, String tooltip)`

- `icon` (`com.vaadin.flow.component.icon.VaadinIcon`) — null-erlaubt: nicht geprüft in
  dieser Methode; `icon.create()` würde bei `icon == null` eine `NullPointerException`
  werfen (Aufruf einer Instanzmethode auf `null`). Alle tatsächlichen Aufrufer im Code
  übergeben feste, nie-`null`e Konstanten (`VaadinIcon.CHEVRON_LEFT`/`CHEVRON_RIGHT`).
- `tooltip` (`String`) — null-erlaubt: ja (keine Prüfung); `button.setTooltipText(tooltip)`
  toleriert laut Vaadin-Framework-Vertrag `null` (entfernt den Tooltip) — dieses Verhalten
  liegt aber außerhalb dieser Klasse und wird hier nicht weiter verifiziert.
- Rückgabewert: `Button`, nie `null` (frisch konstruiert).
- Exceptions: `NullPointerException` bei `icon == null` (siehe oben), in der Praxis nie
  beobachtet, da nur Konstanten übergeben werden.

## `private void update(int matchCount, int currentMatchIndex)`

- `matchCount` (`int`), `currentMatchIndex` (`int`) — primitive Typen, kein Null-Fall,
  keine Wertebereichsprüfung.
- Verhalten: berechnet `currentPosition` (1-basiert, `0` wenn `matchCount <= 0`), setzt den
  Label-Text über `labelFormatter.format(matchCount, currentPosition)` und schaltet die
  Buttons aktiv/inaktiv (`setButtonsEnabled(matchCount > 0)`).
- Rückgabewert: keiner.
- Exceptions: keine explizit in dieser Methode; hängt vom aktiven `labelFormatter` ab (ein
  eigener, per `setLabelFormatter` gesetzter Formatter könnte theoretisch selbst eine
  Exception werfen, das liegt aber außerhalb der Kontrolle von `SearchNavigator`).

## `private void setButtonsEnabled(boolean enabled)`

- `enabled` (`boolean`) — primitiver Typ, kein Null-Fall.
- Setzt `previousButton.setEnabled(enabled)` und `nextButton.setEnabled(enabled)`.
- Rückgabewert: keiner. Exceptions: keine.

# Citations

[1] web-common/src/main/java/de/makno/web/common/component/navigation/SearchNavigator.java
