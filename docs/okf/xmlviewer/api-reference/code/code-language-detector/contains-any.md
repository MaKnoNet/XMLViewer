---
type: API Reference
title: CodeLanguageDetector.containsAny(...)
description: Methode containsAny von CodeLanguageDetector - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/code/CodeLanguageDetector.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

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

# Citations

[1] [CodeLanguageDetector (Übersicht)](./code-language-detector.md)
