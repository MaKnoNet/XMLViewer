# Update-Log

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
