---
type: API Reference
title: CodeLanguageDetector.fromContent(...)
description: Methode fromContent von CodeLanguageDetector - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/code/CodeLanguageDetector.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

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

# Citations

[1] [CodeLanguageDetector (Übersicht)](./code-language-detector.md)
