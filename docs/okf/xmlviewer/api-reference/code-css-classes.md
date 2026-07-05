---
type: API Reference
title: CodeCssClasses
description: Package-private Konstantenklasse mit dem einzigen CSS-Klassennamen des CodeViewer-Host-Elements.
resource: web-common/src/main/java/de/makno/web/common/component/code/CodeCssClasses.java
tags: [api-reference, code, css, constants]
timestamp: '2026-07-07T10:00:00+02:00'
---

# Überblick

`CodeCssClasses` ist eine **package-private, zustandslose Konstantenklasse** (`final class`, nicht
`public`) im Package `de.makno.web.common.component.code`. Sie enthält genau eine Konstante für den
[CodeViewer](/components/codeviewer.md). Das eigentliche Editor-Styling liefert CodeMirror selbst
per JS-Injektion; diese Klasse liefert nur den Rahmen des Host-Elements. Da ausschließlich
`static final`-Felder vorhanden sind, ist die Klasse **thread-safe by design** — es gibt keinen
veränderlichen Zustand.

# Konstruktoren

## `private CodeCssClasses()`

Leerer, privater Konstruktor ohne Parameter, verhindert Instanziierung. Wird nie aufgerufen.

# Felder (Konstanten)

| Konstante | Wert | Bedeutung |
|---|---|---|
| `ROOT` | `"codeviewer"` | Wurzel-/Host-Element, in das CodeMirror den Editor rendert. |

Die Konstante ist ein Compile-Zeit-Stringliteral und kann nicht `null` sein.

# Methoden

Keine Methoden außer dem privaten Konstruktor.

# Cross-Referenzen

- [CodeViewer (Narrative)](/components/codeviewer.md)
- [Frontend-Integration](/architecture/frontend-integration.md)

# Citations

[1] `web-common/src/main/java/de/makno/web/common/component/code/CodeCssClasses.java`
