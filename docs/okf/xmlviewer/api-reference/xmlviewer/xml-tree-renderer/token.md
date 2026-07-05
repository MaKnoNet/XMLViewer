---
type: API Reference
title: XmlTreeRenderer.token(...)
description: Methode token von XmlTreeRenderer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/xmlviewer/XmlTreeRenderer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## token (private)


```java
private Span token(String cssClass, String text, Element owner)
```

- `cssClass` (`String`) — null-erlaubt: **nein, nicht abgesichert**; wird direkt an
  `span.addClassName(cssClass)` übergeben. Vaadins `addClassName` wirft bei `null` typischerweise
  eine eigene `NullPointerException`/`IllegalArgumentException` (außerhalb dieser Klasse) —
  im Projekt wird `cssClass` immer als `CssClasses`-Konstante übergeben, nie `null`.
- `text` (`String`) — null-erlaubt: **ja, explizit abgesichert.** Der Methodenkörper enthält
  `String value = text == null ? "" : text;` — die einzige Stelle in dieser Klasse, die
  `null` aktiv zu einem sicheren Default (leerer String) normalisiert.
- `owner` (`Element`) — null-erlaubt: ja im Sinne, dass diese Methode `owner` nur in ein
  neues `SearchableToken` einbettet, ohne es zu dereferenzieren.
- Rückgabewert (`Span`) — nie `null`; erzeugt immer einen neuen `Span`, registriert ihn in
  `tokens` und gibt ihn zurück.
- Geworfene Exceptions: keine explizit in dieser Methode; potenziell NPE über
  `span.addClassName(cssClass)`, falls `cssClass == null` (im Projekt nicht erreichbar).
- Nebenwirkung: fügt ein neues [SearchableToken](/api-reference/xmlviewer/searchable-token/searchable-token.md) der
  Instanzliste `tokens` hinzu (Aufbau der späteren Suchreihenfolge, Dokumentreihenfolge ==
  Einfügereihenfolge).

# Citations

[1] [XmlTreeRenderer (Übersicht)](./xml-tree-renderer.md)
