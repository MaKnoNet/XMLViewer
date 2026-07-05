---
type: API Reference
title: SearchToken.of(...)
description: Methode of von SearchToken - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/search/SearchToken.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

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

**Hinweis zur Deduplizierung:** [SearchController](/api-reference/search/search-controller/search-controller.md)
nutzt Identitätsvergleich (`IdentityHashMap`) auf `onReveal`, um beim Sichtbarmachen von
Treffern mehrere Tokens desselben Elements nur einmal aufzuklappen. Da `NO_REVEAL` eine
einzige geteilte Instanz ist, werden alle über `of(...)` erzeugten Tokens automatisch als
"dieselbe Reveal-Aktion" erkannt und dedupliziert — ein beabsichtigter Nebeneffekt der
gemeinsamen Konstante.

# Citations

[1] [SearchToken (Übersicht)](./search-token.md)
