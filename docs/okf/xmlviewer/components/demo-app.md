---
type: Demo Application
title: Demo-App (Application, MainView, SampleXmlFactory)
description: Spring-Boot-Einstiegspunkt und XmlViewer-Startseite der Demo; erzeugt einen großen Beispiel-XML-Baum inkl. Namespace, Kommentar, CDATA und XML-Sonderzeichen zum Testen von Scrollen, Highlight und Suche.
resource: demo-app/src/main/java/de/makno/xmlviewer/app/MainView.java
tags: [demo, vaadin, spring-boot]
timestamp: '2026-07-04T18:30:00+02:00'
---

# Überblick

`Application` ist der `@SpringBootApplication`-Einstiegspunkt (`./gradlew bootRun` →
http://localhost:8080); die [XmlViewer](/components/xmlviewer.md)-Komponente selbst hat
keine Spring-Abhängigkeit — nur die Demo nutzt Spring Boot.

`MainView` (Route `""`) ist die Startseite: baut einen `XmlViewer` mit dem Beispielbaum aus
`SampleXmlFactory`, bindet die komplette Such-UI über die eigenständige
[SearchNavigator](/components/search-navigator.md)-Komponente an und demonstriert
Hervorheben (`highlight`/`clearHighlight`), Auf-/Zuklappen sowie Wurzelwechsel
(`setRoot`). `RouterLink`s führen zu den Schwester-Demos `TextDemoView` (`/text`) und
`CodeDemoView` (`/code`) — siehe [TextViewer](/components/textviewer.md) und
[CodeViewer](/components/codeviewer.md).

`SampleXmlFactory` erzeugt absichtlich einen **großen** Beispielbaum (40 Bücher mit
Kapiteln) für vertikales Scrollen und teils sehr lange Attributwerte/Texte für
horizontales Scrollen; deckt zugleich Namespace, Kommentar, CDATA und
XML-Sonderzeichen (`< & " '`) zum Prüfen des Escapings ab.

# Schema

| Klasse | Aufgabe |
|---|---|
| `Application` | Spring-Boot-Start (`main`, `AppShellConfigurator`) |
| `MainView` | Route `""`; XmlViewer-Demo mit Toolbar (Highlight/Klappen/Wurzelwechsel) |
| `SampleXmlFactory` | Beispielbaum-Erzeugung (`createLibrary`, `findHighlightTarget`, `findBookById`) |

# Citations

[1] [README – Demo, Architektur-Tabelle](https://github.com/MaKnoNet/XMLViewer/blob/master/README.md)
