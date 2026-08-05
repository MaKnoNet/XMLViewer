# Update-Log

## 2026-08-05 (2)

* **Update**: Repo-Build von Vaadin 24.5.3 auf **25.2.5** angehoben — damit laufen
  Demo-App und Tests real gegen die Generation, in der `elemental.json` fehlt.
  Mitgezogen: Spring Boot 3.3.5 → **4.1.0** (Vaadin 25 unterstützt Boot 3 nicht mehr),
  Gradle 8.9 → **9.6.1** (Boot 4 verlangt ≥ 8.14), Spotless 6.25.0 → **8.9.0** (erste
  Gradle-9-taugliche Linie), Servlet-API 6.0.0 → **6.1.0** (Jakarta EE 11, nur Testscope).
* **Fix**: `installGitHooks` nutzt statt `Project.exec()` den injizierten
  `ExecOperations`-Service — Gradle 9 hat `Project.exec()` zur Ausführungszeit entfernt.
* **Fix**: `:demo-app:processResources` deklariert `dependsOn vaadinPrepareFrontend`;
  Gradle 9 lehnt die vom Vaadin-Plugin nicht deklarierte Abhängigkeit auf
  `build/vaadin-generated` sonst als „implicit dependency" ab.
* **Fix**: `developmentOnly 'com.vaadin:vaadin-dev'` in `demo-app` ergänzt — ab Vaadin 25
  sind die Dev-Tools nicht mehr im Starter enthalten und `bootRun` bricht sonst mit
  `'vaadin-dev-server' not found` ab.
* **Update**: [Build, Test und Release](/conventions/build-and-release.md) um die
  Toolchain-Tabelle, die Gradle-9- und Vaadin-25-Besonderheiten sowie einen offenen Punkt
  erweitert: Vaadin 25 markiert `META-INF/resources/frontend/` für Add-on-Frontend-Quellen
  als deprecated, ein Wechsel auf `META-INF/frontend/` bräche aber die
  Vaadin-24-Kompatibilität — bleibt daher bewusst liegen.
* **Verifikation**: Demo gegen Vaadin 25 im Browser geprüft — XmlViewer 120 Treffer und
  120 registrierte Highlight-Ranges, TextViewer 2663/2663, CodeViewer 1/3 über den
  `@ClientCallable`-Rückweg; leere Suche ergibt 0 Ranges (Leerstring-Pfad in `parseFlat`),
  keine Konsolenfehler. Die erste Range trägt exakt den Suchtext mit Offsets 0–7.

## 2026-08-05

* **Fix**: `FrontendSearchHighlighter` überträgt die Suchtreffer nicht mehr als
  `elemental.json.JsonArray`, sondern als flache Zahlenfolge
  `"tokenIndex,start,end,…"` (`toFlatCsv`, package-private `static`). Vaadin 25 hat
  `elemental.json` entfernt (Ersatz: Jackson 3) — in einer Vaadin-25-Host-Anwendung
  scheiterte die Suche deshalb zur Laufzeit mit `NoClassDefFoundError`. `String` ist
  der einzige `executeJs`-Parametertyp, den Vaadin 24 **und** 25 unterstützen; damit
  bleibt ein Artefakt auf beiden Generationen lauffähig, ohne Jackson und ohne
  Branch-Aufspaltung. Gegenstück `search-highlighter.js` zerlegt die Folge in
  `parseFlat`; Trennzeichen beidseitig als Konstante `SEPARATOR`.
* **Creation**: neue Konvention
  [Vaadin-Versionsunabhängigkeit](/conventions/vaadin-versionsunabhaengigkeit.md) —
  Tabelle der je Vaadin-Generation zulässigen `executeJs`-Parametertypen plus
  Prüfpunkte für künftige Änderungen.
* **Update**: `api-reference/search/frontend-search-highlighter/to-flat-array.md` →
  `to-flat-csv.md` (verifizierte Signatur, `null`-Verhalten jetzt über `matches.size()`
  statt Schleifen-Iterator); Klassenübersicht um die Felder `SEPARATOR` und
  `CHARS_PER_MATCH` ergänzt; `render.md` auf die neue Hilfsmethode umgestellt;
  [Frontend-Integration](/architecture/frontend-integration.md) um den bislang
  undokumentierten Abschnitt „Draht-Format Server → Browser" erweitert.
* **Update**: neuer Unit-Test `FrontendSearchHighlighterTest` sichert das Draht-Format
  und die No-op-Zusicherung ohne gebundene UI ab — die Kodierung war bisher
  ungetestet (`SearchControllerTest` nutzt nur den `RecordingRenderer`).

## 2026-07-08

* **Restructure**: `api-reference/` von einer flachen Datei pro Klasse auf **einen
  Ordner pro Klasse mit einer Datei pro Methode** umgestellt, zusätzlich nach
  Java-Sub-Package gruppiert (`code/`, `navigation/`, `search/`, `text/`, `xmlviewer/` —
  21 Klassen-Ordner). Jede Methoden-Überladung landet zusammen in einer Datei, alle
  Konstruktoren einer Klasse in `constructor.md`. Vier neue Pflichtabschnitte je
  Klassen-Übersichtsdatei ergänzt: `# Felder`, `# Thread-Safety`, `# Serialisierung`
  (`serialVersionUID = 1L` durchweg bei den Serializable-Klassen, u. a. `CodeViewer`,
  `RenderedTree`, `XmlViewer`, `SearchController`, `TextViewer`) und
  `# equals/hashCode/toString`. 107 Cross-Links im Bundle auf die neuen Pfade migriert,
  teils methodengenau verlinkt (z. B. `SearchController#splitTerms`).

## 2026-07-07 (2)

* **Update**: alle 21 `api-reference/*.md`-Dateien um einen verifizierten
  Abschnitt `# Vererbungshierarchie` ergänzt (Superklasse/Interfaces vorwärts,
  bekannte Implementierer/Subklassen rückwärts, per Grep über den gesamten
  Quellbaum geprüft). Bemerkenswerteste Befunde: `MatchNavigable` wird von
  `XmlViewer`/`TextViewer`/`CodeViewer` implementiert (jetzt bidirektional
  verlinkt); `CodeViewer extends Div` direkt, während `XmlViewer`/`TextViewer`
  über `Composite<Div>` gehen; `SearchController implements Serializable`
  direkt, inkonsistent zu `SearchHighlightRenderer`/`SearchTermSplitter`, die
  es über ein eigenes Interface erweitern.

## 2026-07-07

* **Creation**: neue Kategorie `api-reference/` (21 Dateien, eine pro Klasse unter
  `web-common/src/main/java/de/makno/web/common/component/`) — erschöpfende,
  code-verifizierte Konstruktor-/Methodenreferenz (Parameter, Null-Verhalten,
  Rückgabewert-Semantik, tatsächlich geworfene Exceptions), ergänzend zu den
  narrativen `components/`-/`architecture/`-Dokus. Gegen den echten Code verifiziert
  statt Javadoc blind zu übernehmen; 5 Diskrepanzen gefunden und richtiggestellt —
  u. a. fehlendes `Objects.requireNonNull` in `XmlViewer`/`TextViewer`'s
  `setSearchTermSplitter`, fehlender Null-Guard in `cssClassesOfTokenText`,
  inkonsistentes Null-Handling zwischen `SearchController`-Konstruktor und
  `SearchNavigator`-Konstruktor. Details in den jeweiligen `api-reference/*.md`-Dateien.
* **Update**: [Entwicklerdoku](/conventions/okf-entwicklerdoku.md) und `AGENTS.md`
  um die `api-reference/`-Konvention (Zweck, Abgrenzung zu `components/`, Pflicht zur
  Code-Verifikation) ergänzt.

## 2026-07-06 (2)

* **Update**: Projekt-Konventionen von `CLAUDE.md` nach `AGENTS.md` migriert
  (herstellerneutraler Standard, damit Instruktionen über verschiedene KI-Coding-Tools
  portabel bleiben). `CLAUDE.md` ist jetzt nur noch ein dünner `@AGENTS.md`-Import.
  Betrifft die „Single Source of Truth"-Verweise in dieser
  [Entwicklerdoku](/conventions/okf-entwicklerdoku.md).

## 2026-07-06

* **Update**: `CLAUDE.md`-Routine von „End-of-Session" auf „Pre-Commit" verschärft —
  betroffene OKF-Konzepte werden ab sofort vor jedem einzelnen Commit mit Code-/
  Architekturänderungen aktualisiert statt gebündelt am Sitzungsende;
  [Entwicklerdoku](/conventions/okf-entwicklerdoku.md) entsprechend angepasst.
* **Creation**: neues Konzept
  [Entwicklerdoku – OKF-Wissensdatenbank pflegen](/conventions/okf-entwicklerdoku.md) —
  Bundle-Struktur, Frontmatter-Konvention, Schritt-für-Schritt „Neues Konzept anlegen",
  Automatisierung/Hybrid-Strategie, bekannte Stolpersteine.

## 2026-07-04

* **Initialisierung**: OKF-Bundle angelegt — Architektur-Konzepte
  ([Modulstruktur](/architecture/module-structure.md),
  [Such-Engine](/architecture/search-engine.md),
  [Frontend-Integration](/architecture/frontend-integration.md),
  [Design-Regeln](/architecture/design-rules.md)), Komponenten-Konzepte
  ([XmlViewer](/components/xmlviewer.md), [TextViewer](/components/textviewer.md),
  [CodeViewer](/components/codeviewer.md),
  [SearchNavigator](/components/search-navigator.md)) und Konventionen
  ([Build & Release](/conventions/build-and-release.md),
  [Code-Stil](/conventions/code-style.md)) aus README.md/CLAUDE.md abgeleitet.
* **Creation**: graphify-Wissensgraph erstmalig erzeugt (707 Knoten, 1404 Kanten,
  37 Communities); Aktualisierung ab jetzt automatisch per Pre-Commit-Hook.

## 2026-07-05

* **Update**: Hauptclone auf den Stand nach der Text-/CodeViewer-Refaktorierung
  synchronisiert (`git pull`); Wissensgraph neu gebaut (782 Knoten, 1473 Kanten,
  45 Communities).
* **Update**: Semantische Lücken geschlossen — [XmlViewer](/components/xmlviewer.md)
  um `RenderedTree`/`SearchableToken` ergänzt, [TextViewer](/components/textviewer.md)
  um `TextCssClasses`, [CodeViewer](/components/codeviewer.md) um `CodeCssClasses`.
* **Creation**: neues Konzept [Demo-App](/components/demo-app.md) —
  `Application`/`MainView`/`SampleXmlFactory`.
