---
type: API Reference
title: CodeViewer.callJs(...)
description: Methode callJs von CodeViewer - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/code/CodeViewer.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `private void callJs(String function, Serializable... args)`


Zentrale Helfer-Methode für alle Client-Aufrufe.

| Parameter | Typ | null-erlaubt | Verifikation |
|---|---|---|---|
| `function` | `String` | **nein, ungeprüft** | Wird direkt in einen `StringBuilder` eingefügt (`.append(function)`); bei `null` würde `StringBuilder.append(String)` den Text `"null"` einfügen (JDK-Standardverhalten, keine Exception) — das erzeugte JavaScript wäre dann fehlerhaft (`window.MaknoCodeViewer.null(this)`), aber es gibt keine Exception im Java-Code selbst. Praktisch irrelevant, da `callJs` nur intern mit festen String-Literalen aufgerufen wird. |
| `args` | `Serializable...` | Varargs-Array selbst nie `null` bei normalem Aufruf (auch `callJs("foldAll")` erzeugt ein leeres `Serializable[0]`); einzelne `null`-Elemente sind erlaubt, da nur die Array-Länge (`args.length`) zum Bauen des Platzhalter-Strings verwendet wird, nicht die Werte selbst geprüft werden | Die eigentlichen Werte werden unverändert an `getElement().executeJs(js.toString(), args)` übergeben — Vaadins `executeJs` akzeptiert `null`-Argumente (werden im JS zu `null`/`undefined`). |

Ablauf: Bricht früh ab (`return`), wenn `getUI().isEmpty()` (keine gebundene UI, z. B. im Unit-Test
ohne Client) — dann passiert **nichts**, kein Fehler. Sonst wird ein JS-Aufrufstring
`"window.MaknoCodeViewer.<function>(this, $0, $1, ...)"` zusammengebaut und über
`getElement().executeJs(...)` ausgeführt. `void`, keine Exceptions im Java-Rumpf.

# Citations

[1] [CodeViewer (Übersicht)](./code-viewer.md)
