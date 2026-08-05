---
type: Architecture Concept
title: Frontend-Integration (CSS Custom Highlight API, CodeMirror 6, META-INF-Ressourcen)
description: Wie die Bibliothek Frontend-Arbeit in den Browser verlagert und ihre CSS/JS-Ressourcen npm-frei im Maven-Artefakt ausliefert.
resource: web-common/src/main/resources/META-INF/resources/frontend/web/common/component/search/search-highlighter.js
tags: [architecture, frontend, css, performance, codemirror]
timestamp: '2026-07-04T16:30:00+02:00'
---

# Überblick

Drei Frontend-Prinzipien prägen die Bibliothek:

1. **Suchtreffer-Highlighting im Browser:** Treffer werden nicht server-seitig in Spans
   zerlegt, sondern über die **CSS Custom Highlight API** als Text-Ranges gezeichnet
   (geteiltes Modul `search/search-highlighter.js`, genutzt von XmlViewer UND TextViewer).
   Das spart pro Treffer einen DOM-Knoten und Session-Heap und vermeidet DOM-Mutationen über
   die Leitung — relevant bei großen Bäumen und vielen gleichzeitigen Nutzern
   (Frontend-first-Prinzip). Benötigt aktuelle Chromium-/Firefox-/Safari-Versionen.
2. **Styling ausschließlich per CSS:** Java setzt nur Klassennamen (`CssClasses`); Farben,
   Marker-SVGs und Führungslinien kommen aus den mitgelieferten Stylesheets und sind über
   **CSS Custom Properties** (`--xmlviewer-*`, `--search-*`) override-bar, ohne Java zu ändern.
3. **Auslieferung im Artefakt:** CSS/JS liegen unter
   `META-INF/resources/frontend/web/common/component/...` und werden von Vaadin beim
   Konsumenten automatisch aufgelöst — kein zusätzliches Setup.

# Draht-Format Server → Browser

Die Treffer gehen als **flache Zahlenfolge** `"tokenIndex,start,end,tokenIndex,…"` über
`executeJs` an `window.SearchHighlighter.apply(...)`; leere Trefferliste = leerer String. Der
Browser zerlegt sie in `parseFlat` und findet den Treffer-Knoten positionsbasiert über die
Klasse `.search-token` (Dokumentreihenfolge == Token-Reihenfolge).

Bewusst eine Zeichenkette und **kein JSON-Typ**: Vaadin 25 hat `elemental.json` entfernt und durch
Jackson 3 ersetzt; sich an Jackson zu binden, verschöbe das Problem nur auf die nächste
Jackson-Generation. Begründung und Prüfpunkte in
[Vaadin-API-Nutzung](/conventions/vaadin-api-nutzung.md), Signatur in
[`toFlatCsv`](/api-reference/search/frontend-search-highlighter/to-flat-csv.md).

# Abgrenzung npm

Die Bibliothek `web-common` ist **npm-frei** (kompiliert/testet ohne Node). Nur die
[CodeViewer](/components/codeviewer.md)-Demo zieht CodeMirror 6 (`@codemirror/*`) per npm über
das Vaadin-Plugin der Demo-App.

# Citations

[1] [README – Styling anpassen, Suchtreffer-Highlighting](https://github.com/MaKnoNet/XMLViewer/blob/master/README.md)
[2] [MDN – CSS Custom Highlight API](https://developer.mozilla.org/docs/Web/API/CSS_Custom_Highlight_API)
