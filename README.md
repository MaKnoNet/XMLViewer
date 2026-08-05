# XMLViewer

Eine **Vaadin-Flow-Komponente (Java 21)**, die einen **`org.jdom2.Element`-Baum** als
**eingefärbte, einrückende Quelltext-Ansicht** darstellt – wie das Syntax-Highlighting eines
Editors. Tag-Namen, Attributnamen, Attributwerte, Text und Kommentare werden **andersfarbig**
gerendert; Element-Blöcke sind **ein-/ausklappbar**, einzelne Elemente lassen sich **hervorheben**
und es gibt eine **programmatische Textsuche** mit Treffer-Navigation (die Such-UI baut die
einbettende Anwendung selbst – siehe Demo).

## Highlights

- **JDOM2-Eingabe** – nimmt direkt ein `org.jdom2.Element` entgegen (`new XmlViewer(element)`).
- **Farbiges Rendering** – je eigene CSS-Klasse für Tag/Attributname/Attributwert/Text/Kommentar.
- **Vollständig CSS-gesteuert** – keine Farben im Java-Code; alle Stile über **CSS Custom Properties**
  von außen override-bar (siehe [Styling](#styling-anpassen)).
- **Hervorheben** – `highlight(element)` markiert ein Element, klappt dessen Vorfahren auf und scrollt
  es in den sichtbaren Bereich (Identitätsvergleich der Instanz).
- **Ein-/Ausklappen** – Dreieck je Element, plus `expandAll()` / `collapseAll()`.
- **Textsuche (entkoppelt)** – `search()`/`nextMatch()`/`previousMatch()`/`clearSearch()` plus
  `getMatchCount()`/`getCurrentMatchIndex()` und `addMatchChangeListener(...)`. Die Komponente enthält
  **keine** Such-UI; ein eigenes Suchfeld + Buttons bindet die Anwendung an (siehe Demo).
- **Sicher** – Inhalte werden über echte Vaadin-Komponenten (`setText`) gerendert; XML-Sonderzeichen
  (`<`, `&`, `"`) werden korrekt escaped, kein innerHTML/XSS.
- **Scrollbar** – lange Zeilen brechen nicht um (`white-space: pre`), der Baum scrollt **horizontal
  und vertikal** innerhalb seiner Größe (`setSizeFull()` / Flex-Höhe).
- **Sauberer Aufbau** – getrennte Verantwortlichkeiten: `XmlTreeRenderer` (Rendering), die geteilte
  `search`-Engine (`SearchController` & Co.), `CssClasses` (Klassennamen), schlankes `XmlViewer` (API).
- **Keine Spring-Abhängigkeit in der Komponente** – nur die Demo-App nutzt Spring Boot.
- **Schwester-Komponente `TextViewer`** – read-only Klartext-Anzeige (Zeilennummern, Zeilen-Highlight,
  umschaltbarer Umbruch) mit derselben Suche/Navigation und demselben `SearchNavigator`; die Such-Engine
  liegt im geteilten Package `de.makno.web.common.component.search`.
- **Schwester-Komponente `CodeViewer`** – read-only Quelltext-Ansicht für viele Sprachen (Java, C#,
  Python, JSON, YAML, HTML, CSS, JS, XML, SQL …) mit Syntax-Highlighting, **sprachgenauem Falten**,
  hell/dunkel-Theme, Zeilennummern und Umbruch – ein dünner Wrapper um **CodeMirror 6**, ebenfalls über
  `MatchNavigable` mit dem `SearchNavigator` koppelbar.

## Voraussetzungen

- **Java 21** (Gradle Toolchain)
- **Vaadin 24 oder 25** – die Bibliothek nutzt bewusst keine an eine Vaadin-Generation
  gebundenen APIs und läuft mit beiden. Dieses Repo baut und testet gegen Vaadin 25
  (`vaadin-spring-boot-starter` – nur für die Demo). **JDOM2** (`org.jdom:jdom2`)
- Tests: **JUnit 5**
- Erster Build lädt automatisch **Node.js** herunter und baut das Vaadin-Frontend (Internet nötig).
- Die **CodeViewer-Demo (`/code`)** zieht dabei zusätzlich **CodeMirror 6** per npm (`@codemirror/*`);
  das Vaadin-Plugin der Demo löst das auf (Node/Netzzugang nötig). Die Bibliothek `web-common` selbst
  bleibt npm-frei und kompiliert/testet ohne Node.

## Schnellstart

```java
import de.makno.web.common.component.xmlviewer.XmlViewer;
import org.jdom2.Element;

Element wurzel = ...; // z. B. aus org.jdom2.input.SAXBuilder

XmlViewer viewer = new XmlViewer(wurzel);
viewer.setSizeFull();

add(viewer);                       // in ein Vaadin-Layout einhängen

viewer.highlight(einElement);      // ein konkretes Element hervorheben
viewer.search("EUR");              // alle Treffer markieren, zum ersten springen
```

### Eigenes Suchfeld anbinden

Die Komponente bringt keine Such-UI mit. So koppelt man ein eigenes Feld + Zähler an:

```java
TextField feld = new TextField();
feld.setValueChangeMode(ValueChangeMode.EAGER);
feld.addValueChangeListener(e -> viewer.search(e.getValue()));

Button vor = new Button("›", e -> viewer.nextMatch());
Button zurueck = new Button("‹", e -> viewer.previousMatch());

Span zaehler = new Span();
viewer.addMatchChangeListener(e -> zaehler.setText(
        e.getMatchCount() == 0 ? "0/0" : (e.getCurrentMatchIndex() + 1) + "/" + e.getMatchCount()));
```

## API

| Methode | Zweck |
|---|---|
| `new XmlViewer()` / `new XmlViewer(Element)` | Komponente, optional direkt mit Wurzelelement |
| `setRoot(Element)` | Wurzelelement setzen und neu rendern (`null` leert die Ansicht) |
| `highlight(Element)` | Element hervorheben, Vorfahren aufklappen, hinscrollen |
| `clearHighlight()` | Hervorhebung entfernen |
| `expandAll()` / `collapseAll()` | alle Elemente auf-/zuklappen |
| `setCollapsible(boolean)` | Aufklapp-Dreiecke an/aus |
| `search(String)` | Treffer markieren und zum ersten springen; mehrere durch Leerzeichen getrennte Begriffe werden einzeln gesucht (ODER); leer = Suche löschen |
| `nextMatch()` / `previousMatch()` | durch die Treffer navigieren (umlaufend) |
| `clearSearch()` | Such-Markierungen entfernen |
| `getMatchCount()` | Anzahl der aktuellen Treffer |
| `getCurrentMatchIndex()` | 0-basierter Index des aktuellen Treffers (`-1` = keiner) |
| `addMatchChangeListener(...)` | Event bei jeder Such-/Navigationsänderung (für externe Zähleranzeige) |
| `setSearchCaseSensitive(boolean)` | Groß-/Kleinschreibung der Suche (Standard: aus) |
| `setSearchTermSplitter(SearchTermSplitter)` | Trennverhalten der Begriffe frei bestimmen (Standard: Whitespace) |

> **Hinweis:** `highlight(...)` vergleicht **per Identität**. Übergib dieselbe `Element`-Instanz, die
> auch im angezeigten Baum steckt.

### Suchbegriffe anders trennen

Standardmäßig wird an Leerzeichen getrennt. Über ein eigenes `SearchTermSplitter` (funktionales
Interface: Suchtext → Liste der hervorzuhebenden Begriffe) lässt sich das frei ändern – z.&nbsp;B.
Trennung an Komma oder „gar nicht trennen":

```java
// An Komma trennen statt an Leerzeichen:
viewer.setSearchTermSplitter(query ->
        Arrays.stream(query.split(",")).map(String::trim).filter(s -> !s.isEmpty()).toList());

// Gesamten Text als EINEN Begriff behandeln (kein Aufsplitten):
viewer.setSearchTermSplitter(List::of);
```

## Styling anpassen

Im Java-Code werden **nur CSS-Klassen** gesetzt. Farben/Größen kommen aus der mitgelieferten
`xml-viewer.css` (im Artefakt unter
`META-INF/resources/frontend/web/common/component/xmlviewer/styles/`) und sind über **CSS Custom
Properties** anpassbar – ohne Java zu ändern. Property auf `.xmlviewer` (oder einem Vorfahren)
überschreiben:

```css
.xmlviewer {
    --xmlviewer-tag-color: #c026d3;
    --xmlviewer-attr-value-color: #0ea5e9;
    --xmlviewer-search-current-bg: #fde047;
}
```

| CSS-Klasse | Custom Property | Inhalt |
|---|---|---|
| `xml-tag` | `--xmlviewer-tag-color` | Tag-Namen |
| `xml-attr-name` | `--xmlviewer-attr-name-color` | Attributnamen |
| `xml-attr-value` | `--xmlviewer-attr-value-color` | Attributwerte |
| `xml-text` | `--xmlviewer-text-color` | Textinhalte / CDATA |
| `xml-comment` | `--xmlviewer-comment-color` | Kommentare |
| `xml-punct` | `--xmlviewer-punct-color` | `< > / = "` |
| `xml-children` / `xml-endtag` | `--xmlviewer-guide-color` | durchgehende Führungslinie vom öffnenden bis zum schließenden Tag |
| `xml-highlight` | `--xmlviewer-highlight-bg` | per `highlight(...)` markiertes Element |
| `.search-token::highlight(search-match)` | `--xmlviewer-search-match-bg` | Suchtreffer |
| `.search-token::highlight(search-current)` | `--xmlviewer-search-current-bg` | aktueller Suchtreffer |

Weiter: `--xmlviewer-font-family`, `--xmlviewer-font-size`, `--xmlviewer-indent-width`.

> **Suchtreffer-Highlighting (Frontend):** Treffer werden nicht server-seitig in Spans zerlegt,
> sondern über die [CSS Custom Highlight API](https://developer.mozilla.org/docs/Web/API/CSS_Custom_Highlight_API)
> als Text-Ranges gezeichnet – durch das geteilte Modul `search/search-highlighter.js` (genutzt von
> `XmlViewer` **und** `TextViewer`). Das spart pro Treffer einen DOM-Knoten und Session-Heap und
> vermeidet DOM-Mutationen über die Leitung – relevant bei großen Bäumen und vielen gleichzeitigen
> Nutzern. Gestylt wird über `.search-token::highlight(...)` (in `search/styles/search.css`); die
> Trefferfarben speisen die `--xmlviewer-search-*-bg`-Properties über eine Brücke auf die geteilten
> `--search-match-bg`/`--search-current-bg`. Da `::highlight()` nur begrenzte Eigenschaften erlaubt
> (u. a. `background-color`/`color`), gibt es hier keinen `border-radius`. Benötigt einen Browser mit
> Custom-Highlight-Unterstützung (aktuelle Chromium-, Firefox- und Safari-Versionen).

**Abgrenzung zum Hintergrund** (Rahmen der Komponente): `--xmlviewer-border-color` (Standard
`#cbd5e1`), `--xmlviewer-border-width` (`1px`), `--xmlviewer-border-radius` (`6px`),
`--xmlviewer-background` (`#f8fafc`).

### Marker und Führungslinie anpassen

Die Auf-/Zuklapp-Marker, das Symbol am schließenden Tag und der Stil der Führungslinie kommen
vollständig aus CSS (über `::before`-Inhalte) – der Java-Code setzt **kein** Zeichen. So lassen sich
die Symbole durch **Unicode, Emoji oder ein SVG** ersetzen, ohne Java anzufassen:

Die vier Tree-Symbole (aufgeklappt, zugeklappt, End-Symbol, Linie) sind **SVGs**, die als
`background-image` gerendert werden und zusammen eine durchgehende „Connected-Tree"-Optik ergeben.

| Custom Property | Default | Bedeutung |
|---|---|---|
| `--xmlviewer-marker-expanded` | SVG: Quadrat mit Minus + Linien-Stummel | Marker am aufgeklappten Element |
| `--xmlviewer-marker-collapsed` | SVG: Quadrat mit Plus | Marker am zugeklappten Element |
| `--xmlviewer-marker-endtag` | SVG: Elbow (`┘`) | Symbol am Ende der Linie vor dem `</tag>` |
| `--xmlviewer-marker-line` | SVG: senkrechte Linie | durchgehende Führungslinie |

> **Farbe der SVG-Symbole:** Die Strichfarbe ist im SVG fest eingebacken (`stroke=%2364748b`). Zum
> Umfärben den Hex-Wert in der jeweiligen `--xmlviewer-marker-*`-Property ändern. Eine zentrale
> Live-Färbung über eine einzelne Variable ist hier bewusst nicht möglich, weil die weiße
> Innenfläche des Quadrats erhalten bleiben muss (Bild- statt Masken-Rendering).

```css
/* Eigene SVGs setzen – data-URI (inline) oder Datei-URL. Die senkrechte Linie sollte bei x=Mitte
   des viewBox liegen, damit Marker und Linie bündig ineinander übergehen. */
.xmlviewer {
    --xmlviewer-marker-expanded: url("/icons/node-open.svg");
    --xmlviewer-marker-collapsed: url("/icons/node-closed.svg");
    --xmlviewer-marker-endtag: url("/icons/elbow.svg");
    --xmlviewer-marker-line: url("/icons/line.svg");
}
```

Die übrigen Stell­schrauben (`--xmlviewer-marker-color`, `--xmlviewer-guide-*`) bleiben als
Text-Fallback erhalten, wirken aber nicht auf die SVG-Bilder.

## Bauen & Starten

```bash
./gradlew build                  # kompiliert beide Module + JUnit-5-Tests (inkl. Format-Check)
./gradlew :demo-app:bootRun      # Demo-App -> http://localhost:8080
./gradlew :web-common:test       # nur die Bibliotheks-Tests
./gradlew :web-common:publishToMavenLocal   # Artefakt de.makno:web-common ins lokale ~/.m2
```

## Code-Formatierung

Java-Code wird verbindlich mit **palantir-java-format** formatiert (eingebunden über das
Spotless-Gradle-Plugin):

```bash
./gradlew spotlessApply  # formatiert den Java-Code
./gradlew spotlessCheck  # prüft das Format (Teil von `check`/`build`)
```

Die Demo (`de.makno.xmlviewer.app.MainView`) zeigt einen großen Beispielkatalog inkl. Namespace,
Kommentar, CDATA und XML-Sonderzeichen, baut die Such-UI selbst und demonstriert Hervorheben,
Auf-/Zuklappen, Suche sowie horizontales und vertikales Scrollen.

## Module & Artefakt

Das Repository ist ein **Multi-Modul-Gradle-Build**, der die wiederverwendbare Bibliothek strikt von
der Demo trennt:

| Modul | Inhalt | Veröffentlichung |
|---|---|---|
| `web-common` | Bibliothek: Anzeige-Komponente (`de.makno.web.common.component.xmlviewer`) + Such-Navigation (`de.makno.web.common.component.navigation`) inkl. Frontend-Ressourcen | Maven-Artefakt **`de.makno:web-common:1.0.0-SNAPSHOT`** (mit `-sources`/`-javadoc`) |
| `demo-app` | Eigenständige Spring-Boot-Demo (`de.makno.xmlviewer.app`) | **nicht** publiziert – kein App-Code im Artefakt |

In einem anderen Projekt einbinden:

```groovy
implementation 'de.makno:web-common:1.0.0-SNAPSHOT'
```

Die Frontend-Dateien (CSS/JS) liegen im Artefakt unter
`META-INF/resources/frontend/web/common/component/...` und werden von Vaadin beim Konsumenten
automatisch aufgelöst – kein zusätzliches Setup nötig.

### Migration von `de.makno.xmlviewer:xmlviewer`

Für Konsumenten der früheren Koordinaten (z. B. *web-module-conversion*) ändert sich dreierlei:

1. **Koordinaten:** `de.makno.xmlviewer:xmlviewer` → `de.makno:web-common`.
2. **Packages/Importe:** `de.makno.xmlviewer.component` → `de.makno.web.common.component.xmlviewer`,
   `de.makno.xmlviewer.navigation` → `de.makno.web.common.component.navigation`.
3. **Transitive Abhängigkeiten:** Das Artefakt liefert **nicht** mehr den
   `vaadin-spring-boot-starter` mit (die Bibliothek ist Spring-frei und exponiert nur die genutzten
   Flow-Module). Spring-Boot-Apps deklarieren den Starter selbst:
   `implementation 'com.vaadin:vaadin-spring-boot-starter'`.

## Architektur (Kurzüberblick)

Die Bibliotheks-Quellen sind nach Verantwortlichkeit getrennt: die Anzeige-Komponenten in
`de.makno.web.common.component.xmlviewer` und `…component.text`, die geteilte Such-Engine in
`…component.search`, die Such-Navigations-Komponente in `…component.navigation`; die Demo-App liegt im
Modul `demo-app` (`de.makno.xmlviewer.app`).

| Klasse | Aufgabe |
|---|---|
| `xmlviewer.XmlViewer` | Öffentliche API + Highlight/Collapse/Scroll; orchestriert Renderer & Suche (kein Spring) |
| `xmlviewer.XmlTreeRenderer` | Rekursives Rendern eines `Element`-Baums in `Div`/`Span` |
| `xmlviewer.CssClasses` | Zentrale CSS-Klassennamen (keine Magic-Strings) |
| `xmlviewer.RenderedTree` / `SearchableToken` | Records: Render-Ergebnis bzw. durchsuchbares XML-Token |
| `text.TextViewer` | Schwester-Komponente: read-only Klartext-Anzeige (Zeilennummern, Zeilen-Highlight, Umbruch) mit Suche/Navigation |
| `code.CodeViewer` | Schwester-Komponente: read-only Quelltext-Ansicht (Syntax-Highlighting, Falten, Theme) – Wrapper um CodeMirror 6, via `MatchNavigable` |
| `code.CodeLanguage` / `CodeLanguageDetector` | Unterstützte Sprachen (CM6-Id + Endungen) und best-effort-Erkennung (Endung/Inhalt) |
| `search.SearchController` | Geteilte Textsuche: Treffer finden/zählen, navigieren, Reveal + Änderungen melden |
| `search.SearchToken` / `TokenMatch` | Records: durchsuchbares Token (Text + Reveal-Aktion) bzw. Treffer-Offset |
| `search.SearchHighlightRenderer` / `FrontendSearchHighlighter` | Treffer-Zeichnen entkoppelt; Standard lagert es ins Frontend aus |
| `search.SearchTermSplitter` | Funktionales Interface: zerlegt den Suchtext in Begriffe (Trennzeichen frei wählbar) |
| `navigation.MatchNavigable` | Interface: suchen + Treffer durchlaufen + Stand abfragen (entkoppelt die UI) |
| `navigation.MatchChangeEvent` | Event bei Änderung der Treffer/-navigation |
| `navigation.MatchLabelFormatter` | Funktionales Interface: Label-Format frei bestimmbar (Standard „12/66") |
| `navigation.SearchNavigator` | Such-Pille: Eingabefeld + Treffer-Label + Vor/Zurück (Buttons nur bei Treffern aktiv); steuert ein `MatchNavigable` |
| `…/frontend/web/common/component/search/search-highlighter.js` + `styles/search.css` | Geteiltes Frontend-Highlighting (CSS Custom Highlight API) |
| `…/frontend/web/common/component/{xmlviewer,text}/styles/*.css` | Farb-/Layout-Regeln + Custom Properties je Komponente (unter `META-INF/resources`) |
| `…/frontend/web/common/component/code/code-viewer.js` + `styles/code-viewer.css` | CodeMirror-6-Glue (Editor + selbst gesteuerte Suche) |
| `app.Application` | Spring-Boot-Start der Demo |
| `app.MainView` / `app.SampleXmlFactory` | XmlViewer-Demo (`/`) / großer Beispielbaum |
| `app.TextDemoView` / `app.SampleTextFactory` | TextViewer-Demo (`/text`) / großer Beispieltext |
| `app.CodeDemoView` / `app.SampleCodeFactory` | CodeViewer-Demo (`/code`) / Beispiel-Snippets je Sprache |

`XmlViewer` implementiert `MatchNavigable`, daher genügt zum Anbinden der Navigation:

```java
XmlViewer viewer = new XmlViewer(wurzel);
SearchNavigator navigator = new SearchNavigator(viewer); // Suchfeld + „12/66" + ‹ / ›
add(navigator);

// Label-Format frei bestimmbar (Standard ist „aktuell/gesamt", z. B. „12/66"):
navigator.setLabelFormatter((count, position) -> "Treffer " + position + " von " + count);
```

Rendering-Prinzip: pro `Element` eine Start-Tag-Zeile (`Span`-Folge), ein eingerückter
Kinder-Container und eine End-Tag-Zeile. Ein `IdentityHashMap<Element, Div>` bildet Elemente auf ihre
Knoten ab (für `highlight`); eine Token-Liste trägt die durchsuchbaren Spans.

## Knowledge Base

Das Repo führt eine **automatisch aktualisierte Wissensdatenbank** mit:

| Was | Wo | Pflege |
|---|---|---|
| Wissensgraph (Code-Struktur) | `graphify-out/graph.json`, Report `GRAPH_REPORT.md`, interaktive Ansicht **`graphify-out/graph.html`** (im Browser öffnen) | automatisch per Pre-Commit-Hook |
| OKF-Bundle (kuratierte Konzepte: Architektur, Komponenten, Konventionen) | `docs/okf/xmlviewer/` (Markdown + YAML-Frontmatter, [Open Knowledge Format v0.1](https://github.com/GoogleCloudPlatform/knowledge-catalog/blob/main/okf/SPEC.md)) | Prosa manuell/per Claude-Session; `index.md`-Dateien werden generiert |

**Einmalige Aktivierung nach dem Klonen** (läuft beim ersten Build automatisch mit):

```bash
./gradlew installGitHooks     # oder manuell: git config core.hooksPath .githooks
```

Der Hook aktualisiert bei jedem Commit mit Java-Änderungen den Wissensgraphen (deterministisch,
ohne LLM) und regeneriert bei OKF-Änderungen die `index.md`-Dateien. **Er blockiert nie einen
Commit** – fehlt ein Werkzeug, erscheint nur ein Hinweis. Optionale Werkzeuge:

```bash
uv tool install graphifyy     # graphify-CLI (Wissensgraph); Python 3 für die Index-Generierung
```

**Wissensstand eines Releases ansehen:** Die KB ist normaler Repo-Inhalt – jeder Tag/Branch
trägt seinen passenden Stand:

```bash
git worktree add ../xmlviewer-v1.0.0 v1.0.0   # Release auschecken, Arbeitskopie bleibt unberührt
# dann ../xmlviewer-v1.0.0/graphify-out/graph.html im Browser öffnen bzw. docs/okf/ lesen
```

**Vor einem Release-Tag:** OKF-Konzepte + `docs/okf/xmlviewer/log.md` auffrischen und
`graphify update .` laufen lassen (Details: AGENTS.md, Abschnitt „Knowledge Base").
Für Agenten steht optional ein MCP-Server bereit: `python -m graphify.serve graphify-out/graph.json`.

## Eclipse

Import als **Existing Gradle Project** (Buildship). Encoding (UTF-8) und Java-21-Compliance kommen
über `build.gradle`.

## Lizenz

Noch nicht festgelegt.
