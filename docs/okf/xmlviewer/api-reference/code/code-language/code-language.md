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
eine Liste typischer Datei-Endungen, die [CodeLanguageDetector](/api-reference/code/code-language-detector/code-language-detector.md)
zur Erkennung verwendet.

**Zustand/Thread-Safety:** Enum-Konstanten sind in Java grundsätzlich unveränderliche Singletons;
jede Konstante hat zwei `final`-Felder (`cm6Id`, `extensions`), die im Konstruktor einmalig gesetzt
werden und danach nie mehr verändert werden. `extensions` ist eine über `List.of(...)` erzeugte,
strukturell unveränderliche Liste. Die Klasse ist damit **vollständig thread-safe** — keine
Synchronisation nötig. Enums sind laut Klassen-Javadoc implizit `Serializable` (Serialisierung über
den Konstantennamen), daher bewusst ohne eigene `serialVersionUID`.

# Felder

| Feld | Typ | Bedeutung | null-erlaubt |
|---|---|---|---|
| `cm6Id` | `private final String` | CodeMirror-6-Language-Id der Konstante (leer `""` für `PLAIN`). | nein — im Enum-Konstruktor gesetzt, nie `null` |
| `extensions` | `private final List<String>` | Typische Datei-Endungen (klein, ohne Punkt) via `List.of(...)`. | Feld nie `null`; für `PLAIN` leere Liste (keine Endungen übergeben) |

# Thread-Safety

**Vollständig thread-safe** (verifiziert): Enum-Konstanten sind unveränderliche Singletons;
`cm6Id` und `extensions` sind `final` und werden ausschließlich im Konstruktor gesetzt.
`extensions` ist über `List.of(...)` strukturell unveränderlich. Kein Synchronisationsbedarf.

# Serialisierung

**Implizit `Serializable`** — jedes Java-Enum erweitert `java.lang.Enum<E>`, welches
`Serializable` implementiert; die Serialisierung erfolgt über den Konstantennamen
(`name()`), nicht über die Felder. Bewusst **keine eigene `serialVersionUID`** (im Quellcode-
Javadoc explizit begründet: Enum-Serialisierung ist name-basiert, eine `serialVersionUID`
wäre hier ohne Wirkung/nicht üblich).

# equals/hashCode/toString

Keine dieser Methoden ist überschrieben (verifiziert: keine `equals`/`hashCode`/`toString`-
Deklaration im Quellcode) — es gelten die **von `java.lang.Enum` geerbten Implementierungen**:
`equals`/`hashCode` sind identitätsbasiert (Enum-Konstanten sind Singletons, daher pro JVM
eindeutig), `toString()` liefert den Konstantennamen (z. B. `"JAVA"`).

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
[CodeViewer](/api-reference/code/code-viewer/code-viewer.md) gehalten und von
[CodeLanguageDetector](/api-reference/code/code-language-detector/code-language-detector.md) zurückgegeben (Verwendung
als Wert, keine Vererbung).

# Konstruktoren

- [siehe constructor.md](./constructor.md)

# Methoden

- [``public String cm6Id()``](./cm6-id.md)
- [``public List<String> extensions()``](./extensions.md)

# Citations


[1] `web-common/src/main/java/de/makno/web/common/component/code/CodeLanguage.java`
