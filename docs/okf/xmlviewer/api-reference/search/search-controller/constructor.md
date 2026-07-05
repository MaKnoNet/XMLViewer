---
type: API Reference
title: SearchController – Konstruktoren
description: Alle Konstruktoren von SearchController.
resource: web-common/src/main/java/de/makno/web/common/component/search/SearchController.java
tags: [api-reference, constructor]
timestamp: '2026-07-08T09:00:00+02:00'
---


## `SearchController(List<SearchToken> tokens, SearchHighlightRenderer highlightRenderer, SerializableRunnable onMatchChange)`

```java
public SearchController(
        List<SearchToken> tokens, SearchHighlightRenderer highlightRenderer, SerializableRunnable onMatchChange) {
    this.tokens = tokens;
    this.highlightRenderer = highlightRenderer;
    this.onMatchChange = onMatchChange;
}
```

- `tokens` (`List<SearchToken>`) — null-erlaubt: **ja, faktisch (keine Prüfung)**. Der
  Konstruktor weist den Parameter ungeprüft direkt dem Feld `this.tokens` zu — kein
  `Objects.requireNonNull`, kein `if`-Check. Wird `null` übergeben, schlägt die erste
  Suche fehl: `collectMatches` iteriert per `tokens.size()`/`tokens.get(index)` über die
  Liste und würde eine `NullPointerException` erst **später**, beim ersten Aufruf von
  `search(...)`, auslösen — nicht sofort im Konstruktor. Dies ist eine **Diskrepanz** zum
  impliziten Vertrag ("gehört zu genau einer Komponente"), da nirgends im Konstruktor
  dokumentiert oder erzwungen wird, dass `tokens` nicht `null` sein darf.
- `highlightRenderer` (`SearchHighlightRenderer`) — null-erlaubt: **ja, faktisch (keine
  Prüfung)**. Ebenfalls ungeprüfte Direktzuweisung. `null` würde erst beim ersten Aufruf
  von `search(...)` (`highlightRenderer.render(...)`) oder `clearSearch()`
  (`highlightRenderer.clear()`) eine `NullPointerException` auslösen — nicht im
  Konstruktor.
- `onMatchChange` (`SerializableRunnable`) — null-erlaubt: **ja, faktisch (keine
  Prüfung)**. Ungeprüfte Direktzuweisung. `null` würde erst bei `notifyChange()`
  (`onMatchChange.run()`) eine `NullPointerException` auslösen, ausgelöst durch jeden
  Aufruf von `search(...)`, `clearSearch()` oder `moveCurrentTo(...)`.
- **Geworfene Exceptions:** keine — der Konstruktorbody besteht ausschließlich aus drei
  Feldzuweisungen ohne jede Prüfung.

Im Gegensatz dazu prüfen die Setter `setTermSplitter(...)` (siehe unten) ihren Parameter
sehr wohl per `Objects.requireNonNull` — die fehlende Prüfung im Konstruktor für die drei
oben genannten Parameter ist also eine bewusste oder unbewusste Inkonsistenz innerhalb
derselben Klasse.

# Citations

[1] [SearchController (Übersicht)](./search-controller.md)
