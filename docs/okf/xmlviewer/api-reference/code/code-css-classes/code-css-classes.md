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

# Felder

| Feld | Typ | Bedeutung | null-erlaubt |
|---|---|---|---|
| `ROOT` | `static final String` | CSS-Klassenname `"codeviewer"` des Wurzel-/Host-Elements, in das CodeMirror den Editor rendert. | nein — Literal, nie `null` |

# Thread-Safety

**Thread-safe by design** (verifiziert): ausschließlich ein `static final String`-Feld, kein
veränderlicher Zustand, kein Instanzverhalten. Der private Konstruktor verhindert zudem jede
Instanziierung.

# Serialisierung

Nicht `Serializable` — `CodeCssClasses` implementiert kein Serialisierungs-Interface
(verifiziert gegen die Klassendeklaration `final class CodeCssClasses`). Kein
Serialisierungs-Vertrag.

# equals/hashCode/toString

Keine dieser Methoden ist überschrieben (verifiziert: keine `equals`/`hashCode`/`toString`-
Deklaration im Quellcode) — es gilt die **Identitätssemantik von `java.lang.Object`**. Da die
Klasse nie instanziiert wird (privater Konstruktor, nur `static final`-Zugriff), hat das keine
praktische Relevanz.

# Vererbungshierarchie


**Vorwärts (eigene Deklaration):** `final class CodeCssClasses` (package-private, kein
`extends`/`implements`).

- **Superklasse:** keine explizite (impliziter `Object`).
- **Interfaces:** keine.
- Die Klasse ist `final` und package-private — keine Subklasse möglich.

**Rückwärts (Abhängige):** Verifiziert per Grep auf `extends CodeCssClasses` über den
gesamten `web-common/src/main/java/de/makno/web/common/component/`-Baum — **kein Treffer**.
`CodeCssClasses` hat keine projektinterne Vererbungsbeziehung; sie ist eine reine
Konstantenklasse, deren Werte von [CodeViewer](/api-reference/code/code-viewer/code-viewer.md) gelesen werden
(Verwendung als Konstante, keine Vererbung).

# Konstruktoren

- [siehe constructor.md](./constructor.md)

# Methoden


# Citations


[1] `web-common/src/main/java/de/makno/web/common/component/code/CodeCssClasses.java`
