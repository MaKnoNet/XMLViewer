---
type: Architecture Concept
title: Verbindliche Design-Regeln (Vaadin/Java, Multiuser)
description: Die vier Pflicht-Prüfachsen vor jeder Code-Änderung sowie die Entwurfsmuster- und Thread-Safety-Regeln des Projekts.
resource: CLAUDE.md
tags: [architecture, conventions, concurrency, patterns, vaadin]
timestamp: '2026-07-04T16:30:00+02:00'
---

# Think Before You Code (Pflicht vor jeder Änderung)

1. **Concurrency-Check:** sicher für multithreaded, multiuser Server-Umgebung? (kein geteilter
   Zustand; `ui.access(...)` bei UI-Updates aus Hintergrund-Threads)
2. **Performance-Check:** Memory-Leaks/Serverlast? (lazy Komponenten, flacher Layout-Baum,
   lazy DataProvider)
3. **Design-Pattern-Anwendung:** welches Muster löst es am saubersten?
4. **Testability-Check:** unit-/headless-testbar ohne vollen Server?

# Verbindliche Muster

- **Composite:** Views aus kleinen, wiederverwendbaren Bausteinen (`Composite<T>`).
- **Observer/Event-Listener:** entkoppeln; `Registration` im `onDetach` lösen (Memory-Leaks).
- **Presenter/Mediator:** View emittiert nur Events, ein Presenter steuert den Workflow.
- **Strategy/Factory/Builder:** austauschbare Algorithmen, rollen-/konfigurationsabhängige
  Erzeugung, lesbare komplexe Objekte.
- **Dependency Inversion als Standardhaltung:** Konsumenten hängen an schlanken Abstraktionen
  (`MatchNavigable`, `SearchTermSplitter`, `MatchLabelFormatter`), nie an konkreten Klassen —
  gelebt im [SearchNavigator](/components/search-navigator.md) und der
  [Such-Engine](/architecture/search-engine.md).

# Multiuser-Regeln (kritisch)

- Keine stateful Singletons; kein benutzer-/UI-Zustand in `@Service`-Feldern.
- Alle in Vaadin-Komponenten gehaltenen Objekte `Serializable` (mit `serialVersionUID`).
- Business-Fehler als `Notification`, nie rohe Stacktraces in die UI; globaler `ErrorHandler`
  an der `VaadinSession`.

# Citations

[1] [CLAUDE.md – Architektur- & Entwicklungsregeln](https://github.com/MaKnoNet/XMLViewer/blob/master/CLAUDE.md)
