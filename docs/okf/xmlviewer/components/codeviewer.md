---
type: Vaadin Component
title: CodeViewer
description: Read-only Quelltext-Ansicht für viele Sprachen (Java, C#, Python, JSON, YAML, HTML, CSS, JS, XML, SQL …) mit Syntax-Highlighting, sprachgenauem Falten und hell/dunkel-Theme; dünner Wrapper um CodeMirror 6.
resource: web-common/src/main/java/de/makno/web/common/component/code/CodeViewer.java
tags: [component, vaadin, code, codemirror, search]
timestamp: '2026-07-04T16:30:00+02:00'
---

# Überblick

`CodeViewer` zeigt Quelltext read-only mit Syntax-Highlighting, sprachgenauem Falten,
Zeilennummern, Umbruch und hell/dunkel-Theme. Er ist ein bewusst dünner Wrapper um
**CodeMirror 6** (Frontend-Glue in `code/code-viewer.js`) und über `MatchNavigable` mit dem
[SearchNavigator](/components/search-navigator.md) koppelbar — die Suche steuert hier
CodeMirror selbst, nicht die geteilte Server-Such-Engine.

Sprachunterstützung liegt in `CodeLanguage` (CM6-Id + Dateiendungen) und
`CodeLanguageDetector` (best-effort-Erkennung über Endung/Inhalt).

# Besonderheiten

- **npm-Abhängigkeit nur in der Demo:** Die CodeViewer-Demo (`/code`) zieht `@codemirror/*`
  per npm über das Vaadin-Plugin der Demo-App; die Bibliothek `web-common` selbst bleibt
  npm-frei und kompiliert/testet ohne Node — siehe
  [Frontend-Integration](/architecture/frontend-integration.md).
- Demo unter `/code` (`app.CodeDemoView` mit `app.SampleCodeFactory`).

# Citations

[1] [README – Schwester-Komponente CodeViewer](https://github.com/MaKnoNet/XMLViewer/blob/master/README.md)
