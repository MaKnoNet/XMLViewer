---
type: API Reference
title: MatchChangeEvent – Konstruktoren
description: Alle Konstruktoren von MatchChangeEvent.
resource: web-common/src/main/java/de/makno/web/common/component/navigation/MatchChangeEvent.java
tags: [api-reference, constructor]
timestamp: '2026-07-08T09:00:00+02:00'
---


## `public MatchChangeEvent(Component source, int matchCount, int currentMatchIndex)`

Delegiert an den vierparametrigen Konstruktor mit `reset = false`
(`this(source, matchCount, currentMatchIndex, false);`).

- `source` (`com.vaadin.flow.component.Component`) — null-erlaubt: **nicht explizit geprüft
  in dieser Klasse**; wird 1:1 an `super(source, false)` (`ComponentEvent`) weitergereicht.
  Vaadins `ComponentEvent`-Basisklasse prüft `source` intern typischerweise mit
  `Objects.requireNonNull` (Vaadin-Flow-Framework-Code, nicht Teil dieses Projekts) — die
  genaue Fehlermeldung liegt aber außerhalb dieser Klasse und wird hier nicht verifiziert.
- `matchCount` (`int`) — primitiver Typ, kein Null-Fall möglich. Keine Wertebereichsprüfung
  im Konstruktor (auch negative Werte würden anstandslos übernommen).
- `currentMatchIndex` (`int`) — primitiver Typ, keine Wertebereichsprüfung.

**Was bei ungültiger Eingabe passiert:** Für `source == null` hängt es von Vaadins
`ComponentEvent`-Basisklasse ab (nicht Teil dieses Projekts, hier nicht nachvollzogen). Für
`matchCount`/`currentMatchIndex` mit unplausiblen Werten (z.&nbsp;B. negative `matchCount`):
keine Prüfung, keine Exception — die Werte werden unverändert in die Felder übernommen.

## `public MatchChangeEvent(Component source, int matchCount, int currentMatchIndex, boolean reset)`

- `source` — wie oben, keine explizite Prüfung in dieser Klasse, Weiterreichung an
  `super(source, false)`.
- `matchCount` (`int`) — keine Prüfung.
- `currentMatchIndex` (`int`) — keine Prüfung.
- `reset` (`boolean`) — primitiver Typ, kein Null-Fall.

**Was bei ungültiger Eingabe passiert:** Wie beim dreiparametrigen Konstruktor — keine
eigene Validierung, alle vier Werte werden direkt in die `final`-Felder übernommen
(`this.matchCount = matchCount; this.currentMatchIndex = currentMatchIndex; this.reset =
reset;`).

# Citations

[1] [MatchChangeEvent (Übersicht)](./match-change-event.md)
