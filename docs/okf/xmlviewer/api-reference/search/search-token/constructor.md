---
type: API Reference
title: SearchToken – Konstruktoren
description: Alle Konstruktoren von SearchToken.
resource: web-common/src/main/java/de/makno/web/common/component/search/SearchToken.java
tags: [api-reference, constructor]
timestamp: '2026-07-08T09:00:00+02:00'
---


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

# Citations

[1] [SearchToken (Übersicht)](./search-token.md)
