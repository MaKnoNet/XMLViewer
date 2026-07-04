# Projekt-Konventionen – XMLViewer

## Code-Qualität

- **Es wird IMMER nach Clean-Code-Richtlinien entwickelt.** Das umfasst insbesondere:
  - **Single Responsibility:** Jede Klasse/Methode hat genau eine klar abgegrenzte Aufgabe.
  - **Sprechende Namen:** Klassen, Methoden und Variablen benennen ihre Absicht, nicht ihre Implementierung.
  - **Kleine Einheiten:** Methoden sind kurz und auf einer Abstraktionsebene; Klassen bleiben überschaubar.
  - **Keine Magic-Strings/-Numbers:** Konstanten werden in dedizierten Klassen (z. B. `CssClasses`) gehalten.
  - **Keine Duplikate (DRY):** Wiederholte Logik wird extrahiert und wiederverwendet.
  - **Saubere Fehlerbehandlung:** Null-/Edge-Cases explizit behandeln, kein stilles Verschlucken.
  - **Serializable-Klassen** erhalten immer eine explizite `serialVersionUID`.

## Code-Formatierung

- **Java-Quellcode wird IMMER mit `palantir-java-format` formatiert.**
  Vor dem Commit/Abschluss einer Änderung muss der geänderte Java-Code in diesem Format vorliegen.

---

# Architektur- & Entwicklungsregeln (Vaadin/Java)

Verbindliche Konventionen für dieses Vaadin-Flow-/Java-Projekt. Ergänzen die globalen
Konventionen (`~/.claude/CLAUDE.md`) und die obigen Abschnitte; bei Widerspruch gewinnt die
speziellere Regel.

## Think Before You Code (Pflicht-Reasoning vor jeder Code-Änderung)

Vor dem Generieren, Refactoren oder Ändern von Code MUSS ein expliziter Denkschritt erfolgen, der
den Ansatz gegen diese vier Achsen prüft:

1. **Concurrency-Check:** Ist der Code sicher für eine multithreaded, multiuser Server-Umgebung?
   (Geteilter Zustand? Fehlende `ui.access`-Aufrufe? Unsichere Thread-Variablen?)
2. **Performance-Check:** Drohen Memory-Leaks oder hohe server-seitige Ressourcenlast?
   (Werden Komponenten lazy geladen? Ist der Layout-Baum flach? Sind DataProvider dynamisch?)
3. **Design-Pattern-Anwendung:** Welches Entwurfsmuster löst das Problem am saubersten?
4. **Testability-Check:** Lässt sich UI-/Business-Logik leicht unit-/headless-testen, ohne vollen
   Server?

## Projektkontext & Befehle

- **Tech-Stack:** Java (aktuelle LTS), Vaadin Flow, Spring Boot / Jakarta EE.
- **Build:** Gradle (Groovy `build.gradle` oder Kotlin DSL); Wrapper `gradlew`/`gradlew.bat`.
- **Architektur:** Server-seitiges State-Management, Multiuser, Multithreaded, High Performance.

| Zweck | Befehl |
|---|---|
| Build & Dev vorbereiten | `./gradlew clean vaadinPrepareFrontend` |
| Lokal starten (Dev) | `./gradlew bootRun` (hier: `:demo-app:bootRun`) |
| Tests & Coverage | `./gradlew test jacocoTestReport` |
| Production-Build | `./gradlew clean build -Pvaadin.productionMode=true` |
| Formatieren | `./gradlew spotlessApply` |

## Architektur & Entwurfsmuster

- **Composite Pattern:** Komplexe Views in kleine, wiederverwendbare Bausteine zerlegen
  (`Composite<T>`). Keine Monolith-Views.
- **Observer / Event-Listener:** Vaadins Event-Bus bzw. Spring `@EventListener` zum Entkoppeln von
  Views und async Hintergrundprozessen. Listener beim Detach immer aufräumen.
- **Presenter / Mediator:** Bei komplexer UI-Logik View (Layout) von Navigations-/Business-Flow
  trennen. Die View emittiert nur Events; ein Presenter/Coordinator steuert den Workflow.
- **Builder:** Für komplexe Domain-Entities, DTOs oder Vaadin-Komponenten (z. B. Dialog-Builder) –
  Lesbarkeit + Immutability.
- **Strategy:** Austauschbare Algorithmen (Export-Formate, Berechnungen, Payment) statt langer
  `if-else`/`switch`-Ketten.
- **Factory:** Wenn die Komponentenerzeugung von Benutzerrolle, Mandant oder Laufzeitkonfiguration
  abhängt.

## Multiuser & Multithreading (kritisch)

- **Keine stateful Singletons/Beans:** `@Component`/`@Service`/Singletons speichern NIE benutzer-
  oder UI-spezifischen Zustand in Feldern.
- **Thread-Safety:** Thread-sichere Collections (`ConcurrentHashMap`)/Locks nur, wenn Cross-Session-
  Synchronisation zwingend nötig ist.
- **`ui.access(() -> { ... })`** beim UI-Update aus einem Hintergrund-Thread – für korrektes Locking
  der `VaadinSession`.
- **ThreadLocals** meiden, außer sauber aufgeräumt (Requests laufen auf wiederverwendeten Web-Server-
  Threads).

## Vaadin-State & UI-Management

- **Scoping:** UI-Komponenten/Views korrekt scopen (`@Route`-Views je Session/Navigation).
- **Memory-Leaks:** Listener/Observer/`Registration` im `onDetach` mit `Registration.remove()` lösen.
- **Backend-Trennung:** Business-Logik strikt von UI trennen; **stateless** Services in Views
  injizieren.
- **Serializable:** Alle in Vaadin-Komponenten gehaltenen Objekte implementieren `Serializable`
  (Session-Clustering).

## Performance & Memory

- **Lazy Komponenten:** Tabs/Dialoge/Detail-Layouts erst instanziieren, wenn sichtbar.
- **Flacher Komponentenbaum:** Tiefe Layout-Verschachtelung vermeiden; CSS/Flexbox bevorzugen.
- **Keine großen Read-Only-Daten in der UI:** nur IDs/minimale UI-DTOs halten.
- **Lazy DataProvider:** `Grid` lazy via `DataProvider.fromCallbacks` (offset/limit); kein
  `grid.setItems(collection)` für große Datenmengen.
- **Leichte Renderer:** `LitRenderer`/Text statt interaktiver Komponenten pro Zelle.
- **Eager Detach:** Große Datasets/Komponenten beim Verbergen/Detach freigeben (`null`/clear).

## Clean Code

- **Sprechende Namen:** Vaadin-Komponenten nach Zweck benennen (`saveButton`, `customerGrid`).
- **Klein & fokussiert (SRP):** Methoden tun genau eine Sache (Richtwert < 20 Zeilen); komplexe
  Formulare in eigene Komponenten extrahieren.
- **Wenige Argumente & Constructor Injection:** ≤ 3 Argumente (Records für Parameter); Constructor
  Injection statt `@Autowired`.

## Fehlerbehandlung & Resilienz

- **Keine verschluckten Exceptions:** nie leerer catch-Block oder `printStackTrace()`; SLF4J
  (`log.error("Kontext", e)`).
- **Null-Safety & User-Feedback:** kein `null` zurückgeben (`Optional<T>`); Business-Fehler als
  saubere `Notification` zeigen; **nie** rohe Stacktraces in die UI leaken.
- **Globaler `ErrorHandler`:** an der `VaadinSession` registrieren, um unerwartete Hintergrund-
  Thread-Fehler abzufangen, ohne die UI einzufrieren.

## Tests & Coverage

- **Hohe Abdeckung** über Business-Logik, Validatoren und Kern-UI-Interaktionen.
- **Frameworks:** Business-Logik mit JUnit 5 + Mockito; Vaadin-Views/Übergänge headless mit
  **Karibu-Testing** oder **Vaadin TestBench**.
- **Concurrent- & Negativtests:** Integrationstests, die nebenläufige Aktionen/Hintergrund-Threads
  simulieren, um `ui.access`-Locks und Randbedingungen abzusichern.

---

# Knowledge Base (graphify + OKF)

Dieses Repo enthält eine automatisch mitgeführte Wissensdatenbank:

- **Wissensgraph:** `graphify-out/graph.json` (+ `GRAPH_REPORT.md`, `graph.html`) –
  deterministisch aus dem Java-AST erzeugt, wird per Pre-Commit-Hook aktuell gehalten.
- **OKF-Bundle:** `docs/okf/xmlviewer/` – kuratierte Konzept-Dokumente (Architektur,
  Komponenten, Konventionen) mit YAML-Frontmatter nach der Open-Knowledge-Format-Spec v0.1.
- **`index.md`-Dateien sind GENERIERT** (`tools/kb/generate_okf_index.py`) – NIE von Hand
  editieren; der Pre-Commit-Hook regeneriert sie.

## Graph-First-Regel

Bei Fragen zur Codebasis (Struktur, Abhängigkeiten, „wo ist X?", „was nutzt Y?") ZUERST den
Wissensgraphen befragen (`graphify query "<Frage>"` bzw. `GRAPH_REPORT.md`), dann erst frei
suchen.

## Pre-Commit-Routine (Pflicht vor JEDEM Commit mit Code-/Architekturänderungen)

Nicht erst am Sitzungsende bündeln – vor **jedem einzelnen** Commit, der Code oder
Architektur ändert:

1. **OKF-Konzepte aktualisieren:** alle Konzepte unter `docs/okf/xmlviewer/`, die von den
   Änderungen dieses Commits berührt sind (neue Komponente → neues Konzept mit
   `type`-Frontmatter; API-Änderung → `# Schema` anpassen; Muster-/Konventionsänderung →
   `architecture/` bzw. `conventions/`).
2. **`docs/okf/xmlviewer/log.md`:** Eintrag unter dem heutigen ISO-Datum ergänzen
   (neueste zuerst).
3. **`graphify update .`** ausführen (deterministisch, kein LLM nötig – Kontrolle vor dem
   Commit; der Hook macht es sonst beim Commit).
4. **Erst dann committen** – Doku-Änderung und Code-Änderung landen im selben Commit,
   nie in getrennten "Doku hinterher"-Commits.

Trivial-Commits ohne Code-/Architekturrelevanz (Formatierung, reine Kommentar-Tippfehler
o. Ä.) sind von dieser Routine ausgenommen.

Frontmatter-Minimum je Konzeptdatei: `type` (Pflicht), `title`, `description`, `resource`
(repo-relativer Pfad), `tags`, `timestamp`. Querverweise bundle-root-absolut,
z. B. `/components/xmlviewer.md`.
