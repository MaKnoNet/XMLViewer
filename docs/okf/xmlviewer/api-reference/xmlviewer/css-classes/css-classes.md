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
[XmlTreeRenderer](/api-reference/xmlviewer/xml-tree-renderer/xml-tree-renderer.md) beim Rendern setzt und die
[XmlViewer](/api-reference/xmlviewer/xml-viewer/xml-viewer.md) zur Laufzeit umschaltet (z.&nbsp;B.
`HIGHLIGHT`, `TOGGLE_COLLAPSED`), damit keine Magic-Strings über den Code verstreut sind.

**Thread-Safety:** Alle Felder sind `static final String` — unveränderliche Konstanten,
kein Zustand. Die Klasse ist damit trivial thread-sicher; sie hält keine Instanzdaten und
kann von beliebig vielen Threads gleichzeitig gelesen werden, ohne dass Synchronisation
nötig wäre.

Die Werte müssen exakt mit `web/common/component/xmlviewer/styles/xml-viewer.css` (fast
alle Konstanten) und mit `TOKEN_SELECTOR` in `search/search-highlighter.js`
(`SEARCH_TOKEN`) übereinstimmen — das ist eine externe Konvention, keine vom Compiler
geprüfte Invariante.

# Felder

| Feld | Typ | Bedeutung | null-erlaubt |
|---|---|---|---|
| `ROOT` | `static final String` | Wurzel-Container-Klasse der Komponente (`"xmlviewer"`). | entfällt (String-Literal-Konstante) |
| `TREE` | `static final String` | Scrollbarer Baum-Container (`"xmlviewer-tree"`). | entfällt |
| `EMPTY` | `static final String` | Platzhaltertext-Klasse, wenn kein Wurzelelement gesetzt ist (`"xmlviewer-empty"`). | entfällt |
| `LINE` | `static final String` | Eine Zeile: Start-Tag, Text, Kommentar oder End-Tag (`"xml-line"`). | entfällt |
| `CHILDREN` | `static final String` | Eingerückter Container für die Kindknoten (`"xml-children"`). | entfällt |
| `ENDTAG` | `static final String` | Schließende-Tag-Zeile (`"xml-endtag"`). | entfällt |
| `ENDTAG_MARKER` | `static final String` | Zentrierte Marker-Box am Ende der Zeile; das Symbol liefert CSS `::before` (`"xml-endtag-marker"`). | entfällt |
| `TOGGLE` | `static final String` | Klickbares Auf-/Zuklapp-Dreieck, Marker kommt aus CSS `::before` (`"xml-toggle"`). | entfällt |
| `TOGGLE_COLLAPSED` | `static final String` | Zustands-Modifier am Toggle: gesetzt = zugeklappt, wählt per CSS den Collapsed-Marker (`"xml-toggle--collapsed"`). | entfällt |
| `INDENT` | `static final String` | Einrückungs-Platzhalter, gleiche Breite wie das Dreieck (`"xml-indent"`). | entfällt |
| `RAIL` | `static final String` | Einrückungs-/Führungslinienzelle pro Vorfahr-Ebene, gleiche Breite wie der Toggle (`"xml-rail"`). | entfällt |
| `TAG` | `static final String` | Tag-Name (`"xml-tag"`). | entfällt |
| `ATTR_NAME` | `static final String` | Attributname (`"xml-attr-name"`). | entfällt |
| `ATTR_VALUE` | `static final String` | Attributwert (`"xml-attr-value"`). | entfällt |
| `TEXT` | `static final String` | Textinhalt / CDATA (`"xml-text"`). | entfällt |
| `COMMENT` | `static final String` | Kommentar (`"xml-comment"`). | entfällt |
| `PUNCT` | `static final String` | Satzzeichen `< > / = "` (`"xml-punct"`). | entfällt |
| `HIGHLIGHT` | `static final String` | Von `XmlViewer.highlight(Element)` hervorgehobenes Element (`"xml-highlight"`). | entfällt |
| `SEARCH_TOKEN` | `static final String` | Markiert einen durchsuchbaren Token-Span; muss exakt `TOKEN_SELECTOR` in `search/search-highlighter.js` entsprechen (`"search-token"`). | entfällt |

Alle 18 Felder sind `static final String`-Konstanten mit String-Literal-Initialisierung — nie
`null`, da String-Literale zur Compile-Zeit im Konstanten-Pool liegen. Die Klasse hat keine
Instanzfelder (kein Objekt wird je instanziiert, siehe Konstruktor).

# Thread-Safety

**Trivial thread-sicher** (verifiziert): Alle 18 Felder sind `static final String` —
unveränderliche Konstanten ohne jeglichen Instanzzustand. Beliebig viele Threads können sie
gleichzeitig lesen, ohne dass Synchronisation nötig wäre. Die Klasse besitzt zusätzlich einen
privaten, leeren Konstruktor (`private CssClasses() {}`), der Instanziierung verhindert — es
gibt also nie ein Objekt, dessen Zustand geteilt werden könnte.

# Serialisierung

Nicht `Serializable` — `CssClasses` implementiert kein Serialisierungs-Interface (verifiziert
gegen die Klassendeklaration `final class CssClasses`, kein `extends`/`implements`). Da die
Klasse ohnehin nicht instanziierbar ist (privater No-Op-Konstruktor), stellt sich die Frage der
Objekt-Serialisierung nicht; die einzelnen `String`-Konstanten sind über den JDK-Typ `String`
selbst serialisierbar, was aber keine Eigenschaft von `CssClasses` ist.

# equals/hashCode/toString

Keine dieser Methoden ist überschrieben (verifiziert: keine `equals`/`hashCode`/`toString`-
Deklaration im Quellcode) — es gilt die **Identitätssemantik von `java.lang.Object`**. Da die
Klasse nie instanziiert wird (privater Konstruktor ohne Aufrufer), ist diese Semantik in der
Praxis irrelevant; es existiert kein Objekt, auf dem diese Methoden je aufgerufen würden.

# Vererbungshierarchie


**Vorwärts (eigene Deklaration):** `final class CssClasses` (package-private, kein
`extends`/`implements`).

- **Superklasse:** keine explizite (impliziter `Object`).
- **Interfaces:** keine.
- Die Klasse ist `final` und package-private — keine Subklasse möglich.

**Rückwärts (Abhängige):** Verifiziert per Grep auf `extends CssClasses` über den gesamten
`web-common/src/main/java/de/makno/web/common/component/`-Baum — **kein Treffer**.
`CssClasses` hat keine projektinterne Vererbungsbeziehung; ihre Konstanten werden von
[XmlTreeRenderer](/api-reference/xmlviewer/xml-tree-renderer/xml-tree-renderer.md) und
[XmlViewer](/api-reference/xmlviewer/xml-viewer/xml-viewer.md) gelesen (Verwendung als Konstante, keine
Vererbung).

# Konstruktoren

- [siehe constructor.md](./constructor.md)

# Methoden


# Citations


[1] web-common/src/main/java/de/makno/web/common/component/xmlviewer/CssClasses.java
