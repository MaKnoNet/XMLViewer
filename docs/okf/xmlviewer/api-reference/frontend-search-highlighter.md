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
[SearchHighlightRenderer](/api-reference/search-highlight-renderer.md). Sie überträgt
Treffer als flaches Zahlen-Array (`[tokenIndex, start, end, …]`) per `executeJs` an das
Frontend-Modul `search-highlighter.js`, das die Bereiche via CSS Custom Highlight API
zeichnet — dadurch entsteht kein zusätzlicher DOM-Knoten und kein zusätzlicher
Session-Heap pro Treffer. Mehr zur Rolle im Gesamtbild in
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

# Vererbungshierarchie

**Vorwärts (eigene Deklaration):** `public final class FrontendSearchHighlighter implements
SearchHighlightRenderer`.

- **Superklasse:** keine explizite (impliziter `Object`).
- **Interfaces:**
  - [SearchHighlightRenderer](/api-reference/search-highlight-renderer.md) — projektinternes
    Abstraktions-Interface aus `search`; `FrontendSearchHighlighter` implementiert dessen
    Zeichenmethoden per Frontend-Delegation (CSS Custom Highlight API statt Server-DOM, siehe
    Überblick).
- Die Klasse ist `final` — es kann ohnehin keine projektinterne oder externe Subklasse geben.

**Rückwärts (Abhängige):** Verifiziert per Grep auf `extends FrontendSearchHighlighter` über
den gesamten `web-common/src/main/java/de/makno/web/common/component/`-Baum — **kein
Treffer** (erwartungsgemäß, die Klasse ist `final`).

# Konstruktoren

## `FrontendSearchHighlighter(Component host)`

```java
public FrontendSearchHighlighter(Component host)
```

- `host` (`Component`) — null-erlaubt: **nein**. Verifiziert durch
  `this.host = Objects.requireNonNull(host, "host");` — wirft `NullPointerException` mit
  Nachricht `"host"` bei `null`.
- **Geworfene Exceptions:** `NullPointerException`, wenn `host == null`.
- Die Wurzel-Element von `host` (`this` im JS-Kontext) dient dem Highlighter als
  Geltungsbereich; mehrere Komponenten je Seite werden über dieses Wurzel-Scoping
  unterstützt.

# Methoden

## `render(List<TokenMatch> matches, int currentIndex)` <a id="render"></a>

```java
@Override
public void render(List<TokenMatch> matches, int currentIndex)
```

- `matches` (`List<TokenMatch>`) — null-erlaubt: **nein, faktisch nicht geprüft, aber
  würde zur NPE führen**. Es gibt keinen expliziten Null-Check im Methodenbody; `matches`
  wird direkt an die private Hilfsmethode `toFlatArray(matches)` weitergereicht, die per
  erweiterter `for`-Schleife (`for (TokenMatch match : matches)`) iteriert — bei
  `matches == null` würde dies eine `NullPointerException` auslösen (implizit über
  `Iterable.iterator()` auf `null`). In der Praxis unkritisch, da der einzige Aufrufer
  ([SearchController](/api-reference/search-controller.md)) immer eine nicht-null
  `List` (ggf. `List.of()`) übergibt.
- `currentIndex` (`int`) — null-erlaubt: entfällt (primitiver Typ). Kein Bereichs-Check;
  wird unverändert als JS-Parameter `$1` übergeben.
- **Verhalten bei fehlender UI:** Ist `host.getUI()` leer (`Optional.isEmpty()`), kehrt
  die Methode sofort zurück (No-op) — kein Aufruf von `executeJs`.
- **Rückgabewert:** `void`.
- **Geworfene Exceptions:** implizite `NullPointerException`, falls `matches == null`
  (siehe oben); sonst keine expliziten `throw`-Statements im Methodenbody.

## `moveCurrent(int currentIndex)`

```java
@Override
public void moveCurrent(int currentIndex)
```

- `currentIndex` (`int`) — null-erlaubt: entfällt (primitiver Typ).
- Ist `host.getUI()` leer, kehrt die Methode sofort zurück (No-op).
- **Rückgabewert:** `void`.
- **Geworfene Exceptions:** keine expliziten `throw`-Statements.

## `clear()`

```java
@Override
public void clear()
```

- Keine Parameter.
- Ist `host.getUI()` leer, kehrt die Methode sofort zurück (No-op).
- **Rückgabewert:** `void`.
- **Geworfene Exceptions:** keine expliziten `throw`-Statements.

## `toFlatArray(List<TokenMatch> matches)` (package-private/private Hilfsmethode)

```java
private JsonArray toFlatArray(List<TokenMatch> matches)
```

- `matches` (`List<TokenMatch>`) — null-erlaubt: **nein, faktisch nicht geprüft**. Kein
  expliziter Null-Check; die erweiterte `for`-Schleife über `matches` würde bei `null`
  eine `NullPointerException` auslösen. Aufrufer ist ausschließlich `render(...)` in
  dieser Klasse (siehe oben).
- Iteriert über jeden `TokenMatch` in `matches` und schreibt `tokenIndex()`, `start()`,
  `end()` sequenziell in ein `JsonArray` (Format: `[tokenIndex, start, end, tokenIndex,
  start, end, …]`).
- **Rückgabewert:** `JsonArray`, niemals `null` — auch bei leerer `matches`-Liste wird ein
  (leeres) `JsonArray`-Objekt über `Json.createArray()` erzeugt und zurückgegeben.
- **Geworfene Exceptions:** implizite `NullPointerException`, falls `matches == null`
  (siehe oben).

# Citations

[1] web-common/src/main/java/de/makno/web/common/component/search/FrontendSearchHighlighter.java
