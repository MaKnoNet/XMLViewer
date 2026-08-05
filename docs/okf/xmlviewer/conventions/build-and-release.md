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

# Toolchain

Zentral gepflegt: die Vaadin-Version steht **nur** in `gradle.properties` (`vaadinVersion`) und
speist Plugin wie BOM beider Module.

| Baustein | Version | Grund / Kopplung |
|---|---|---|
| Vaadin | 25.2.5 | Basis; das Repo baut und testet gegen diese Generation |
| Java-Toolchain | 21 | Vaadin 25 verlangt mindestens Java 21 |
| Spring Boot / Framework | 4.1.0 / 7.x | Vaadin 25 unterstützt Boot 3 nicht mehr; `vaadin-spring-boot-starter:25.2.5` zieht `spring-boot-starter-webmvc:4.1.0` |
| Gradle | 9.6.1 | Spring Boot 4 verlangt ≥ 8.14 |
| Spotless | 8.9.0 | erste Linie mit Gradle-9-Unterstützung (6.x bricht dort) |
| Servlet-API (nur Testscope) | 6.1.0 | Vaadin 25 = Servlet 6.1 / Jakarta EE 11 |
| Node | 24+ | lädt das Vaadin-Plugin selbst herunter |

**Die Bibliothek `web-common` ist davon bewusst entkoppelt** und läuft auch in
Vaadin-24-Anwendungen — siehe
[Vaadin-Versionsunabhängigkeit](/conventions/vaadin-versionsunabhaengigkeit.md). Diese Tabelle
beschreibt nur die Build-Umgebung dieses Repositories.

## Gradle-9-Besonderheiten

- `Project.exec()` existiert zur Ausführungszeit nicht mehr; `installGitHooks` nutzt den
  injizierten `ExecOperations`-Service (Halter-Interface in `build.gradle`).
- `:demo-app:processResources` liest `build/vaadin-generated` von `vaadinPrepareFrontend`. Das
  Vaadin-Plugin deklariert diese Abhängigkeit nicht selbst — Gradle 9 bricht dann mit
  „implicit dependency" ab, deshalb ein explizites `dependsOn` in `demo-app/build.gradle`.
- Hinter einem TLS-inspizierenden Proxy braucht schon der **Wrapper-Bootstrap** die Proxy-CA.
  `org.gradle.jvmargs` greift dort noch nicht (das gilt erst für den Daemon) — stattdessen
  maschinenlokal `GRADLE_OPTS=-Djavax.net.ssl.trustStoreType=Windows-ROOT` setzen.

## Vaadin-25-Besonderheiten

- Die **Dev-Tools sind nicht mehr im Starter enthalten**. Ohne
  `developmentOnly 'com.vaadin:vaadin-dev'` bricht `bootRun` im Dev-Mode beim Start ab
  (`'vaadin-dev-server' not found`).

## Offener Punkt

Vaadin 25 markiert `META-INF/resources/frontend/` für Add-on-Frontend-Quellen als **deprecated**
(Empfehlung: `META-INF/frontend/`). Ein Wechsel würde die Vaadin-24-Kompatibilität brechen, weil
24 nur den alten Pfad kennt — bleibt deshalb bewusst unverändert, bis Vaadin 24 nicht mehr
unterstützt werden muss. Der Build gibt bis dahin eine Deprecation-Warnung aus.

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
