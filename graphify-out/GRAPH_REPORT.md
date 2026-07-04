# Graph Report - XMLViewer  (2026-07-04)

## Corpus Check
- 45 files · ~21,963 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 707 nodes · 1404 edges · 37 communities (32 shown, 5 thin omitted)
- Extraction: 92% EXTRACTED · 8% INFERRED · 0% AMBIGUOUS · INFERRED: 119 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `15e7b105`
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
- [[_COMMUNITY_Community 29|Community 29]]
- [[_COMMUNITY_Community 30|Community 30]]
- [[_COMMUNITY_Community 31|Community 31]]
- [[_COMMUNITY_Community 32|Community 32]]

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

## Communities (37 total, 5 thin omitted)

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
Cohesion: 0.05
Nodes (38): license, name, overrides, @codemirror/lang-css, @codemirror/lang-html, @codemirror/lang-java, @codemirror/lang-javascript, @codemirror/lang-json (+30 more)

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
Cohesion: 0.08
Nodes (14): MatchLabelFormatter, MatchNavigable, SearchHighlightRenderer, SearchTermSplitter, Serializable, String, ComponentEventListener, MatchChangeEvent (+6 more)

### Community 9 - "Community 9"
Cohesion: 0.12
Nodes (9): SearchController, SerializableRunnable, Set, List, SearchHighlightRenderer, SearchTermSplitter, SearchToken, String (+1 more)

### Community 10 - "Community 10"
Cohesion: 0.11
Nodes (14): CodeDemoView, SampleCodeFactory, SampleTextFactory, TextDemoView, CodeViewer, CodeLanguage, HorizontalLayout, CodeLanguage (+6 more)

### Community 11 - "Community 11"
Cohesion: 0.14
Nodes (10): cm6Id(), CodeLanguage(), extensions(), CodeLanguageDetector, CodeLanguageDetectorTest, List, String, CodeLanguage (+2 more)

### Community 12 - "Community 12"
Cohesion: 0.14
Nodes (3): Object, Test, XmlViewerTest

### Community 13 - "Community 13"
Cohesion: 0.11
Nodes (13): MatchLabelFormatter, MatchChangeEvent, SearchNavigator, VaadinIcon, Component, AttachEvent, Button, DetachEvent (+5 more)

### Community 14 - "Community 14"
Cohesion: 0.13
Nodes (7): SearchableToken, List, Override, RenderedTree, SearchToken, String, XmlViewer

### Community 15 - "Community 15"
Cohesion: 0.15
Nodes (8): CodeViewerTest, BeforeEach, Component, List, Object, Stream, String, Test

### Community 16 - "Community 16"
Cohesion: 0.18
Nodes (21): devDependencies, async, @babel/preset-react, glob, @preact/signals-react-transform, rollup-plugin-brotli, @rollup/plugin-replace, rollup-plugin-visualizer (+13 more)

### Community 17 - "Community 17"
Cohesion: 0.20
Nodes (6): RecordingRenderer, SearchControllerTest, List, Override, Test, TokenMatch

### Community 18 - "Community 18"
Cohesion: 0.12
Nodes (16): API, Architektur (Kurzüberblick), Bauen & Starten, Code-Formatierung, Eclipse, Eigenes Suchfeld anbinden, Highlights, Lizenz (+8 more)

### Community 19 - "Community 19"
Cohesion: 0.14
Nodes (13): Architektur- & Entwicklungsregeln (Vaadin/Java), Architektur & Entwurfsmuster, Clean Code, Code-Formatierung, Code-Qualität, Fehlerbehandlung & Resilienz, Multiuser & Multithreading (kritisch), Performance & Memory (+5 more)

### Community 20 - "Community 20"
Cohesion: 0.26
Nodes (7): JsonArray, FrontendSearchHighlighter, SearchHighlightRenderer, Component, List, Override, TokenMatch

### Community 22 - "Community 22"
Cohesion: 0.36
Nodes (3): SampleXmlFactory, Element, String

### Community 23 - "Community 23"
Cohesion: 0.22
Nodes (7): HasSize, Component, ComponentEventListener, Div, MatchChangeEvent, Registration, SearchTermSplitter

### Community 24 - "Community 24"
Cohesion: 0.42
Nodes (9): apply(), clear(), currentFromFlat(), isSupported(), moveCurrent(), pruneDetachedRoots(), rangeFor(), rebuild() (+1 more)

### Community 25 - "Community 25"
Cohesion: 0.43
Nodes (4): MainView, Element, HorizontalLayout, XmlViewer

### Community 26 - "Community 26"
Cohesion: 0.40
Nodes (3): BeforeEach, Component, List

### Community 27 - "Community 27"
Cohesion: 0.50
Nodes (3): Application, AppShellConfigurator, String

## Knowledge Gaps
- **95 isolated node(s):** `version`, `configurations`, `name`, `license`, `type` (+90 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **5 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `Span` connect `Community 7` to `Community 0`, `Community 1`, `Community 3`, `Community 5`, `Community 8`, `Community 13`, `Community 15`, `Community 23`, `Community 28`?**
  _High betweenness centrality (0.114) - this node is a cross-community bridge._
- **Why does `CodeViewer` connect `Community 1` to `Community 15`?**
  _High betweenness centrality (0.114) - this node is a cross-community bridge._
- **Why does `of()` connect `Community 5` to `Community 0`, `Community 3`, `Community 9`, `Community 11`, `Community 12`, `Community 14`, `Community 15`, `Community 17`?**
  _High betweenness centrality (0.096) - this node is a cross-community bridge._
- **What connects `version`, `configurations`, `name` to the rest of the system?**
  _95 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Community 0` be split into smaller, more focused modules?**
  _Cohesion score 0.12073170731707317 - nodes in this community are weakly interconnected._
- **Should `Community 1` be split into smaller, more focused modules?**
  _Cohesion score 0.10256410256410256 - nodes in this community are weakly interconnected._
- **Should `Community 2` be split into smaller, more focused modules?**
  _Cohesion score 0.07564102564102564 - nodes in this community are weakly interconnected._