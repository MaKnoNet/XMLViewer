---
type: Convention
title: Vaadin-Versionsunabhängigkeit der Bibliothek
description: web-common nutzt keine an eine Vaadin-Generation gebundenen APIs - insbesondere keine JSON-Typen als executeJs-Parameter, damit ein Artefakt auf Vaadin 24 und 25 läuft.
resource: web-common/src/main/java/de/makno/web/common/component/search/FrontendSearchHighlighter.java
tags: [convention, vaadin, compatibility, frontend, executejs]
timestamp: '2026-08-05T12:00:00+02:00'
---

# Regel

`web-common` ist ein **publiziertes Add-on**, das in fremde Vaadin-Anwendungen eingebunden
wird. Die konsumierende Anwendung bestimmt die Vaadin-Version zur Laufzeit, nicht dieses
Repository. Deshalb gilt: **In der Bibliothek werden keine APIs verwendet, die an eine
bestimmte Vaadin-Generation gebunden sind.**

Konkret für die Server→Browser-Übertragung (`Element.executeJs`, `callJsFunction`):

| Parametertyp | Vaadin 24 | Vaadin 25 | in web-common |
|---|---|---|---|
| `String` | ja | ja | **verwenden** |
| `int` / `double` / `boolean` | ja | ja | **verwenden** |
| `elemental.json.JsonValue` (`JsonArray`, `JsonObject`) | ja | **entfernt** | verboten |
| Jackson `ArrayNode`/`ObjectNode` | nein | ja | verboten |
| `List`/`Map`/Beans | nein | ja | verboten |

Für strukturierte Nutzlasten wird deshalb eine **kompakte Zeichenkette** übertragen, die das
zugehörige JS-Modul selbst zerlegt.

# Warum

Vaadin 25 hat die JSON-Bibliothek `elemental.json` **entfernt** und durch Jackson 3 ersetzt
(gleichzeitig Paketwechsel `com.fasterxml.jackson` → `tools.jackson`). Ein gegen Vaadin 24
kompiliertes Add-on, das ein `JsonArray` an `executeJs` übergibt, kompiliert zwar, scheitert
in einer Vaadin-25-Anwendung aber zur **Laufzeit** mit `NoClassDefFoundError` — und würde
selbst bei wieder eingehängtem `gwt-elemental` als Parametertyp abgelehnt.

Der Fehler tritt erst beim Auslösen der betroffenen Funktion auf (hier: beim Suchen), nicht
beim Start — er ist also durch einen erfolgreichen Build der Host-Anwendung **nicht**
ausgeschlossen.

Vaadins eigene Add-on-Empfehlung für diesen Fall ist ein eigener Branch bzw. Major je
Vaadin-Generation. Das ist hier bewusst **nicht** gewählt: Der Verzicht auf JSON-Typen kostet
sechs Zeilen Code und hält ein einziges Artefakt auf beiden Generationen lauffähig — ohne
Fremd-Dependency (die Community-Alternative `JsonMigrationHelper` bräuchte zusätzlich
Lombok).

# Gelebtes Beispiel

[FrontendSearchHighlighter](/api-reference/search/frontend-search-highlighter/frontend-search-highlighter.md)
überträgt die Suchtreffer als Zahlenfolge `"tokenIndex,start,end,…"`
([`toFlatCsv`](/api-reference/search/frontend-search-highlighter/to-flat-csv.md)); das
Gegenstück `search-highlighter.js` zerlegt sie in `parseFlat` wieder in Zahlen. Das
Trennzeichen liegt auf beiden Seiten als benannte Konstante `SEPARATOR` vor (keine
Magic-Strings, siehe [Code-Stil](/conventions/code-style.md)).

Über die Leitung ist die Zeichenkette sogar minimal kleiner als das frühere JSON-Array — die
Zahlen wurden ohnehin als Text serialisiert, nur die eckigen Klammern entfallen.

# Prüfpunkte bei Änderungen

- Kein `import elemental.*` und kein `import tools.jackson.*` / `com.fasterxml.jackson.*` in
  `web-common/src/main/java`.
- Neue `executeJs`/`callJsFunction`-Parameter nur aus der oberen Tabellenhälfte.
- `@ClientCallable`-Methoden nur mit primitiven Typen oder `String` deklarieren
  (z. B. `CodeViewer.onMatchChange(int, int)`).
- Neue Draht-Formate mit einem Unit-Test festhalten, der ohne Browser läuft
  (`FrontendSearchHighlighterTest` als Vorlage).

# Citations

[1] web-common/src/main/java/de/makno/web/common/component/search/FrontendSearchHighlighter.java
[2] web-common/src/main/resources/META-INF/resources/frontend/web/common/component/search/search-highlighter.js
[3] https://vaadin.com/docs/latest/upgrading
[4] https://vaadin.com/blog/upgrading-your-add-on-to-vaadin-25-guide
