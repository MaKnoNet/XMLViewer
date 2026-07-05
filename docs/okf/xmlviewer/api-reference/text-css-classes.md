---
type: API Reference
title: TextCssClasses
description: Package-private Konstantensammlung aller CSS-Klassennamen, die TextViewer verwendet.
resource: web-common/src/main/java/de/makno/web/common/component/text/TextCssClasses.java
tags: [api-reference, text, css, constants]
timestamp: '2026-07-07T10:00:00+02:00'
---

# Überblick

`TextCssClasses` ist eine **package-private, zustandslose Konstantenklasse** (`final class`, nicht
`public`) im Package `de.makno.web.common.component.text`. Sie bündelt alle CSS-Klassennamen, die
[TextViewer](/components/textviewer.md) auf seine Elemente setzt, damit diese nicht als
Magic-Strings über den Code verstreut sind. Die Klasse hat keinerlei veränderlichen Zustand (nur
`static final String`-Felder) und ist damit **thread-safe by design** — es gibt nichts, was sich zur
Laufzeit ändern könnte.

Die Klasse ist nicht instanziierbar: Ein einziger privater No-Arg-Konstruktor ohne Rumpf verhindert
Instanzen von außen; da die Klasse zusätzlich package-private ist, ist selbst dieser Konstruktor nur
innerhalb des Pakets überhaupt sichtbar.

# Vererbungshierarchie

**Vorwärts (eigene Deklaration):** `final class TextCssClasses` (package-private, kein
`extends`/`implements`).

- **Superklasse:** keine explizite (impliziter `Object`).
- **Interfaces:** keine.
- Die Klasse ist `final` und package-private, zusätzlich mit `private`-Konstruktor — keine
  Subklasse möglich.

**Rückwärts (Abhängige):** Verifiziert per Grep auf `extends TextCssClasses` über den
gesamten `web-common/src/main/java/de/makno/web/common/component/`-Baum — **kein Treffer**.
`TextCssClasses` hat keine projektinterne Vererbungsbeziehung; ihre Konstanten werden von
[TextViewer](/api-reference/text-viewer.md) gelesen (Verwendung als Konstante, keine
Vererbung).

# Konstruktoren

## `private TextCssClasses()`

Leerer, privater Konstruktor ohne Parameter. Verhindert Instanziierung (Utility-Klassen-Muster).
Wird nie aufgerufen — reine Absicherung gegen versehentliche `new TextCssClasses()`-Aufrufe.

# Felder (Konstanten)

Alle Felder sind `static final String` und damit unveränderlich; keine Methoden im eigentlichen
Sinn, aber sicherheitshalber hier vollständig aufgeführt, da sie die öffentlich relevante Schnittstelle
dieser Klasse gegenüber [TextViewer](/components/textviewer.md) bilden:

| Konstante | Wert | Bedeutung |
|---|---|---|
| `ROOT` | `"textviewer"` | Wurzel-Container der Komponente. |
| `CONTENT` | `"textviewer-content"` | Scrollbarer Zeilen-Container. |
| `EMPTY` | `"textviewer-empty"` | Platzhaltertext, wenn kein Text gesetzt ist. |
| `LINE` | `"text-line"` | Eine Textzeile (Gutter + Inhalt). |
| `GUTTER` | `"text-gutter"` | Optionale Zeilennummern-Spalte. |
| `LINE_CONTENT` | `"text-line-content"` | Der eigentliche Zeilentext (zugleich durchsuchbares Token). |
| `SEARCH_TOKEN` | `"search-token"` | Markiert den durchsuchbaren Zeilen-Span; muss exakt dem `TOKEN_SELECTOR` in `search/search-highlighter.js` entsprechen (siehe [Frontend-Integration](/architecture/frontend-integration.md)). |
| `HIGHLIGHT` | `"text-highlight"` | Per `TextViewer.highlight(int)` hervorgehobene Zeile. |
| `WRAP` | `"textviewer--wrap"` | Modifier am Wurzelelement: aktiviert den Zeilenumbruch (`TextViewer.setWrap(boolean)`). |

Keine dieser Konstanten kann `null` sein — sie sind allesamt String-Literale, zur Compile-Zeit
konstant.

# Methoden

Keine Methoden außer dem privaten Konstruktor.

# Citations

[1] `web-common/src/main/java/de/makno/web/common/component/text/TextCssClasses.java`
