---
type: API Reference
title: SearchTermSplitter
description: Funktionales Interface, das den Sucheingabe-Text in einzelne hervorzuhebende Begriffe zerlegt; das Trennverhalten ist frei austauschbar.
resource: web-common/src/main/java/de/makno/web/common/component/search/SearchTermSplitter.java
tags: [api-reference, search, functional-interface]
timestamp: '2026-07-07T10:00:00+02:00'
---

# Überblick

`SearchTermSplitter` ist ein `@FunctionalInterface`, das `Serializable` erweitert (damit
Lambda-Implementierungen als Vaadin-Session-Zustand serialisierbar bleiben). Es entkoppelt
die Trennlogik für Suchbegriffe vom [SearchController](/api-reference/search-controller.md):
die einbettende Anwendung kann z.&nbsp;B. an Leerzeichen (Standard), an Komma oder gar
nicht trennen (gesamter Text als ein Begriff). Details zur Architektur in
[Geteilte Such-Engine](/architecture/search-engine.md).

**Thread-Safety:** Das Interface selbst ist zustandslos. Ob eine konkrete Implementierung
thread-sicher ist, hängt von ihr ab — die mitgelieferte Standardimplementierung
`SearchController.DEFAULT_TERM_SPLITTER` ist eine reine, zustandslose Lambda und damit
gefahrlos teilbar.

# Methoden

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
  [`SearchController.DEFAULT_TERM_SPLITTER`](/api-reference/search-controller.md#default_term_splitter):
  dort wird `query == null` explizit geprüft und mit `List.of()` beantwortet, statt eine
  Exception zu werfen — die Standardimplementierung hält den dokumentierten Vertrag ein.
- **Rückgabewert:** `List<String>` — laut Interface-Javadoc „die zu
  suchenden/hervorzuhebenden Begriffe; eine leere Liste bedeutet 'keine Suche'". Ob `null`
  als Rückgabewert zulässig ist, legt das Interface selbst nicht fest (keine Prüfung im
  Interface möglich, da abstrakte Methode). Der Konsument
  [SearchController#splitTerms(String)](/api-reference/search-controller.md#splitterms)
  behandelt einen `null`-Rückgabewert defensiv: `if (terms == null) { return List.of(); }`
  — das bestätigt, dass Implementierungen `null` liefern *könnten*, auch wenn das nicht
  der dokumentierte Normalfall ist.
- **Geworfene Exceptions:** vom Interface selbst keine deklariert (`throws`-Klausel
  fehlt); eine konkrete Implementierung kann natürlich beliebige `RuntimeException`s
  werfen.

# Citations

[1] web-common/src/main/java/de/makno/web/common/component/search/SearchTermSplitter.java
