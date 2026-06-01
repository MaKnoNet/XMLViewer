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
- **Sauberer Aufbau** – getrennte Verantwortlichkeiten: `XmlTreeRenderer` (Rendering),
  `XmlSearchController` (Suche), `CssClasses` (Klassennamen), schlankes `XmlViewer` (API).
- **Keine Spring-Abhängigkeit in der Komponente** – nur die Demo-App nutzt Spring Boot.

## Voraussetzungen

- **Java 21** (Gradle Toolchain)
- **Vaadin 24** (`vaadin-spring-boot-starter` – nur für die Demo), **JDOM2** (`org.jdom:jdom2`)
- Tests: **JUnit 5**
- Erster Build lädt automatisch **Node.js** herunter und baut das Vaadin-Frontend (Internet nötig).

## Schnellstart

```java
import de.makno.xmlviewer.component.XmlViewer;
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
| `search(String)` | Treffer markieren und zum ersten springen (leer = Suche löschen) |
| `nextMatch()` / `previousMatch()` | durch die Treffer navigieren (umlaufend) |
| `clearSearch()` | Such-Markierungen entfernen |
| `getMatchCount()` | Anzahl der aktuellen Treffer |
| `getCurrentMatchIndex()` | 0-basierter Index des aktuellen Treffers (`-1` = keiner) |
| `addMatchChangeListener(...)` | Event bei jeder Such-/Navigationsänderung (für externe Zähleranzeige) |
| `setSearchCaseSensitive(boolean)` | Groß-/Kleinschreibung der Suche (Standard: aus) |

> **Hinweis:** `highlight(...)` vergleicht **per Identität**. Übergib dieselbe `Element`-Instanz, die
> auch im angezeigten Baum steckt.

## Styling anpassen

Im Java-Code werden **nur CSS-Klassen** gesetzt. Farben/Größen kommen aus
`frontend/styles/xml-viewer.css` und sind über **CSS Custom Properties** anpassbar – ohne Java zu
ändern. Property auf `.xmlviewer` (oder einem Vorfahren) überschreiben:

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
| `xml-search-match` | `--xmlviewer-search-match-bg` | Suchtreffer |
| `xml-search-current` | `--xmlviewer-search-current-bg` | aktueller Suchtreffer |

Weiter: `--xmlviewer-font-family`, `--xmlviewer-font-size`, `--xmlviewer-indent-width`.

**Abgrenzung zum Hintergrund** (Rahmen der Komponente): `--xmlviewer-border-color` (Standard
`#cbd5e1`), `--xmlviewer-border-width` (`1px`), `--xmlviewer-border-radius` (`6px`),
`--xmlviewer-background` (`#ffffff`).

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
./gradlew build          # kompiliert + JUnit-5-Tests (inkl. Format-Check)
./gradlew bootRun        # Demo-App -> http://localhost:8080
./gradlew test           # nur Tests
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

## Architektur (Kurzüberblick)

Die Quellen sind in drei Packages getrennt: die wiederverwendbare Anzeige-Komponente in
`de.makno.xmlviewer.component`, die Such-Navigations-Komponente in `de.makno.xmlviewer.navigation`
und die Demo-App in `de.makno.xmlviewer.app`.

| Klasse | Aufgabe |
|---|---|
| `component.XmlViewer` | Öffentliche API + Highlight/Collapse/Scroll; orchestriert Renderer & Suche (kein Spring) |
| `component.XmlTreeRenderer` | Rekursives Rendern eines `Element`-Baums in `Div`/`Span` |
| `component.XmlSearchController` | Textsuche: Treffer markieren, navigieren, Änderungen melden |
| `component.CssClasses` | Zentrale CSS-Klassennamen (keine Magic-Strings) |
| `component.RenderedTree` / `SearchableToken` | Records: Render-Ergebnis bzw. durchsuchbares Token |
| `navigation.MatchNavigable` | Interface: suchen + Treffer durchlaufen + Stand abfragen (entkoppelt die UI) |
| `navigation.MatchChangeEvent` | Event bei Änderung der Treffer/-navigation |
| `navigation.MatchLabelFormatter` | Funktionales Interface: Label-Format frei bestimmbar (Standard „12/66") |
| `navigation.SearchNavigator` | Such-Pille: Eingabefeld + Treffer-Label + Vor/Zurück (Buttons nur bei Treffern aktiv); steuert ein `MatchNavigable` |
| `frontend/styles/xml-viewer.css` | Farb-/Layout-Regeln + Custom Properties |
| `app.Application` | Spring-Boot-Start der Demo |
| `app.MainView` / `app.SampleXmlFactory` | Demo-View / großer Beispielbaum |

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

## Eclipse

Import als **Existing Gradle Project** (Buildship). Encoding (UTF-8) und Java-21-Compliance kommen
über `build.gradle`.

## Lizenz

Noch nicht festgelegt.
