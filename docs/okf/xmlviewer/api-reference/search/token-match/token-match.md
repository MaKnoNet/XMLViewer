---
type: API Reference
title: TokenMatch
description: Record für einen einzelnen Suchtreffer als Zeichen-Bereich innerhalb eines durchsuchbaren Tokens.
resource: web-common/src/main/java/de/makno/web/common/component/search/TokenMatch.java
tags: [api-reference, search, record]
timestamp: '2026-07-07T10:00:00+02:00'
---

# Überblick


`TokenMatch` ist ein `Serializable` **Record** mit den drei `int`-Komponenten
`tokenIndex`, `start` und `end`. Er beschreibt einen Suchtreffer rein als
Offset-Deskriptor — ohne Bezug auf konkrete UI-Knoten — und wird von
[SearchController](/api-reference/search/search-controller/search-controller.md) erzeugt und an einen
[SearchHighlightRenderer](/api-reference/search/search-highlight-renderer/search-highlight-renderer.md) übergeben, der ihn
im Frontend zeichnet. Mehr zur Rolle im Gesamtbild in
[Geteilte Such-Engine](/architecture/search-engine.md).

`tokenIndex` referenziert die Position in der Token-Liste (= Dokumentreihenfolge der
`.search-token`-Spans im DOM), `start` ist der 0-basierte, inklusive Start-Offset im
Token-Text, `end` der exklusive End-Offset.

**Thread-Safety:** Records sind unveränderliche Value-Types mit ausschließlich
primitiven (`int`) Feldern — trivial thread-sicher, beliebig teilbar.

# Felder

Felder = Record-Komponenten, siehe [Konstruktor](./constructor.md). Zusätzlich zu den
drei `int`-Komponenten (`tokenIndex`, `start`, `end`) besitzt die Klasse eine eigene
statische Konstante:

| Feld | Typ | Bedeutung | null-erlaubt |
|---|---|---|---|
| `serialVersionUID` | `private static final long` | Serialisierungs-Versionskonstante, Wert `1L` (verifiziert). | entfällt (primitiv `long`) |

# Thread-Safety

Records sind unveränderliche Value-Types; alle drei Komponenten sind primitive
`int`-Felder (keine Objektreferenzen, kein Compact-Constructor mit zusätzlicher
Validierung im Quellcode) — die Klasse ist damit trivial thread-sicher und beliebig
zwischen Threads teilbar, ohne jede Synchronisation.

# Serialisierung

`implements Serializable` mit explizit gesetztem
`private static final long serialVersionUID = 1L` (verifiziert). Alle drei
Record-Komponenten sind primitive `int`-Werte — keine Abhängigkeit von der
Serialisierbarkeit fremder Typen. Solange sich die Komponentenstruktur nicht
ändert, bleibt die `serialVersionUID` stabil kompatibel zu vorher serialisierten
Session-Zuständen.

# equals/hashCode/toString

Als Record erhält `TokenMatch` automatisch **komponentenbasierte Semantik** für
alle drei Methoden (vom Compiler generiert, keine eigene Überschreibung im
Quellcode verifiziert): `equals`/`hashCode` vergleichen `tokenIndex`, `start` und
`end` strukturell (zwei Instanzen mit denselben drei Werten sind `equals` und
liefern denselben Hashcode), `toString()` liefert ein Format wie
`TokenMatch[tokenIndex=0, start=3, end=7]`. Da alle Komponenten primitive `int`-Werte
sind, ist diese Semantik ohne Einschränkungen (keine Lambda-/Referenz-Sonderfälle
wie bei `SearchToken`).

# Vererbungshierarchie


**Vorwärts (eigene Deklaration):** `public record TokenMatch(int tokenIndex, int start, int
end) implements Serializable`.

- **Superklasse:** implizit `java.lang.Record` — JDK-Typ, kein Cross-Link.
- **Interfaces:**
  - `java.io.Serializable` — JDK-Standard-Interface (Marker-Interface, keine Methoden), kein
    Projekt-Typ, daher kein Cross-Link.
- Records sind implizit `final` — es kann keine Subklasse geben.

**Rückwärts (Abhängige):** Verifiziert per Grep auf `extends TokenMatch` über den gesamten
`web-common/src/main/java/de/makno/web/common/component/`-Baum — **kein Treffer**
(erwartungsgemäß, Records können nicht erweitert werden). `TokenMatch` wird von
[SearchController](/api-reference/search/search-controller/search-controller.md) erzeugt und an einen
[SearchHighlightRenderer](/api-reference/search/search-highlight-renderer/search-highlight-renderer.md) übergeben
(Assoziation, nicht Vererbung).

# Konstruktoren

- [siehe constructor.md](./constructor.md)

# Methoden

- [`tokenIndex()` / `start()` / `end()`](./token-index.md) *(implizite Accessor-Methoden)*

# Citations


[1] web-common/src/main/java/de/makno/web/common/component/search/TokenMatch.java
