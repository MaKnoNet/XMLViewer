---
type: Convention
title: Vaadin-API-Nutzung und Versionsuntergrenze
description: web-common zielt auf Vaadin 25+; keine Kopplung an eine JSON-Bibliothek in der Server-Browser-Uebertragung, und die Compile-Version bestimmt die Laufzeit-Untergrenze.
resource: web-common/src/main/java/de/makno/web/common/component/search/FrontendSearchHighlighter.java
tags: [convention, vaadin, compatibility, frontend, executejs]
timestamp: '2026-08-05T20:00:00+02:00'
---

# Untergrenze: Vaadin 25

`web-common` wird gegen **Vaadin 25** kompiliert und läuft ab dieser Generation. Vaadin 24 wird
**nicht** unterstützt.

Das ist keine freie Wahl, sondern eine Folge der Bytecode-Bindung: Vaadin 25 hat
`Element.executeJs(String, Object...)` eingeführt und die alte Überladung als
`executeJs(String, Serializable[])` **deprecated** hinterlassen. Vaadin 24 kennt ausschließlich
`executeJs(String, Serializable...)`. Ein gegen 25 kompilierter Aufruf referenziert im Bytecode
`(String, Object[])` — auf Vaadin 24 gäbe es dafür `NoSuchMethodError`.

**Merksatz:** Bei `executeJs`/`callJsFunction` bestimmt die **Compile-Version** die
Laufzeit-Untergrenze, nicht der Quelltext. Wer die Untergrenze senken will, muss die Bibliothek
gegen die niedrigste Zielversion kompilieren — Quelltextkosmetik reicht nicht.

# Regel: keine JSON-Bibliothek in der Übertragung

Unabhängig von der Untergrenze wird für strukturierte Nutzlasten **keine JSON-Bibliothek**
verwendet, sondern eine kompakte Zeichenkette, die das JS-Modul selbst zerlegt.

| Parametertyp | Status | in web-common |
|---|---|---|
| `String` | stabil über alle Generationen | **verwenden** |
| `int` / `double` / `boolean` | stabil | **verwenden** |
| `Object...`-Überladung von `executeJs` | Vaadin 25+ | zulässig (ab 25 die richtige) |
| `Serializable[]`-Überladung | in 25 deprecated | vermeiden — nie ein `Serializable[]` durchreichen |
| `elemental.json.JsonValue` | in 25 entfernt | verboten |
| Jackson `ArrayNode`/`ObjectNode` | Jackson-3-Kopplung | verboten |

## Warum kein Jackson

Vaadin 25 hat `elemental.json` entfernt und durch **Jackson 3** ersetzt — samt Paketwechsel
`com.fasterxml.jackson` → `tools.jackson`. Sich daran zu binden, verschiebt das Problem nur auf
die nächste Jackson-Generation. Die Zeichenkette ist über die Leitung sogar minimal kleiner als
ein JSON-Array (die Zahlen wurden ohnehin als Text serialisiert, nur die Klammern entfallen) und
ohne Browser unit-testbar.

# Gelebtes Beispiel

[FrontendSearchHighlighter](/api-reference/search/frontend-search-highlighter/frontend-search-highlighter.md)
überträgt die Suchtreffer als Zahlenfolge `"tokenIndex,start,end,…"`
([`toFlatCsv`](/api-reference/search/frontend-search-highlighter/to-flat-csv.md)); das Gegenstück
`search-highlighter.js` zerlegt sie in `parseFlat`. Das Trennzeichen liegt auf beiden Seiten als
benannte Konstante `SEPARATOR` vor (keine Magic-Strings, siehe
[Code-Stil](/conventions/code-style.md)).

`CodeViewer.callJs` nimmt bewusst `Object...` statt `Serializable...`: ein `Serializable[]` träfe
exakt die veraltete Überladung und erzeugte eine Deprecation-Warnung. Die Werte sind ohnehin nur
`String`/`boolean`/`int` und werden durchgereicht, nie in einem Feld gehalten.

# Prüfpunkte bei Änderungen

- Kein `import elemental.*`, kein `import tools.jackson.*` / `com.fasterxml.jackson.*` in
  `web-common/src/main/java`.
- Nie ein `Serializable[]` an `executeJs`/`callJsFunction` durchreichen (bindet an die veraltete
  Überladung) — lose Argumente oder `Object...` verwenden.
- `@ClientCallable`-Methoden nur mit primitiven Typen oder `String` deklarieren
  (z. B. `CodeViewer.onMatchChange(int, int)`).
- Neue Draht-Formate mit einem Unit-Test ohne Browser festhalten
  (`FrontendSearchHighlighterTest` als Vorlage).
- Deprecation-Details muss man nicht mehr selbst anfordern: `-Xlint:deprecation -Werror` ist im
  Root-`build.gradle` fest verdrahtet, jede veraltete API **bricht den Build** (Details in
  [Build, Test und Release](/conventions/build-and-release.md)).

# Citations

[1] web-common/src/main/java/de/makno/web/common/component/search/FrontendSearchHighlighter.java
[2] web-common/src/main/java/de/makno/web/common/component/code/CodeViewer.java
[3] https://vaadin.com/docs/latest/upgrading
[4] https://vaadin.com/blog/upgrading-your-add-on-to-vaadin-25-guide
