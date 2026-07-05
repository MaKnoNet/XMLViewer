---
type: API Reference
title: SearchTermSplitter
description: Funktionales Interface, das den Sucheingabe-Text in einzelne hervorzuhebende Begriffe zerlegt; das Trennverhalten ist frei austauschbar.
resource: web-common/src/main/java/de/makno/web/common/component/search/SearchTermSplitter.java
tags: [api-reference, search, functional-interface]
timestamp: '2026-07-07T10:00:00+02:00'
---

# Überblick


`SearchTermSplitter` ist ein `@FunctionalInterface`, das `Serializable` erweitert (damit
Lambda-Implementierungen als Vaadin-Session-Zustand serialisierbar bleiben). Es entkoppelt
die Trennlogik für Suchbegriffe vom [SearchController](/api-reference/search/search-controller/search-controller.md):
die einbettende Anwendung kann z.&nbsp;B. an Leerzeichen (Standard), an Komma oder gar
nicht trennen (gesamter Text als ein Begriff). Details zur Architektur in
[Geteilte Such-Engine](/architecture/search-engine.md).

**Thread-Safety:** Das Interface selbst ist zustandslos. Ob eine konkrete Implementierung
thread-sicher ist, hängt von ihr ab — die mitgelieferte Standardimplementierung
`SearchController.DEFAULT_TERM_SPLITTER` ist eine reine, zustandslose Lambda und damit
gefahrlos teilbar.

# Felder

Keine Felder (Interface). `SearchTermSplitter` deklariert ausschließlich eine
abstrakte Instanzmethode (`split`) und keine Konstanten (verifiziert: keine
Feld-Deklaration im Quellcode).

# Thread-Safety

Kein besonderer Vertrag auf Interface-Ebene: `SearchTermSplitter` selbst ist
zustandslos und deklariert keine Felder. Ob eine konkrete Implementierung
thread-sicher ist, hängt von ihr ab. Die mitgelieferte Standardimplementierung
`SearchController.DEFAULT_TERM_SPLITTER` ist eine reine, zustandslose Lambda ohne
Seiteneffekte (`query.trim().split(...)` gefolgt von einem Stream-Filter, kein
Zugriff auf veränderlichen Zustand) und damit gefahrlos zwischen Threads teilbar.

# Serialisierung

`extends Serializable` — die Schnittstelle selbst erweitert `java.io.Serializable`
als Marker (verifiziert: `@FunctionalInterface public interface SearchTermSplitter
extends Serializable`), damit Lambda-Implementierungen als Vaadin-Session-Zustand
serialisierbar bleiben. Ein Interface kann selbst keine `serialVersionUID` sinnvoll
deklarieren (keine Instanzfelder); dieser Wert ist Sache jeder konkreten
Implementierung bzw. — bei Lambdas — der vom JDK generierten synthetischen Klasse.

# equals/hashCode/toString

Ein Interface deklariert keine eigenen `equals`-/`hashCode`-/`toString`-Implementierungen
und kann dies auch nicht (verifiziert: keine entsprechenden Default- oder
statischen Methoden im Quellcode). Für die mitgelieferte Lambda-Implementierung
`SearchController.DEFAULT_TERM_SPLITTER` gilt die vom JDK für Lambdas generierte
Identitätssemantik (keine strukturelle Gleichheit zwischen separat erzeugten
Lambda-Instanzen desselben Funktionskörpers).

# Vererbungshierarchie


**Vorwärts (eigene Deklaration):** `public interface SearchTermSplitter extends
Serializable`.

- **Erweitertes Interface:** `java.io.Serializable` — JDK-Standard-Interface (Marker-Interface,
  keine Methoden), kein Projekt-Typ, daher kein Cross-Link.

**Rückwärts (Abhängige):** Verifiziert per Grep auf `implements ... SearchTermSplitter` bzw.
`extends SearchTermSplitter` über den gesamten
`web-common/src/main/java/de/makno/web/common/component/`-Baum — **kein Treffer** durch eine
benannte Klasse. Die einzige mitgelieferte Implementierung,
`SearchController.DEFAULT_TERM_SPLITTER`, ist eine anonyme Lambda-Konstante innerhalb von
[SearchController](/api-reference/search/search-controller/search-controller.md) (Verwendung als Wert, keine
Vererbungsbeziehung).

# Konstruktoren


# Methoden

- [``split(String query)``](./split.md)

# Citations


[1] web-common/src/main/java/de/makno/web/common/component/search/SearchTermSplitter.java
