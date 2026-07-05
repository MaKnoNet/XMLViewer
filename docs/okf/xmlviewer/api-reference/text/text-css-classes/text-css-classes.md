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

# Felder

| Feld | Typ | Bedeutung | null-erlaubt |
|---|---|---|---|
| `ROOT` | `static final String` | `"textviewer"` — CSS-Klasse des Wurzel-Containers der Komponente (gesetzt auf `getContent()` in `TextViewer`). | entfällt (primitiv `String`-Konstante, nie `null`) |
| `CONTENT` | `static final String` | `"textviewer-content"` — CSS-Klasse des scrollbaren Zeilen-Containers. | entfällt (primitiv) |
| `EMPTY` | `static final String` | `"textviewer-empty"` — CSS-Klasse des Platzhaltertexts, wenn kein Text gesetzt ist. | entfällt (primitiv) |
| `LINE` | `static final String` | `"text-line"` — CSS-Klasse einer einzelnen Textzeile (Gutter + Inhalt). | entfällt (primitiv) |
| `GUTTER` | `static final String` | `"text-gutter"` — CSS-Klasse der optionalen Zeilennummern-Spalte. | entfällt (primitiv) |
| `LINE_CONTENT` | `static final String` | `"text-line-content"` — CSS-Klasse des eigentlichen Zeilentexts (zugleich durchsuchbares Token). | entfällt (primitiv) |
| `SEARCH_TOKEN` | `static final String` | `"search-token"` — markiert den durchsuchbaren Zeilen-Span; laut Kommentar im Code mit dem `XmlViewer` geteilt und muss exakt dem `TOKEN_SELECTOR` in `search/search-highlighter.js` entsprechen. | entfällt (primitiv) |
| `HIGHLIGHT` | `static final String` | `"text-highlight"` — CSS-Klasse der per `TextViewer#highlight(int)` hervorgehobenen Zeile. | entfällt (primitiv) |
| `WRAP` | `static final String` | `"textviewer--wrap"` — Modifier-Klasse am Wurzelelement, aktiviert den Zeilenumbruch (`TextViewer#setWrap(boolean)`). | entfällt (primitiv) |

Alle Felder sind `static final String` (implizit auch package-private, da kein Modifier über
`static final` hinaus angegeben ist) — reine, zur Compile-Zeit bekannte String-Konstanten ohne
Setter oder anderen Änderungsmechanismus.

# Thread-Safety

**Thread-safe by design** (verifiziert): Die Klasse hat keinerlei veränderlichen Zustand — nur
`static final String`-Konstanten, die zur Compile-Zeit feststehen. Es gibt keine Instanzfelder,
keine Setter und keine Methoden außer dem leeren privaten Konstruktor. Da nichts an dieser
Klasse sich zur Laufzeit ändern kann, ist paralleler Zugriff aus beliebig vielen Threads ohne
Synchronisation unproblematisch.

# Serialisierung

Nicht `Serializable` — `TextCssClasses` implementiert kein Serialisierungs-Interface und erweitert
keine Klasse, die eines implementiert (verifiziert gegen die Klassendeklaration `final class
TextCssClasses`, kein `extends`/`implements`). Da die Klasse ohnehin nicht instanziierbar ist
(privater Konstruktor), stellt sich die Frage der Objekt-Serialisierung praktisch nicht; die
Konstanten selbst werden als `String`-Literale von Aufrufern direkt gelesen, nicht die Klasse als
Objekt serialisiert.

# equals/hashCode/toString

Keine dieser Methoden ist überschrieben (verifiziert: keine `equals`/`hashCode`/`toString`-
Deklaration im Quellcode) — es gilt die **Identitätssemantik von `java.lang.Object`**
(`==`-Vergleich, identitätsbasierter Hashcode). Da die Klasse nicht instanziierbar ist (privater
Konstruktor, nie aufgerufen), hat das keine praktische Relevanz — es existiert schlicht keine
Instanz, für die diese Methoden je aufgerufen würden.

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
[TextViewer](/api-reference/text/text-viewer/text-viewer.md) gelesen (Verwendung als Konstante, keine
Vererbung).

# Konstruktoren

- [siehe constructor.md](./constructor.md)

# Methoden


# Citations


[1] `web-common/src/main/java/de/makno/web/common/component/text/TextCssClasses.java`
