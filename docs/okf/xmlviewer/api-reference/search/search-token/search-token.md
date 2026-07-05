---
type: API Reference
title: SearchToken
description: Record für ein durchsuchbares Text-Token (Klartext + Reveal-Aktion), die Grundeinheit der Textsuche im search-Package.
resource: web-common/src/main/java/de/makno/web/common/component/search/SearchToken.java
tags: [api-reference, search, record]
timestamp: '2026-07-07T10:00:00+02:00'
---

# Überblick


`SearchToken` ist ein `Serializable` **Record** mit den Komponenten `text` (der zu
durchsuchende Klartext) und `onReveal` (eine `SerializableRunnable`, die einen Treffer in
diesem Token sichtbar macht, z.&nbsp;B. einen zugeklappten XML-Zweig aufklappen). Für
Komponenten ohne eigene Sichtbarkeits-Logik (reiner Text) liefert die statische Fabrik
[`of(String)`](#of) eine geteilte No-Op-Aktion.

Der Typ ist bewusst ohne Bezug auf konkrete UI-Knoten oder ein Eingabeformat modelliert
(kein Vaadin-`Span`, kein XML-`Element`) — dadurch bleibt
[SearchController](/api-reference/search/search-controller/search-controller.md) von der konkreten Komponente
entkoppelt. Mehr zur Architektur in [Geteilte Such-Engine](/architecture/search-engine.md).

**Thread-Safety:** Records sind Value-Types mit ausschließlich `final` Feldern; eine
einmal konstruierte Instanz ist unveränderlich und kann gefahrlos zwischen Threads geteilt
werden. Der referenzierte `onReveal`-Callback selbst ist jedoch typischerweise an eine
UI-Instanz (Session-Thread) gebunden — seine Ausführung außerhalb des Session-Threads ist
Sache des Aufrufers, nicht von `SearchToken`.

# Felder

Felder = Record-Komponenten, siehe [Konstruktor](./constructor.md). Zusätzlich zu den
Komponenten `text` und `onReveal` besitzt die Klasse eine eigene statische Konstante:

| Feld | Typ | Bedeutung | null-erlaubt |
|---|---|---|---|
| `serialVersionUID` | `private static final long` | Serialisierungs-Versionskonstante, Wert `1L` (verifiziert). | entfällt (primitiv `long`) |
| `NO_REVEAL` | `private static final SerializableRunnable` | Geteilte No-Op-Reveal-Aktion (leerer Lambda-Body `() -> {}`) für Tokens ohne Sichtbarkeits-Logik, verwendet von der Fabrik `of(String)`. | nein — Lambda-Konstante, nie `null` |

# Thread-Safety

Records sind Value-Types mit ausschließlich `final` Feldern (compact constructor
erzwingt `Objects.requireNonNull` für beide Komponenten, keine Setter vorhanden) —
eine einmal konstruierte Instanz ist unveränderlich und kann gefahrlos zwischen
Threads geteilt werden. Die geteilte `NO_REVEAL`-Konstante ist ein zustandsloses,
seiteneffektfreies Lambda und ebenfalls gefahrlos teilbar. Der jeweils referenzierte
`onReveal`-Callback selbst ist jedoch typischerweise an eine UI-Instanz
(Session-Thread) gebunden — dessen Ausführung außerhalb des Session-Threads ist
Sache des Aufrufers, nicht von `SearchToken`.

# Serialisierung

`implements Serializable` mit explizit gesetztem
`private static final long serialVersionUID = 1L` (verifiziert). Beide
Record-Komponenten sind serialisierungsfreundlich: `text` ist `String`, `onReveal`
ist `SerializableRunnable` (Vaadin-Typ, eigens für serialisierbare Lambdas gedacht).
Solange sich die Komponentenstruktur nicht ändert, bleibt die `serialVersionUID`
stabil kompatibel zu vorher serialisierten Session-Zuständen.

# equals/hashCode/toString

Als Record erhält `SearchToken` automatisch **komponentenbasierte Semantik** für
alle drei Methoden (vom Compiler generiert, keine eigene Überschreibung im
Quellcode verifiziert): `equals`/`hashCode` vergleichen `text` und `onReveal`
strukturell (wertbasierte Gleichheit beider Komponenten), `toString()` liefert ein
Format wie `SearchToken[text=..., onReveal=...]`. Da `onReveal` typischerweise ein
Lambda ist, unterliegt der Vergleich zweier `SearchToken`-Instanzen mit
unterschiedlichen (aber funktional gleichen) Lambda-Objekten der üblichen
JDK-Lambda-Identität — zwei separat erzeugte Lambda-Instanzen sind auch bei
gleichem Funktionskörper nicht als `equals` garantiert.

# Vererbungshierarchie


**Vorwärts (eigene Deklaration):** `public record SearchToken(String text, SerializableRunnable
onReveal) implements Serializable`.

- **Superklasse:** implizit `java.lang.Record` (jeder Record erweitert automatisch `Record`,
  kein eigener `extends`-Ausdruck möglich) — JDK-Typ, kein Cross-Link.
- **Interfaces:**
  - `java.io.Serializable` — JDK-Standard-Interface (Marker-Interface, keine Methoden), kein
    Projekt-Typ, daher kein Cross-Link.
- Records sind implizit `final` — es kann keine Subklasse geben.

**Rückwärts (Abhängige):** Verifiziert per Grep auf `extends SearchToken` über den gesamten
`web-common/src/main/java/de/makno/web/common/component/`-Baum — **kein Treffer**
(erwartungsgemäß, Records können nicht erweitert werden). `SearchToken` wird von
[SearchController](/api-reference/search/search-controller/search-controller.md) als Element einer
`List<SearchToken>` verwendet (Assoziation, nicht Vererbung).

# Konstruktoren

- [siehe constructor.md](./constructor.md)

# Methoden

- [`text()` / `onReveal()`](./text.md) *(implizite Accessor-Methoden)*
- [`of(String text)`](./of.md)

# Citations


[1] web-common/src/main/java/de/makno/web/common/component/search/SearchToken.java
