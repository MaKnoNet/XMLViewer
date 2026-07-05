---
type: API Reference
title: SearchTermSplitter.split(...)
description: Methode split von SearchTermSplitter - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/search/SearchTermSplitter.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `split(String query)`


Die einzige abstrakte Methode des funktionalen Interfaces.

```java
List<String> split(String query);
```

- `query` (`String`) — null-erlaubt: **ja**, laut Javadoc-Vertrag
  (`@param query der eingegebene Suchtext (kann leer oder {@code null} sein)`). Da es
  sich um eine Methodendeklaration ohne Body handelt, gibt es keinen Code, der `null`
  serverseitig abweist — die Nulltoleranz ist Vertragssache jeder Implementierung.
  Verifiziert an der Standardimplementierung
  [`SearchController.DEFAULT_TERM_SPLITTER`](/api-reference/search/search-controller/search-controller.md):
  dort wird `query == null` explizit geprüft und mit `List.of()` beantwortet, statt eine
  Exception zu werfen — die Standardimplementierung hält den dokumentierten Vertrag ein.
- **Rückgabewert:** `List<String>` — laut Interface-Javadoc „die zu
  suchenden/hervorzuhebenden Begriffe; eine leere Liste bedeutet 'keine Suche'". Ob `null`
  als Rückgabewert zulässig ist, legt das Interface selbst nicht fest (keine Prüfung im
  Interface möglich, da abstrakte Methode). Der Konsument
  [SearchController#splitTerms(String)](/api-reference/search/search-controller/split-terms.md)
  behandelt einen `null`-Rückgabewert defensiv: `if (terms == null) { return List.of(); }`
  — das bestätigt, dass Implementierungen `null` liefern *könnten*, auch wenn das nicht
  der dokumentierte Normalfall ist.
- **Geworfene Exceptions:** vom Interface selbst keine deklariert (`throws`-Klausel
  fehlt); eine konkrete Implementierung kann natürlich beliebige `RuntimeException`s
  werfen.

# Citations

[1] [SearchTermSplitter (Übersicht)](./search-term-splitter.md)
