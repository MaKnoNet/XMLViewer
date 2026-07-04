# Graph Report - XMLViewer  (2026-07-04)

## Corpus Check
- 62 files · ~25,140 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 782 nodes · 1473 edges · 45 communities (36 shown, 9 thin omitted)
- Extraction: 92% EXTRACTED · 8% INFERRED · 0% AMBIGUOUS · INFERRED: 119 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `c358a223`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- [[_COMMUNITY_Community 0|Community 0]]
- [[_COMMUNITY_Community 1|Community 1]]
- [[_COMMUNITY_Community 2|Community 2]]
- [[_COMMUNITY_Community 3|Community 3]]
- [[_COMMUNITY_Community 4|Community 4]]
- [[_COMMUNITY_Community 5|Community 5]]
- [[_COMMUNITY_Community 6|Community 6]]
- [[_COMMUNITY_Community 7|Community 7]]
- [[_COMMUNITY_Community 8|Community 8]]
- [[_COMMUNITY_Community 9|Community 9]]
- [[_COMMUNITY_Community 10|Community 10]]
- [[_COMMUNITY_Community 11|Community 11]]
- [[_COMMUNITY_Community 12|Community 12]]
- [[_COMMUNITY_Community 13|Community 13]]
- [[_COMMUNITY_Community 14|Community 14]]
- [[_COMMUNITY_Community 15|Community 15]]
- [[_COMMUNITY_Community 16|Community 16]]
- [[_COMMUNITY_Community 17|Community 17]]
- [[_COMMUNITY_Community 18|Community 18]]
- [[_COMMUNITY_Community 19|Community 19]]
- [[_COMMUNITY_Community 20|Community 20]]
- [[_COMMUNITY_Community 21|Community 21]]
- [[_COMMUNITY_Community 22|Community 22]]
- [[_COMMUNITY_Community 23|Community 23]]
- [[_COMMUNITY_Community 24|Community 24]]
- [[_COMMUNITY_Community 25|Community 25]]
- [[_COMMUNITY_Community 26|Community 26]]
- [[_COMMUNITY_Community 27|Community 27]]
- [[_COMMUNITY_Community 28|Community 28]]
- [[_COMMUNITY_Community 29|Community 29]]
- [[_COMMUNITY_Community 30|Community 30]]
- [[_COMMUNITY_Community 31|Community 31]]
- [[_COMMUNITY_Community 32|Community 32]]
- [[_COMMUNITY_Community 37|Community 37]]
- [[_COMMUNITY_Community 38|Community 38]]
- [[_COMMUNITY_Community 39|Community 39]]
- [[_COMMUNITY_Community 40|Community 40]]
- [[_COMMUNITY_Community 41|Community 41]]
- [[_COMMUNITY_Community 42|Community 42]]
- [[_COMMUNITY_Community 43|Community 43]]
- [[_COMMUNITY_Community 44|Community 44]]

## God Nodes (most connected - your core abstractions)
1. `XmlViewer` - 39 edges
2. `XmlViewerTest` - 35 edges
3. `TextViewer` - 34 edges
4. `overrides` - 33 edges
5. `CodeViewer` - 32 edges
6. `Test` - 31 edges
7. `TextViewerTest` - 26 edges
8. `of()` - 24 edges
9. `XmlTreeRenderer` - 24 edges
10. `SearchController` - 20 edges

## Surprising Connections (you probably didn't know these)
- `CodeLanguage()` --calls--> `of()`  [INFERRED]
  web-common/src/main/java/de/makno/web/common/component/code/CodeLanguage.java → web-common/src/main/java/de/makno/web/common/component/search/SearchToken.java

## Import Cycles
- None detected.

## Communities (45 total, 9 thin omitted)

### Community 0 - "Community 0"
Cohesion: 0.12
Nodes (14): FakeNavigable, SearchNavigatorTest, TextField, BeforeEach, Button, Component, ComponentEventListener, List (+6 more)

### Community 1 - "Community 1"
Cohesion: 0.10
Nodes (12): ClientCallable, CodeViewer, Div, MatchNavigable, AttachEvent, CodeLanguage, ComponentEventListener, DetachEvent (+4 more)

### Community 2 - "Community 2"
Cohesion: 0.08
Nodes (30): applyMatches(), braceFoldService, buildExtensions(), buildFoldTreeDeco(), clearSearch(), collectMatches(), computeFoldRegions(), create() (+22 more)

### Community 3 - "Community 3"
Cohesion: 0.10
Nodes (12): HasStyle, SearchController, TextViewer, Component, ComponentEventListener, Div, List, MatchChangeEvent (+4 more)

### Community 4 - "Community 4"
Cohesion: 0.06
Nodes (33): overrides, @codemirror/lang-css, @codemirror/lang-html, @codemirror/lang-java, @codemirror/lang-javascript, @codemirror/lang-json, @codemirror/lang-python, @codemirror/lang-sql (+25 more)

### Community 5 - "Community 5"
Cohesion: 0.10
Nodes (11): of(), TextViewerTest, SearchToken, String, BeforeEach, Component, List, Object (+3 more)

### Community 6 - "Community 6"
Cohesion: 0.11
Nodes (34): dependencies, @codemirror/lang-css, @codemirror/lang-html, @codemirror/lang-java, @codemirror/lang-javascript, @codemirror/lang-json, @codemirror/lang-python, @codemirror/lang-sql (+26 more)

### Community 7 - "Community 7"
Cohesion: 0.25
Nodes (8): Content, Span, Div, Element, List, RenderedTree, String, XmlTreeRenderer

### Community 8 - "Community 8"
Cohesion: 0.05
Nodes (23): MatchLabelFormatter, MatchNavigable, SearchController, SearchHighlightRenderer, SearchTermSplitter, Serializable, SerializableRunnable, Set (+15 more)

### Community 9 - "Community 9"
Cohesion: 0.28
Nodes (12): bundle_relative_link(), check_conformance(), main(), Warn-only OKF-Checks: type-Pflichtfeld, keine relativen ../-Links., Liest title/description/type aus dem YAML-Frontmatter (naiver Zeilen-Parser)., Bundle-root-absoluter Link gemaess OKF-Spec (Abschnitt 5.1)., Erzeugt den index.md-Inhalt fuer ein Verzeichnis (deterministisch sortiert)., Schreibt nur bei Aenderung (haelt Hook-Ausgabe und git status ruhig). (+4 more)

### Community 10 - "Community 10"
Cohesion: 0.11
Nodes (13): MainView, SampleTextFactory, SampleXmlFactory, TextDemoView, Element, HorizontalLayout, String, Element (+5 more)

### Community 11 - "Community 11"
Cohesion: 0.14
Nodes (10): cm6Id(), CodeLanguage(), extensions(), CodeLanguageDetector, CodeLanguageDetectorTest, List, String, CodeLanguage (+2 more)

### Community 12 - "Community 12"
Cohesion: 0.05
Nodes (21): HasSize, SearchableToken, Component, ComponentEventListener, Div, Element, List, MatchChangeEvent (+13 more)

### Community 13 - "Community 13"
Cohesion: 0.11
Nodes (13): MatchLabelFormatter, MatchChangeEvent, SearchNavigator, VaadinIcon, Component, AttachEvent, Button, DetachEvent (+5 more)

### Community 14 - "Community 14"
Cohesion: 0.33
Nodes (5): Besonderheiten, Citations, Examples, Schema, Überblick

### Community 15 - "Community 15"
Cohesion: 0.09
Nodes (16): CodeDemoView, SampleCodeFactory, CodeViewerTest, CodeViewer, CodeLanguage, HorizontalLayout, CodeLanguage, String (+8 more)

### Community 16 - "Community 16"
Cohesion: 0.13
Nodes (26): devDependencies, async, @babel/preset-react, glob, @preact/signals-react-transform, rollup-plugin-brotli, @rollup/plugin-replace, rollup-plugin-visualizer (+18 more)

### Community 17 - "Community 17"
Cohesion: 0.20
Nodes (6): RecordingRenderer, SearchControllerTest, List, Override, Test, TokenMatch

### Community 18 - "Community 18"
Cohesion: 0.11
Nodes (17): API, Architektur (Kurzüberblick), Bauen & Starten, Code-Formatierung, Eclipse, Eigenes Suchfeld anbinden, Highlights, Knowledge Base (+9 more)

### Community 19 - "Community 19"
Cohesion: 0.12
Nodes (16): Architektur- & Entwicklungsregeln (Vaadin/Java), Architektur & Entwurfsmuster, Clean Code, Code-Formatierung, Code-Qualität, End-of-Session-Routine (Pflicht bei Code-/Architekturänderungen), Fehlerbehandlung & Resilienz, Graph-First-Regel (+8 more)

### Community 20 - "Community 20"
Cohesion: 0.26
Nodes (7): JsonArray, FrontendSearchHighlighter, SearchHighlightRenderer, Component, List, Override, TokenMatch

### Community 21 - "Community 21"
Cohesion: 0.40
Nodes (4): Citations, Multiuser-Regeln (kritisch), Think Before You Code (Pflicht vor jeder Änderung), Verbindliche Muster

### Community 22 - "Community 22"
Cohesion: 0.40
Nodes (4): Citations, Entwurfsentscheidungen, Schema, Überblick

### Community 23 - "Community 23"
Cohesion: 0.40
Nodes (4): Citations, Examples, Schema, Überblick

### Community 24 - "Community 24"
Cohesion: 0.42
Nodes (9): apply(), clear(), currentFromFlat(), isSupported(), moveCurrent(), pruneDetachedRoots(), rangeFor(), rebuild() (+1 more)

### Community 25 - "Community 25"
Cohesion: 0.40
Nodes (4): Citations, Clean Code (Auszug der verbindlichen Regeln), Fehlerbehandlung, Formatierung

### Community 26 - "Community 26"
Cohesion: 0.50
Nodes (3): Abgrenzung npm, Citations, Überblick

### Community 27 - "Community 27"
Cohesion: 0.50
Nodes (3): Application, AppShellConfigurator, String

### Community 28 - "Community 28"
Cohesion: 0.50
Nodes (3): Citations, Leitplanken, Überblick

### Community 37 - "Community 37"
Cohesion: 0.50
Nodes (3): Besonderheiten, Citations, Überblick

### Community 38 - "Community 38"
Cohesion: 0.50
Nodes (3): Besonderheiten, Citations, Überblick

### Community 39 - "Community 39"
Cohesion: 0.50
Nodes (3): Befehle, Citations, Regeln

## Knowledge Gaps
- **140 isolated node(s):** `version`, `configurations`, `name`, `license`, `type` (+135 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **9 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `Span` connect `Community 7` to `Community 0`, `Community 1`, `Community 3`, `Community 5`, `Community 8`, `Community 12`, `Community 13`, `Community 15`?**
  _High betweenness centrality (0.093) - this node is a cross-community bridge._
- **Why does `CodeViewer` connect `Community 1` to `Community 15`?**
  _High betweenness centrality (0.093) - this node is a cross-community bridge._
- **Why does `of()` connect `Community 5` to `Community 0`, `Community 3`, `Community 8`, `Community 11`, `Community 12`, `Community 15`, `Community 17`?**
  _High betweenness centrality (0.079) - this node is a cross-community bridge._
- **What connects `version`, `configurations`, `name` to the rest of the system?**
  _145 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Community 0` be split into smaller, more focused modules?**
  _Cohesion score 0.12073170731707317 - nodes in this community are weakly interconnected._
- **Should `Community 1` be split into smaller, more focused modules?**
  _Cohesion score 0.10256410256410256 - nodes in this community are weakly interconnected._
- **Should `Community 2` be split into smaller, more focused modules?**
  _Cohesion score 0.07564102564102564 - nodes in this community are weakly interconnected._