---
type: API Reference
title: FrontendSearchHighlighter.render(...)
description: Methode render von FrontendSearchHighlighter - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/search/FrontendSearchHighlighter.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

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
  ([SearchController](/api-reference/search/search-controller/search-controller.md)) immer eine nicht-null
  `List` (ggf. `List.of()`) übergibt.
- `currentIndex` (`int`) — null-erlaubt: entfällt (primitiver Typ). Kein Bereichs-Check;
  wird unverändert als JS-Parameter `$1` übergeben.
- **Verhalten bei fehlender UI:** Ist `host.getUI()` leer (`Optional.isEmpty()`), kehrt
  die Methode sofort zurück (No-op) — kein Aufruf von `executeJs`.
- **Rückgabewert:** `void`.
- **Geworfene Exceptions:** implizite `NullPointerException`, falls `matches == null`
  (siehe oben); sonst keine expliziten `throw`-Statements im Methodenbody.

# Citations

[1] [FrontendSearchHighlighter (Übersicht)](./frontend-search-highlighter.md)
