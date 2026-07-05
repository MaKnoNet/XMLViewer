---
type: API Reference
title: CodeLanguageDetector.fromFileName(...)
description: Methode fromFileName von CodeLanguageDetector - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/code/CodeLanguageDetector.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

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

# Citations

[1] [CodeLanguageDetector (Übersicht)](./code-language-detector.md)
