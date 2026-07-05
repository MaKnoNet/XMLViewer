---
type: API Reference
title: CodeLanguage.extensions(...)
description: Methode extensions von CodeLanguage - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/code/CodeLanguage.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `public List<String> extensions()`


Keine Parameter. Rückgabewert: `List<String>`, **nie `null`** — mindestens eine leere Liste
(`PLAIN` hat keine Endungen, `extensions()` liefert dann `List.of()`, keine `null`-Rückgabe). Die
zurückgegebene Liste ist über `List.of(...)` erzeugt und damit strukturell unveränderlich
(`UnsupportedOperationException` bei Mutationsversuchen durch den Aufrufer, z. B. `add`). Keine
Exceptions im Methodenrumpf selbst.

# Citations

[1] [CodeLanguage (Übersicht)](./code-language.md)
