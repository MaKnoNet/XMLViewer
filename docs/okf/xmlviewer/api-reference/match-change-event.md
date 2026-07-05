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
Zusammenspiels: implementierende Quellen wie [XmlViewer](/api-reference/xml-viewer.md),
[TextViewer](/api-reference/text-viewer.md) und `CodeViewer` feuern dieses Ereignis bei jeder
Änderung ihrer Suchtreffer/-navigation, und der
[SearchNavigator](/api-reference/search-navigator.md) hält darüber seine Anzeige synchron.

**Thread-Safety:** Die Instanz ist nach Konstruktion unveränderlich (alle drei Zusatzfelder
`final`, keine Setter) — insofern trivial thread-sicher zu lesen. Wie jedes Vaadin-`ComponentEvent`
wird sie jedoch innerhalb des Session-Locks der auslösenden Komponente erzeugt und verteilt;
kein eigenständiger Thread-Safety-Vertrag über den von Vaadins Event-Bus hinaus.

# Konstruktoren

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

# Methoden

## `public int getMatchCount()`

- Keine Parameter. Rückgabewert: `int`, der im Konstruktor übergebene Wert unverändert,
  Anzahl der Treffer der Suche, die das Event ausgelöst hat. Kein `null`-Fall (primitiver
  Typ). Exceptions: keine.

## `public int getCurrentMatchIndex()`

- Keine Parameter. Rückgabewert: `int`, 0-basierter Index des aktuellen Treffers, oder
  `-1`, wenn keiner aktiv ist (Bedeutung laut Javadoc-Kommentar; der Wert wird 1:1
  durchgereicht, die Konvention „-1 = kein Treffer" wird von den Aufrufern, nicht von dieser
  Klasse selbst, sichergestellt). Exceptions: keine.

## `public boolean isReset()`

- Keine Parameter. Rückgabewert: `boolean`. `true` bedeutet laut Javadoc-Kommentar: die
  Suche wurde zurückgesetzt, weil der dargestellte Inhalt ersetzt wurde (z.&nbsp;B.
  `setRoot`/`setText`), nicht bloß, weil die aktuelle Eingabe keine Treffer hatte. Diese
  Unterscheidung wird ausschließlich vom jeweiligen Aufrufer (z.&nbsp;B. `XmlViewer.render()`
  ruft `fireSearchReset()` mit `reset = true` auf, `fireMatchChange()` mit dem
  dreiparametrigen Konstruktor, also `reset = false`) korrekt gepflegt — die Klasse selbst
  erzwingt diese Semantik nicht. Exceptions: keine.

# Citations

[1] web-common/src/main/java/de/makno/web/common/component/navigation/MatchChangeEvent.java
