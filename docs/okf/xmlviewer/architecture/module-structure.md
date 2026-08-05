---
type: Architecture Concept
title: Modulstruktur (web-common / demo-app)
description: Multi-Modul-Gradle-Build, der die wiederverwendbare, Spring-freie Bibliothek strikt von der Spring-Boot-Demo trennt; publiziert wird nur de.makno:web-common.
resource: settings.gradle
tags: [architecture, gradle, module, maven, publishing]
timestamp: '2026-07-04T16:30:00+02:00'
---

# Überblick

Das Repository ist ein Multi-Modul-Gradle-Build (`web-common-build`) mit klarer Trennlinie:

| Modul | Inhalt | Veröffentlichung |
|---|---|---|
| `web-common` | Bibliothek: Viewer-Komponenten + Such-Navigation inkl. Frontend-Ressourcen | Maven-Artefakt **`de.makno:web-common:1.0.0-SNAPSHOT`** (mit `-sources`/`-javadoc`) |
| `demo-app` | Eigenständige Spring-Boot-Demo (`de.makno.xmlviewer.app`) | **nicht** publiziert |

Package-Layout der Bibliothek (`de.makno.web.common.component.*`): `xmlviewer`
([XmlViewer](/components/xmlviewer.md)), `text` ([TextViewer](/components/textviewer.md)),
`code` ([CodeViewer](/components/codeviewer.md)), `search`
([Such-Engine](/architecture/search-engine.md)), `navigation`
([SearchNavigator](/components/search-navigator.md)).

# Leitplanken

- **Keine Spring-Abhängigkeit in der Bibliothek** — nur die Demo nutzt Spring Boot; das
  Artefakt liefert den `vaadin-spring-boot-starter` NICHT transitiv mit, Konsumenten
  deklarieren ihn selbst.
- **Frontend-Ressourcen reisen im Artefakt** unter `META-INF/frontend/...` und
  werden von Vaadin beim Konsumenten automatisch aufgelöst — siehe
  [Frontend-Integration](/architecture/frontend-integration.md).
- **Migration von `de.makno.xmlviewer:xmlviewer`:** neue Koordinaten `de.makno:web-common`,
  neue Packages `de.makno.web.common.component.*`, Spring-Starter selbst deklarieren.

# Citations

[1] [README – Module & Artefakt, Migration](https://github.com/MaKnoNet/XMLViewer/blob/master/README.md)
