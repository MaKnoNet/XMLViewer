---
type: API Reference
title: CssClasses – Konstruktoren
description: Alle Konstruktoren von CssClasses.
resource: web-common/src/main/java/de/makno/web/common/component/xmlviewer/CssClasses.java
tags: [api-reference, constructor]
timestamp: '2026-07-08T09:00:00+02:00'
---


```java
private CssClasses() {}
```

- Keine Parameter.
- Leerer Konstruktor-Body: kein Feld wird gesetzt, keine Validierung möglich oder nötig.
- Zweck ist ausschließlich, die Instanziierung der Klasse zu verhindern (Utility-Class-Pattern).
  Da der Konstruktor `private` ist, kann er nur von innerhalb der Klasse selbst aufgerufen
  werden — im Code geschieht das nirgends, die Klasse wird nie instanziiert.
- Wirft nichts, kann nichts scheitern lassen (leerer Body).

# Citations

[1] [CssClasses (Übersicht)](./css-classes.md)
