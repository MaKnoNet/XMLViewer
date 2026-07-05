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
[SearchController](/api-reference/search-controller.md) von der konkreten Komponente
entkoppelt. Mehr zur Architektur in [Geteilte Such-Engine](/architecture/search-engine.md).

**Thread-Safety:** Records sind Value-Types mit ausschließlich `final` Feldern; eine
einmal konstruierte Instanz ist unveränderlich und kann gefahrlos zwischen Threads geteilt
werden. Der referenzierte `onReveal`-Callback selbst ist jedoch typischerweise an eine
UI-Instanz (Session-Thread) gebunden — seine Ausführung außerhalb des Session-Threads ist
Sache des Aufrufers, nicht von `SearchToken`.

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
[SearchController](/api-reference/search-controller.md) als Element einer
`List<SearchToken>` verwendet (Assoziation, nicht Vererbung).

# Konstruktoren

## Kanonischer Konstruktor (kompakt)

```java
public SearchToken {
    Objects.requireNonNull(text, "text");
    Objects.requireNonNull(onReveal, "onReveal");
}
```

Records erzeugen implizit einen kanonischen Konstruktor mit einem Parameter pro
Record-Komponente (`String text, SerializableRunnable onReveal`); hier ist er als
**kompakter Konstruktor** überschrieben, um Validierung vor der (impliziten)
Feldzuweisung einzufügen.

- `text` (`String`) — null-erlaubt: **nein**. Verifiziert durch
  `Objects.requireNonNull(text, "text")`; wirft `NullPointerException` mit Nachricht
  `"text"`, wenn `null` übergeben wird. Leerer String (`""`) ist dagegen erlaubt — nur
  `null` wird abgewiesen.
- `onReveal` (`SerializableRunnable`) — null-erlaubt: **nein**. Verifiziert durch
  `Objects.requireNonNull(onReveal, "onReveal")`; wirft `NullPointerException` mit
  Nachricht `"onReveal"` bei `null`.
- Keine weiteren Prüfungen (z.&nbsp;B. keine Bereichs- oder Formatvalidierung auf `text`).
- **Geworfene Exceptions:** `NullPointerException`, wenn `text == null` oder
  `onReveal == null` — beides durch expliziten `Objects.requireNonNull`-Aufruf im
  kompakten Konstruktor, nicht nur implizit über spätere Nutzung.

# Methoden

## `text()` / `onReveal()` (implizite Accessor-Methoden)

Von Records automatisch erzeugte Zugriffsmethoden für die beiden Komponenten.

- **Rückgabewert `text()`:** `String`, niemals `null` (durch den kompakten Konstruktor
  garantiert).
- **Rückgabewert `onReveal()`:** `SerializableRunnable`, niemals `null` (durch den
  kompakten Konstruktor garantiert).
- Werfen nichts.

## `of(String text)` <a id="of"></a>

```java
public static SearchToken of(String text)
```

Fabrikmethode für Tokens ohne eigene Sichtbarkeits-Logik. Erzeugt intern
`new SearchToken(text, NO_REVEAL)`, wobei `NO_REVEAL` eine geteilte, statische No-Op-Lambda
(`() -> {}`) ist — alle über `of(...)` erzeugten Tokens teilen sich dieselbe Reveal-Instanz.

- `text` (`String`) — null-erlaubt: **nein**, indirekt. `of` selbst prüft `text` nicht
  explizit, aber die Delegation an den kanonischen Konstruktor `new SearchToken(text,
  NO_REVEAL)` löst dessen kompakten Konstruktor aus, der `Objects.requireNonNull(text,
  "text")` ausführt. Ergebnis ist identisch zum direkten Konstruktoraufruf: `null` führt zu
  `NullPointerException` mit Nachricht `"text"`.
- **Rückgabewert:** `SearchToken`, niemals `null`.
- **Geworfene Exceptions:** `NullPointerException`, wenn `text == null` (über den
  kompakten Konstruktor, siehe oben).

**Hinweis zur Deduplizierung:** [SearchController](/api-reference/search-controller.md)
nutzt Identitätsvergleich (`IdentityHashMap`) auf `onReveal`, um beim Sichtbarmachen von
Treffern mehrere Tokens desselben Elements nur einmal aufzuklappen. Da `NO_REVEAL` eine
einzige geteilte Instanz ist, werden alle über `of(...)` erzeugten Tokens automatisch als
"dieselbe Reveal-Aktion" erkannt und dedupliziert — ein beabsichtigter Nebeneffekt der
gemeinsamen Konstante.

# Citations

[1] web-common/src/main/java/de/makno/web/common/component/search/SearchToken.java
