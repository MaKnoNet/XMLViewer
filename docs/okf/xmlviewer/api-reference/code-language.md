---
type: API Reference
title: CodeLanguage
description: Enum der vom CodeViewer unterstützten Sprachen mit CodeMirror-6-Language-Id und typischen Datei-Endungen je Konstante.
resource: web-common/src/main/java/de/makno/web/common/component/code/CodeLanguage.java
tags: [api-reference, code, enum, codemirror]
timestamp: '2026-07-07T10:00:00+02:00'
---

# Überblick

`CodeLanguage` (`public enum CodeLanguage`) listet alle vom [CodeViewer](/components/codeviewer.md)
unterstützten Sprachen auf. Jede Konstante trägt ihre CodeMirror-6-Language-Id (vom
Frontend-Glue `code/code-viewer.js` zur Auswahl der passenden CM6-Spracherweiterung genutzt) sowie
eine Liste typischer Datei-Endungen, die [CodeLanguageDetector](/api-reference/code-language-detector.md)
zur Erkennung verwendet.

**Zustand/Thread-Safety:** Enum-Konstanten sind in Java grundsätzlich unveränderliche Singletons;
jede Konstante hat zwei `final`-Felder (`cm6Id`, `extensions`), die im Konstruktor einmalig gesetzt
werden und danach nie mehr verändert werden. `extensions` ist eine über `List.of(...)` erzeugte,
strukturell unveränderliche Liste. Die Klasse ist damit **vollständig thread-safe** — keine
Synchronisation nötig. Enums sind laut Klassen-Javadoc implizit `Serializable` (Serialisierung über
den Konstantennamen), daher bewusst ohne eigene `serialVersionUID`.

# Vererbungshierarchie

**Vorwärts (eigene Deklaration):** `public enum CodeLanguage` (kein expliziter
`extends`/`implements`-Ausdruck im Quelltext).

- **Superklasse:** implizit `java.lang.Enum<CodeLanguage>` — jedes Java-Enum erweitert
  automatisch `Enum<E>`, kein eigener `extends`-Ausdruck möglich; JDK-Typ, kein Cross-Link.
- **Interfaces:** implizit `java.io.Serializable` und `java.lang.Comparable<CodeLanguage>` —
  beide werden von `Enum<E>` geerbt, keine eigene Deklaration im Quelltext nötig; beide
  JDK-Typen, kein Cross-Link.
- Enums sind implizit `final` — es kann keine Subklasse geben.

**Rückwärts (Abhängige):** Verifiziert per Grep auf `extends CodeLanguage` über den gesamten
`web-common/src/main/java/de/makno/web/common/component/`-Baum — **kein Treffer**
(erwartungsgemäß, Enums können nicht erweitert werden). `CodeLanguage`-Werte werden von
[CodeViewer](/api-reference/code-viewer.md) gehalten und von
[CodeLanguageDetector](/api-reference/code-language-detector.md) zurückgegeben (Verwendung
als Wert, keine Vererbung).

# Enum-Konstanten

| Konstante | `cm6Id()` | Datei-Endungen | Bedeutung |
|---|---|---|---|
| `PLAIN` | `""` (leer) | *(keine)* | Kein Syntax-Highlighting (reiner Text). |
| `JAVA` | `"java"` | `java` | Java-Quelltext. |
| `CSHARP` | `"csharp"` | `cs`, `csx` | C#-Quelltext. |
| `PYTHON` | `"python"` | `py`, `pyw` | Python-Quelltext. |
| `JSON` | `"json"` | `json` | JSON-Daten. |
| `YAML` | `"yaml"` | `yaml`, `yml` | YAML-Daten. |
| `HTML` | `"html"` | `html`, `htm`, `xhtml` | HTML-Markup. |
| `XML` | `"xml"` | `xml`, `xsd`, `xsl`, `xslt`, `svg` | XML-Markup. |
| `CSS` | `"css"` | `css` | CSS-Stylesheets. |
| `JAVASCRIPT` | `"javascript"` | `js`, `mjs`, `cjs` | JavaScript-Quelltext. |
| `SQL` | `"sql"` | `sql` | SQL-Anweisungen. |

# Konstruktoren

## `CodeLanguage(String cm6Id, String... extensions)`

Package-privater (kein Modifier — für Enum-Konstruktoren ohnehin implizit `private`) Konstruktor,
nur von den Enum-Konstanten selbst aufgerufen.

| Parameter | Typ | null-erlaubt | Verifikation |
|---|---|---|---|
| `cm6Id` | `String` | in der Praxis nein, aber **kein Code-Check** | Wird ungeprüft dem Feld `cm6Id` zugewiesen (Zeile 32). Kein `Objects.requireNonNull`. Alle Aufrufstellen (die Enum-Konstanten selbst) übergeben stets ein String-Literal, nie `null` — die Absicherung existiert nur "durch Disziplin", nicht durch Code. |
| `extensions` | `String...` (Varargs) | Varargs-Array selbst kann technisch nicht `null` sein, wenn der Aufrufer keine Endungen übergibt (`PLAIN(""))` erzeugt ein leeres Array `String[0]`, nicht `null`) | `List.of(extensions)` (Zeile 33) wirft laut JDK-Doku eine `NullPointerException`, falls das Array selbst `null` wäre oder ein Element `null` enthielte — bei Aufruf mit einem expliziten `null`-Array (`(String[]) null`) via Reflection wäre das theoretisch möglich, aber über den normalen Varargs-Aufrufpfad (wie in allen 11 Konstanten) nicht erreichbar. |

Kein `throws`; keine expliziten `throw`-Statements im Konstruktor-Rumpf.

# Methoden

## `public String cm6Id()`

Keine Parameter. Rückgabewert: `String`, **nie `null`** (im schlimmsten Fall `""` für `PLAIN`) — das
Feld wird nur im Konstruktor mit einem nicht-null String-Literal gesetzt. Keine Exceptions.

## `public List<String> extensions()`

Keine Parameter. Rückgabewert: `List<String>`, **nie `null`** — mindestens eine leere Liste
(`PLAIN` hat keine Endungen, `extensions()` liefert dann `List.of()`, keine `null`-Rückgabe). Die
zurückgegebene Liste ist über `List.of(...)` erzeugt und damit strukturell unveränderlich
(`UnsupportedOperationException` bei Mutationsversuchen durch den Aufrufer, z. B. `add`). Keine
Exceptions im Methodenrumpf selbst.

# Cross-Referenzen

- [CodeViewer (Narrative)](/components/codeviewer.md)
- [CodeLanguageDetector](/api-reference/code-language-detector.md)
- [CodeViewer (API-Referenz)](/api-reference/code-viewer.md)

# Citations

[1] `web-common/src/main/java/de/makno/web/common/component/code/CodeLanguage.java`
