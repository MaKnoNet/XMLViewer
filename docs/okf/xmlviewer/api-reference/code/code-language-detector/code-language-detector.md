---
type: API Reference
title: CodeLanguageDetector
description: Zustandslose Utility-Klasse mit Best-effort-Heuristiken zur Spracherkennung über Datei-Endung oder Inhaltsmuster.
resource: web-common/src/main/java/de/makno/web/common/component/code/CodeLanguageDetector.java
tags: [api-reference, code, detector, heuristics]
timestamp: '2026-07-07T10:00:00+02:00'
---

# Überblick


`CodeLanguageDetector` (`public final class CodeLanguageDetector`) liefert eine Best-effort-Erkennung
der Sprache eines Quelltexts, entweder über Datei-Endung (`fromFileName`) oder über eine
Inhaltsheuristik (`fromContent`), verwendet von [CodeViewer](/components/codeviewer.md) als
Vorauswahl, wenn keine Sprache explizit gesetzt ist. Laut Klassen-Javadoc bewusst einfach gehalten
(kein ML, keine vollständige Lexer-Analyse); im Zweifel liefert sie `CodeLanguage.PLAIN`.

**Zustand/Thread-Safety:** Die Klasse ist eine reine Utility-Klasse mit ausschließlich `static`-
Methoden und `private static final Pattern`-Feldern (`SQL_START`, `PYTHON`, `CSS_RULE`, `YAML_KEY`).
`java.util.regex.Pattern`-Instanzen sind laut JDK-Dokumentation selbst **immutable und thread-safe**
(nur die zugehörigen `Matcher`-Instanzen sind zustandsbehaftet und nicht thread-safe — hier werden
`Matcher` aber stets lokal in der Methode erzeugt, nie geteilt). Damit ist `CodeLanguageDetector`
**vollständig thread-safe**, es gibt keinen mutablen geteilten Zustand.

# Felder

| Feld | Typ | Bedeutung | null-erlaubt |
|---|---|---|---|
| `SQL_START` | `private static final Pattern` | Regex `^\s*(SELECT\|INSERT\|UPDATE\|DELETE\|CREATE\|ALTER\|DROP\|WITH)\b` (case-insensitive) — erkennt SQL am Textanfang. | nein — im statischen Initialisierer gesetzt, nie `null` |
| `PYTHON` | `private static final Pattern` | Regex für typische Python-Konstrukte (`def`, `elif`, `class ... :`, `from ... import`). | nein |
| `CSS_RULE` | `private static final Pattern` | Regex für eine CSS-Regel (`selector { prop: value; }`). | nein |
| `YAML_KEY` | `private static final Pattern` | Regex für eine YAML-Key-Zeile (`key: value`). | nein |

# Thread-Safety

**Vollständig thread-safe** (verifiziert): reine Utility-Klasse mit ausschließlich `static`-
Methoden und `private static final Pattern`-Feldern. `java.util.regex.Pattern`-Instanzen sind
laut JDK-Dokumentation immutable und thread-safe; die zugehörigen `Matcher`-Instanzen werden in
`fromContent(String)` stets lokal erzeugt (`SQL_START.matcher(head)` usw.) und nie zwischen
Aufrufen oder Threads geteilt. Kein mutabler geteilter Zustand.

# Serialisierung

Nicht `Serializable` — `CodeLanguageDetector` implementiert kein Serialisierungs-Interface
(verifiziert gegen die Klassendeklaration `public final class CodeLanguageDetector`). Kein
Serialisierungs-Vertrag.

# equals/hashCode/toString

Keine dieser Methoden ist überschrieben (verifiziert: keine `equals`/`hashCode`/`toString`-
Deklaration im Quellcode) — es gilt die **Identitätssemantik von `java.lang.Object`**. Die
Klasse wird nie instanziiert (privater Konstruktor, nur `static`-Zugriff), daher ohne
praktische Relevanz.

# Vererbungshierarchie


**Vorwärts (eigene Deklaration):** `public final class CodeLanguageDetector` (kein
`extends`/`implements`).

- **Superklasse:** keine explizite (impliziter `Object`).
- **Interfaces:** keine.
- Die Klasse ist `final` und hat einen `private`-Konstruktor (siehe unten) — keine
  Subklasse möglich, auch nicht innerhalb des Pakets.

**Rückwärts (Abhängige):** Verifiziert per Grep auf `extends CodeLanguageDetector` über den
gesamten `web-common/src/main/java/de/makno/web/common/component/`-Baum — **kein Treffer**.
`CodeLanguageDetector` hat keine projektinterne Vererbungsbeziehung; sie wird von
[CodeViewer](/api-reference/code/code-viewer/code-viewer.md) als reine Utility-Klasse aufgerufen (Verwendung,
keine Vererbung) und liefert Werte vom Typ [CodeLanguage](/api-reference/code/code-language/code-language.md).

# Konstruktoren

- [siehe constructor.md](./constructor.md)

# Methoden

- [``public static CodeLanguage fromFileName(String fileName)``](./from-file-name.md)
- [``public static CodeLanguage fromContent(String text)``](./from-content.md)
- [``private static boolean containsAny(String text, String... needles)``](./contains-any.md)

# Citations


[1] `web-common/src/main/java/de/makno/web/common/component/code/CodeLanguageDetector.java`
