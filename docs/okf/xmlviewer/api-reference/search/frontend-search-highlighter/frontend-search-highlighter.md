---
type: API Reference
title: FrontendSearchHighlighter
description: Standard-SearchHighlightRenderer, der Treffer als Offset-Daten an das Frontend-Modul search-highlighter.js überträgt (CSS Custom Highlight API statt Server-DOM).
resource: web-common/src/main/java/de/makno/web/common/component/search/FrontendSearchHighlighter.java
tags: [api-reference, search, vaadin, rendering]
timestamp: '2026-07-07T10:00:00+02:00'
---

# Überblick


`FrontendSearchHighlighter` ist die `final`, `Serializable` Standardimplementierung von
[SearchHighlightRenderer](/api-reference/search/search-highlight-renderer/search-highlight-renderer.md). Sie überträgt
Treffer als flache Zahlenfolge (`"tokenIndex,start,end,…"`) per `executeJs` an das
Frontend-Modul `search-highlighter.js`, das die Bereiche via CSS Custom Highlight API
zeichnet — dadurch entsteht kein zusätzlicher DOM-Knoten und kein zusätzlicher
Session-Heap pro Treffer. Die Zahlenfolge ist bewusst ein `String` und kein JSON-Typ, damit
die Bibliothek an keine JSON-Bibliothek gebunden ist (siehe
[Vaadin-API-Nutzung](/conventions/vaadin-api-nutzung.md)). Mehr zur
Rolle im Gesamtbild in
[Geteilte Such-Engine](/architecture/search-engine.md) und
[Frontend-Integration](/architecture/frontend-integration.md).

Ohne gebundene UI (z.&nbsp;B. im Unit-Test, wo die Komponente nie an eine `UI` angehängt
wurde) sind alle drei öffentlichen Methoden wirkungslose No-ops — verifiziert durch die
`host.getUI().isEmpty()`-Prüfung am Anfang jeder Methode.

**Thread-Safety:** Die einzige Instanzvariable `host` ist `final` und wird im Konstruktor
gesetzt — die Klasse selbst hält keinen veränderlichen Zustand. Sie ist an die
Wirts-Komponente (und damit an deren Session/UI-Thread) gebunden; `executeJs`-Aufrufe
außerhalb des Session-Threads sind eine Vaadin-weite Einschränkung, keine Besonderheit
dieser Klasse.

# Felder

| Feld | Typ | Bedeutung | null-erlaubt |
|---|---|---|---|
| `serialVersionUID` | `private static final long` | Serialisierungs-Versionskonstante, Wert `1L` (verifiziert). | entfällt (primitiv `long`) |
| `JS_APPLY` | `private static final String` | JS-Snippet `"window.SearchHighlighter.apply(this, $0, $1)"`, ruft das Frontend-Modul zum Zeichnen aller Treffer auf. | nein — Kompilierzeit-Konstante |
| `JS_MOVE_CURRENT` | `private static final String` | JS-Snippet `"window.SearchHighlighter.moveCurrent(this, $0)"`, verschiebt nur die Hervorhebung des aktuellen Treffers. | nein — Kompilierzeit-Konstante |
| `JS_CLEAR` | `private static final String` | JS-Snippet `"window.SearchHighlighter.clear(this)"`, entfernt alle Markierungen. | nein — Kompilierzeit-Konstante |
| `SEPARATOR` | `private static final char` | Trennzeichen `','` der Treffer-Zahlenfolge; identisch mit der Konstante `SEPARATOR` in `search-highlighter.js`. | entfällt (primitiv `char`) |
| `CHARS_PER_MATCH` | `private static final int` | Kapazitätsschätzung `16` Zeichen je Treffer für die Vordimensionierung des `StringBuilder` in `toFlatCsv`. | entfällt (primitiv `int`) |
| `host` | `private final Component` | Wirts-Komponente; deren Wurzel-Element dient als `this`-Scope für alle `executeJs`-Aufrufe. | nein — Konstruktor erzwingt `Objects.requireNonNull(host, "host")` |

# Thread-Safety

Kein besonderer Synchronisationsaufwand nötig, aber auch nicht beliebig teilbar: Die
einzige Instanzvariable `host` ist `final` und wird ausschließlich im Konstruktor
gesetzt (verifiziert) — die Klasse selbst trägt keinen veränderlichen Zustand. Jede
öffentliche Methode (`render`, `moveCurrent`, `clear`) prüft zuerst
`host.getUI().isEmpty()` und ist ohne gebundene UI ein wirkungsloses No-op (verifiziert).
Die tatsächliche Ausführung von `executeJs` ist an die Vaadin-Session des `host`
gebunden — Aufrufe außerhalb des zugehörigen Session-Threads sind eine
Vaadin-weite Einschränkung (Vaadin serialisiert UI-Zugriffe pro Session), keine
Besonderheit dieser Klasse. Da `host` selbst veränderlich sein kann (Vaadin-`Component`),
ist die Klasse an genau eine Komponenteninstanz gebunden und nicht für parallele
Wiederverwendung über mehrere Komponenten hinweg gedacht.

# Serialisierung

`implements Serializable` (über `SearchHighlightRenderer`, das `Serializable`
erweitert) mit explizit gesetztem `private static final long serialVersionUID = 1L`
(verifiziert). Die Klasse hält nur die `host`-Referenz (`Component`); deren
Serialisierbarkeit hängt von der konkreten Vaadin-Komponente ab und liegt außerhalb
der Kontrolle dieser Klasse. Solange sich die Feldstruktur nicht ändert, bleibt die
`serialVersionUID` stabil kompatibel zu vorher serialisierten Session-Zuständen.

# equals/hashCode/toString

Keine dieser Methoden ist überschrieben (verifiziert: keine `equals`-/`hashCode`-/
`toString`-Deklaration im Quellcode) — es gilt die **Identitätssemantik von
`java.lang.Object`** (`==`-Vergleich, identitätsbasierter Hashcode, `toString()`
liefert Klassenname+Hashcode). Da je Komponente genau eine Instanz erzeugt und
gehalten wird (kein Sammlungs-/Vergleichs-Kontext), hat das in der Praxis keine
Auswirkung.

# Vererbungshierarchie


**Vorwärts (eigene Deklaration):** `public final class FrontendSearchHighlighter implements
SearchHighlightRenderer`.

- **Superklasse:** keine explizite (impliziter `Object`).
- **Interfaces:**
  - [SearchHighlightRenderer](/api-reference/search/search-highlight-renderer/search-highlight-renderer.md) — projektinternes
    Abstraktions-Interface aus `search`; `FrontendSearchHighlighter` implementiert dessen
    Zeichenmethoden per Frontend-Delegation (CSS Custom Highlight API statt Server-DOM, siehe
    Überblick).
- Die Klasse ist `final` — es kann ohnehin keine projektinterne oder externe Subklasse geben.

**Rückwärts (Abhängige):** Verifiziert per Grep auf `extends FrontendSearchHighlighter` über
den gesamten `web-common/src/main/java/de/makno/web/common/component/`-Baum — **kein
Treffer** (erwartungsgemäß, die Klasse ist `final`).

# Konstruktoren

- [siehe constructor.md](./constructor.md)

# Methoden

- [`render(List<TokenMatch> matches, int currentIndex)`](./render.md)
- [`moveCurrent(int currentIndex)`](./move-current.md)
- [`clear()`](./clear.md)
- [`toFlatCsv(List<TokenMatch> matches)`](./to-flat-csv.md) *(package-private `static`, für Tests sichtbar)*

# Citations


[1] web-common/src/main/java/de/makno/web/common/component/search/FrontendSearchHighlighter.java
