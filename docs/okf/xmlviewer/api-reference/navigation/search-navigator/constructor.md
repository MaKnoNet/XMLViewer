---
type: API Reference
title: SearchNavigator – Konstruktoren
description: Alle Konstruktoren von SearchNavigator.
resource: web-common/src/main/java/de/makno/web/common/component/navigation/SearchNavigator.java
tags: [api-reference, constructor]
timestamp: '2026-07-08T09:00:00+02:00'
---


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

# Citations

[1] [SearchNavigator (Übersicht)](./search-navigator.md)
