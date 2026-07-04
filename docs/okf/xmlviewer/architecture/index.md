# Konzepte

* [Verbindliche Design-Regeln (Vaadin/Java, Multiuser)](/architecture/design-rules.md) - Die vier Pflicht-Prüfachsen vor jeder Code-Änderung sowie die Entwurfsmuster- und Thread-Safety-Regeln des Projekts.
* [Frontend-Integration (CSS Custom Highlight API, CodeMirror 6, META-INF-Ressourcen)](/architecture/frontend-integration.md) - Wie die Bibliothek Frontend-Arbeit in den Browser verlagert und ihre CSS/JS-Ressourcen npm-frei im Maven-Artefakt ausliefert.
* [Modulstruktur (web-common / demo-app)](/architecture/module-structure.md) - Multi-Modul-Gradle-Build, der die wiederverwendbare, Spring-freie Bibliothek strikt von der Spring-Boot-Demo trennt; publiziert wird nur de.makno:web-common.
* [Geteilte Such-Engine (component.search)](/architecture/search-engine.md) - Eine Textsuche für alle Viewer — SearchController & Co. im Package de.makno.web.common.component.search, konsumiert über schlanke Abstraktionen statt konkreter Typen.
