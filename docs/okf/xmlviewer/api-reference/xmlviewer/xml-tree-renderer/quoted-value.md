---
type: API Reference
title: XmlTreeRenderer.quotedValue(...)
description: Methode quotedValue von XmlTreeRenderer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/xmlviewer/XmlTreeRenderer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## quotedValue (private)


```java
private Span quotedValue(String value, Element owner)
```

- `value` (`String`) — null-erlaubt: ja im Sinne, dass die Methode selbst nicht direkt
  darauf zugreift (übergibt `value` unverändert an `token(...)`, welches `null` intern zu
  `""` normalisiert — siehe [token](#token-private) unten). Kein NPE-Risiko in dieser
  Methode selbst.
- `owner` (`Element`) — null-erlaubt: ja im Sinne, dass diese Methode `owner` nur
  durchreicht (an `token(...)`, das `owner` nur als Feld in `SearchableToken` speichert,
  ohne es zu dereferenzieren).
- Rückgabewert (`Span`) — nie `null`: Es wird immer ein neuer `Span` (`group`) konstruiert
  und mit vier Kindspans befüllt.
- Geworfene Exceptions: keine.
- Baut `="wert"` als Span-Folge (Anführungszeichen und Gleichheitszeichen als
  Satzzeichen-Spans, Wert als durchsuchbares Token).

# Citations

[1] [XmlTreeRenderer (Übersicht)](./xml-tree-renderer.md)
