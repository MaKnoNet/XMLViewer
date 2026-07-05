---
type: API Reference
title: CodeLanguage – Konstruktoren
description: Alle Konstruktoren von CodeLanguage.
resource: web-common/src/main/java/de/makno/web/common/component/code/CodeLanguage.java
tags: [api-reference, constructor]
timestamp: '2026-07-08T09:00:00+02:00'
---


## `CodeLanguage(String cm6Id, String... extensions)`

Package-privater (kein Modifier — für Enum-Konstruktoren ohnehin implizit `private`) Konstruktor,
nur von den Enum-Konstanten selbst aufgerufen.

| Parameter | Typ | null-erlaubt | Verifikation |
|---|---|---|---|
| `cm6Id` | `String` | in der Praxis nein, aber **kein Code-Check** | Wird ungeprüft dem Feld `cm6Id` zugewiesen (Zeile 32). Kein `Objects.requireNonNull`. Alle Aufrufstellen (die Enum-Konstanten selbst) übergeben stets ein String-Literal, nie `null` — die Absicherung existiert nur "durch Disziplin", nicht durch Code. |
| `extensions` | `String...` (Varargs) | Varargs-Array selbst kann technisch nicht `null` sein, wenn der Aufrufer keine Endungen übergibt (`PLAIN(""))` erzeugt ein leeres Array `String[0]`, nicht `null`) | `List.of(extensions)` (Zeile 33) wirft laut JDK-Doku eine `NullPointerException`, falls das Array selbst `null` wäre oder ein Element `null` enthielte — bei Aufruf mit einem expliziten `null`-Array (`(String[]) null`) via Reflection wäre das theoretisch möglich, aber über den normalen Varargs-Aufrufpfad (wie in allen 11 Konstanten) nicht erreichbar. |

Kein `throws`; keine expliziten `throw`-Statements im Konstruktor-Rumpf.

# Citations

[1] [CodeLanguage (Übersicht)](./code-language.md)
