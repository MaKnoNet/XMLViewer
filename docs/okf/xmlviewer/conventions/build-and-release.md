---
type: Convention
title: Build, Test und Release
description: Die Gradle-Befehle des Projekts, Test-/Coverage-Anforderungen und der Release-Ablauf inkl. Knowledge-Base-Pflege vor dem Taggen.
resource: build.gradle
tags: [convention, gradle, build, test, release, jacoco]
timestamp: '2026-07-04T16:30:00+02:00'
---

# Befehle

| Zweck | Befehl |
|---|---|
| Build (beide Module + Tests + Format-Check) | `./gradlew build` |
| Demo starten | `./gradlew :demo-app:bootRun` → http://localhost:8080 |
| Nur Bibliotheks-Tests | `./gradlew :web-common:test` |
| Tests & Coverage | `./gradlew test jacocoTestReport` |
| Production-Build | `./gradlew clean build -Pvaadin.productionMode=true` |
| Ins lokale Maven-Repo | `./gradlew :web-common:publishToMavenLocal` |
| Team-Git-Hooks aktivieren | `./gradlew installGitHooks` (läuft beim ersten Build automatisch mit) |

# Regeln

- **Tests:** JUnit 5 (JUnit-Platform für alle Module erzwungen); Business-Logik mit
  JUnit 5 + Mockito, Vaadin-Views headless mit Karibu-Testing/TestBench; Coverage via jacoco.
- **Erster Build braucht Internet** (Node.js-Download + Vaadin-Frontend-Build); die
  CodeViewer-Demo zieht zusätzlich CodeMirror per npm — die Bibliothek selbst bleibt npm-frei.
- **Release:** vor dem Taggen die Knowledge Base auffrischen (OKF-Konzepte + `log.md`
  aktualisieren, `graphify update`), committen, dann taggen — so trägt jeder Release-Tag
  seinen passenden Wissensstand.

# Citations

[1] [README – Bauen & Starten](https://github.com/MaKnoNet/XMLViewer/blob/master/README.md)
