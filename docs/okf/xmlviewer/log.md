# Update-Log

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
