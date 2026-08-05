# Update-Log

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
