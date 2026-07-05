---
type: API Reference
title: CssClasses
description: Package-private Konstantenklasse mit allen CSS-Klassennamen, die der XmlViewer und sein Renderer verwenden.
resource: web-common/src/main/java/de/makno/web/common/component/xmlviewer/CssClasses.java
tags: [api-reference, xmlviewer, css, constants]
timestamp: '2026-07-07T10:00:00+02:00'
---

# Überblick

`CssClasses` ist eine finale, nicht instanziierbare Konstantenklasse (`final class` mit
private Konstruktor ohne Body) im package-private Sichtbarkeitsbereich von
`de.makno.web.common.component.xmlviewer`. Sie bündelt alle CSS-Klassennamen, die
[XmlTreeRenderer](/api-reference/xml-tree-renderer.md) beim Rendern setzt und die
[XmlViewer](/api-reference/xml-viewer.md) zur Laufzeit umschaltet (z.&nbsp;B.
`HIGHLIGHT`, `TOGGLE_COLLAPSED`), damit keine Magic-Strings über den Code verstreut sind.

**Thread-Safety:** Alle Felder sind `static final String` — unveränderliche Konstanten,
kein Zustand. Die Klasse ist damit trivial thread-sicher; sie hält keine Instanzdaten und
kann von beliebig vielen Threads gleichzeitig gelesen werden, ohne dass Synchronisation
nötig wäre.

Die Werte müssen exakt mit `web/common/component/xmlviewer/styles/xml-viewer.css` (fast
alle Konstanten) und mit `TOKEN_SELECTOR` in `search/search-highlighter.js`
(`SEARCH_TOKEN`) übereinstimmen — das ist eine externe Konvention, keine vom Compiler
geprüfte Invariante.

# Konstruktoren

```java
private CssClasses() {}
```

- Keine Parameter.
- Leerer Konstruktor-Body: kein Feld wird gesetzt, keine Validierung möglich oder nötig.
- Zweck ist ausschließlich, die Instanziierung der Klasse zu verhindern (Utility-Class-Pattern).
  Da der Konstruktor `private` ist, kann er nur von innerhalb der Klasse selbst aufgerufen
  werden — im Code geschieht das nirgends, die Klasse wird nie instanziiert.
- Wirft nichts, kann nichts scheitern lassen (leerer Body).

# Methoden

Keine Methoden. Die Klasse besteht ausschließlich aus `static final String`-Konstanten
und dem privaten Konstruktor. Die Konstanten selbst (kein Verhalten, keine Berechnung):

| Konstante | Wert | Verwendung |
|---|---|---|
| `ROOT` | `"xmlviewer"` | Wurzel-Container der Komponente |
| `TREE` | `"xmlviewer-tree"` | Scrollbarer Baum-Container |
| `EMPTY` | `"xmlviewer-empty"` | Platzhaltertext ohne Wurzelelement |
| `LINE` | `"xml-line"` | Eine Zeile (Start-Tag, Text, Kommentar, End-Tag) |
| `CHILDREN` | `"xml-children"` | Eingerückter Kindknoten-Container |
| `ENDTAG` | `"xml-endtag"` | Schließende-Tag-Zeile |
| `ENDTAG_MARKER` | `"xml-endtag-marker"` | Marker-Box am Zeilenende (Symbol aus CSS `::before`) |
| `TOGGLE` | `"xml-toggle"` | Klickbares Auf-/Zuklapp-Dreieck |
| `TOGGLE_COLLAPSED` | `"xml-toggle--collapsed"` | Zustands-Modifier: gesetzt = zugeklappt |
| `INDENT` | `"xml-indent"` | Einrückungs-Platzhalter (Breite wie Dreieck) |
| `RAIL` | `"xml-rail"` | Einrückungs-/Führungslinien-Zelle pro Vorfahr-Ebene |
| `TAG` | `"xml-tag"` | Tag-Name |
| `ATTR_NAME` | `"xml-attr-name"` | Attributname |
| `ATTR_VALUE` | `"xml-attr-value"` | Attributwert |
| `TEXT` | `"xml-text"` | Textinhalt / CDATA |
| `COMMENT` | `"xml-comment"` | Kommentar |
| `PUNCT` | `"xml-punct"` | Satzzeichen (`< > / = "`) |
| `HIGHLIGHT` | `"xml-highlight"` | Per `XmlViewer#highlight` hervorgehobenes Element |
| `SEARCH_TOKEN` | `"search-token"` | Durchsuchbarer Token-Span (Frontend-Highlighting) |

Alle Felder sind package-private oder `static` ohne Zugriffsmodifikator-Aufwand außer
`static final` — de facto package-private, da die Klasse selbst package-private ist.

# Citations

[1] web-common/src/main/java/de/makno/web/common/component/xmlviewer/CssClasses.java
