---
type: API Reference
title: RenderedTree – Konstruktoren
description: Alle Konstruktoren von RenderedTree.
resource: web-common/src/main/java/de/makno/web/common/component/xmlviewer/RenderedTree.java
tags: [api-reference, constructor]
timestamp: '2026-07-08T09:00:00+02:00'
---


Kanonischer (impliziter) Record-Konstruktor:

```java
RenderedTree(
    Div root,
    Map<Element, Div> elementHeaders,
    Map<Element, Div> childContainers,
    Map<Element, Div> endTags,
    Map<Element, Span> toggles,
    List<SearchableToken> tokens)
```

Es gibt **keinen** kompakten Konstruktor (compact constructor) im Quellcode — der Record
verwendet ausschließlich den impliziten kanonischen Konstruktor, der die Werte 1:1 in die
Komponenten übernimmt. Es findet **keine Validierung** statt.

- `root` (`Div`) — null-erlaubt: ja, verifiziert durch Fehlen jeglicher Prüfung im Record
  und durch `XmlViewer.emptyTree()`, das zwar stets ein `new Div()` (nie `null`) übergibt,
  aber der Record selbst würde `null` klaglos akzeptieren.
- `elementHeaders` (`Map<Element, Div>`) — null-erlaubt: ja (keine Prüfung); wird
  intern von `XmlTreeRenderer` immer als (ggf. leere) `IdentityHashMap` bzw. von
  `XmlViewer.emptyTree()` als `Collections.emptyMap()` übergeben, nie als `null` beobachtet,
  aber der Konstruktor selbst prüft nicht.
- `childContainers` (`Map<Element, Div>`) — null-erlaubt: ja, gleiche Begründung wie oben.
- `endTags` (`Map<Element, Div>`) — null-erlaubt: ja, gleiche Begründung wie oben.
- `toggles` (`Map<Element, Span>`) — null-erlaubt: ja, gleiche Begründung wie oben.
- `tokens` (`List<SearchableToken>`) — null-erlaubt: ja (keine Prüfung); wird stets als
  (ggf. leere) `ArrayList` bzw. `List.of()` übergeben.

**Was bei ungültiger Eingabe passiert:** Nichts im Konstruktor selbst — kein `throw`,
keine `Objects.requireNonNull`-Aufrufe. Wird z.&nbsp;B. `null` für eine der Maps übergeben,
schlägt das nicht hier fehl, sondern erst später bei einem Zugriff über die generierten
Record-Zugriffsmethoden (z.&nbsp;B. `tree.elementHeaders().get(element)` in `XmlViewer`
würde eine `NullPointerException` werfen, wenn `elementHeaders` selbst `null` wäre).

# Citations

[1] [RenderedTree (Übersicht)](./rendered-tree.md)
