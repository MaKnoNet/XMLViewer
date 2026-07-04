---
type: Convention
title: Code-Stil und Clean Code
description: palantir-java-format als Pflichtformat (Spotless), Clean-Code-Regeln und Fehlerbehandlungs-Konventionen des Projekts.
resource: CLAUDE.md
tags: [convention, clean-code, formatting, spotless]
timestamp: '2026-07-04T16:30:00+02:00'
---

# Formatierung

Java-Code wird verbindlich mit **palantir-java-format** formatiert, eingebunden über Spotless:
`./gradlew spotlessApply` (formatieren) / `spotlessCheck` (Teil von `check`/`build`).

# Clean Code (Auszug der verbindlichen Regeln)

- **Single Responsibility**, sprechende Namen, kleine Einheiten (Methoden < 20 Zeilen Richtwert).
- **Keine Magic-Strings/-Numbers:** Konstanten in dedizierten Klassen (z. B. `CssClasses`).
- **DRY:** wiederholte Logik extrahieren — gelebtes Beispiel ist die geteilte
  [Such-Engine](/architecture/search-engine.md).
- **Serializable-Klassen** immer mit expliziter `serialVersionUID`.
- **≤ 3 Argumente** (Records für Parameter), Constructor Injection statt `@Autowired`.

# Fehlerbehandlung

- Nie leere catch-Blöcke oder `printStackTrace()` — SLF4J (`log.error("Kontext", e)`).
- Kein `null` zurückgeben (`Optional<T>`); Business-Fehler als saubere `Notification`.

# Citations

[1] [CLAUDE.md – Code-Qualität, Clean Code, Fehlerbehandlung](https://github.com/MaKnoNet/XMLViewer/blob/master/CLAUDE.md)
