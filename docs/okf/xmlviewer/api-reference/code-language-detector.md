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
[CodeViewer](/api-reference/code-viewer.md) als reine Utility-Klasse aufgerufen (Verwendung,
keine Vererbung) und liefert Werte vom Typ [CodeLanguage](/api-reference/code-language.md).

# Konstruktoren

## `private CodeLanguageDetector()`

Leerer, privater Konstruktor ohne Parameter — Utility-Klassen-Muster, verhindert Instanziierung. Wird
nie aufgerufen.

# Methoden

## `public static CodeLanguage fromFileName(String fileName)`

| Parameter | Typ | null-erlaubt | Verifikation |
|---|---|---|---|
| `fileName` | `String` | **ja** | Explizite Prüfung Zeile 25–27: `if (fileName == null) { return CodeLanguage.PLAIN; }`. |

Ablauf: Findet den letzten `.` (`lastIndexOf('.')`). Ist kein Punkt vorhanden oder steht der Punkt am
Stringende (`dot < 0 || dot == fileName.length() - 1`), wird `CodeLanguage.PLAIN` zurückgegeben.
Sonst wird die Endung ab dem Zeichen nach dem Punkt extrahiert, mit `toLowerCase(Locale.ROOT)`
normalisiert (bewusst locale-unabhängig, kein "Turkish-I-Problem") und gegen `extensions()` jeder
`CodeLanguage`-Konstante geprüft (lineare Suche über `CodeLanguage.values()`); die erste passende
Konstante wird zurückgegeben.

**Rückgabewert:** `CodeLanguage`, **nie `null`** — jeder Pfad endet entweder mit einer konkreten
Konstante oder mit `CodeLanguage.PLAIN` als Fallback. Keine deklarierten oder geworfenen Exceptions.

## `public static CodeLanguage fromContent(String text)`

| Parameter | Typ | null-erlaubt | Verifikation |
|---|---|---|---|
| `text` | `String` | **ja** | Explizite Prüfung Zeile 46: `if (text == null \|\| text.isBlank()) { return CodeLanguage.PLAIN; }` — `null` UND rein aus Whitespace bestehender Text werden gleich behandelt. |

Ablauf (geordnete Regeln, erste Übereinstimmung gewinnt — wie im Klassen-Javadoc beschrieben und im
Code exakt nachvollziehbar):

1. `head = text.stripLeading()`, `lower = head.toLowerCase(Locale.ROOT)`.
2. `lower.startsWith("<?xml")` → `XML`.
3. `lower` enthält eines von `"<!doctype html"`, `"<html"`, `"<head"`, `"<body"`, `"<div"` → `HTML`.
4. `head.startsWith("<")` (und obige HTML-Muster griffen nicht) → `XML`.
5. `head.startsWith("{")` oder `head.startsWith("[")` → `JSON`.
6. `SQL_START`-Pattern (`^\s*(SELECT|INSERT|UPDATE|DELETE|CREATE|ALTER|DROP|WITH)\b`, case-insensitiv)
   matcht `head` → `SQL`.
7. `PYTHON`-Pattern (mehrzeilig: `def `, `elif `, `class ...:` am Zeilenende, `from ... import`)
   matcht den **vollständigen** `text` (nicht nur `head`) → `PYTHON`.
8. `text` enthält eines von `"using System"`, `"namespace "`, `"Console.WriteLine"` → `CSHARP`.
9. `text` enthält eines von `"public class"`, `"package "`, `"System.out."`, `"void main"` → `JAVA`.
10. `text` enthält eines von `"function "`, `"=>"`, `"const "`, `"console.log"`, `"document."` →
    `JAVASCRIPT`.
11. `CSS_RULE`-Pattern matcht `text` → `CSS`.
12. `YAML_KEY`-Pattern matcht `text` → `YAML`.
13. Kein Treffer → `CodeLanguage.PLAIN`.

Alle String-Vergleiche in Schritten 8–10 sind **case-sensitiv** (`String.contains`, kein
`toLowerCase` auf `text` selbst dort) — nur die HTML-Prüfung (Schritt 3) läuft auf dem bereits
kleingeschriebenen `lower`.

**Rückgabewert:** `CodeLanguage`, **nie `null`**. Keine deklarierten oder geworfenen Exceptions.

## `private static boolean containsAny(String text, String... needles)`

Package-interne Helfer-Methode: Iteriert über `needles` und gibt `true` zurück, sobald
`text.contains(needle)` zutrifft, sonst `false` nach vollständiger Iteration.

| Parameter | Typ | null-erlaubt | Verifikation |
|---|---|---|---|
| `text` | `String` | **nein, ungeprüft** | Kein Null-Check; ruft direkt `text.contains(needle)` auf. Bei `text == null` würde eine `NullPointerException` geworfen. In der Praxis unkritisch, da `fromContent` diese Methode nur mit dem bereits auf `null`/blank geprüften `text`-Parameter aufruft. |
| `needles` | `String...` | wird über `for (String needle : needles)` iteriert; einzelne `null`-Elemente würden bei `text.contains(null)` eine `NullPointerException` auslösen (Vertrag von `String.contains`), das leere Array selbst (kein Varargs übergeben) ist unproblematisch | Alle Aufrufstellen übergeben feste String-Literale, nie `null`-Elemente. |

**Rückgabewert:** `boolean`, kein `null` möglich (primitiver Typ). Keine deklarierten Exceptions;
potenzielle `NullPointerException` bei `text == null`, praktisch nicht erreichbar über den
öffentlichen API-Pfad (`fromContent`).

# Cross-Referenzen

- [CodeLanguage](/api-reference/code-language.md)
- [CodeViewer (Narrative)](/components/codeviewer.md)
- [CodeViewer (API-Referenz)](/api-reference/code-viewer.md)

# Citations

[1] `web-common/src/main/java/de/makno/web/common/component/code/CodeLanguageDetector.java`
