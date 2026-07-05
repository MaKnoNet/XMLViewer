---
type: API Reference
title: SearchableToken – Konstruktoren
description: Alle Konstruktoren von SearchableToken.
resource: web-common/src/main/java/de/makno/web/common/component/xmlviewer/SearchableToken.java
tags: [api-reference, constructor]
timestamp: '2026-07-08T09:00:00+02:00'
---


Kanonischer (impliziter) Record-Konstruktor:

```java
SearchableToken(Span span, String text, Element owner)
```

Kein kompakter Konstruktor im Quellcode — der kanonische Konstruktor übernimmt die Werte
unverändert, **keine Validierung**:

- `span` (`Span`) — null-erlaubt: ja, verifiziert durch Fehlen jeglicher Prüfung. In der
  Praxis erzeugt `XmlTreeRenderer.token(...)` immer einen echten `new Span(value)`, nie
  `null`, aber der Record-Konstruktor selbst würde `null` nicht ablehnen.
- `text` (`String`) — null-erlaubt: ja laut Konstruktor (keine Prüfung). Praktisch wird
  beim einzigen Aufrufer (`XmlTreeRenderer.token`) vorher `text == null ? "" : text`
  angewendet, sodass `text` im gerenderten Baum nie `null` ist — diese Absicherung liegt
  aber im Aufrufer, nicht im Record selbst.
- `owner` (`Element`) — null-erlaubt: ja laut Konstruktor (keine Prüfung); im Projekt
  stets das jeweilige `org.jdom2.Element`, dem der Token-Text zugeordnet ist (nie `null`
  beobachtet, aber nicht erzwungen).

**Was bei ungültiger Eingabe passiert:** Nichts im Konstruktor — kein `throw`. Wird z.&nbsp;B.
`owner = null` übergeben, wirkt sich das erst später aus: `XmlViewer.toSearchTokens(...)`
ruft `token.owner()` als Schlüssel eines `IdentityHashMap` auf — `IdentityHashMap` erlaubt
`null`-Schlüssel, sodass dies nicht sofort scheitert, aber `expandTo(null)` würde in der
`for`-Schleife (`ancestor = element; ancestor != null; ...`) einfach null Iterationen
durchlaufen (kein Effekt, keine Exception).

# Citations

[1] [SearchableToken (Übersicht)](./searchable-token.md)
