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

### Marker und Führungslinie anpassen

Die Auf-/Zuklapp-Marker, das Symbol am schließenden Tag und der Stil der Führungslinie kommen
vollständig aus CSS (über `::before`-Inhalte) – der Java-Code setzt **kein** Zeichen. So lassen sich
die Symbole durch **Unicode, Emoji oder ein SVG** ersetzen, ohne Java anzufassen:

| Custom Property | Default | Bedeutung |
|---|---|---|
| `--xmlviewer-marker-expanded` | SVG (Quadrat mit Minus) | Marker am aufgeklappten Element – als CSS-Maske gerendert (einfärbbar) |
| `--xmlviewer-marker-collapsed` | `"\229E"` (⊞) | Marker am zugeklappten Element |
| `--xmlviewer-marker-endtag` | `"\2514"` (└) | Symbol am Ende der Linie vor dem `</tag>` |
| `--xmlviewer-marker-color` | `--xmlviewer-punct-color` | Farbe aller Marker |
| `--xmlviewer-guide-color` | `--xmlviewer-marker-color` | Linienfarbe (Default = Markerfarbe) |
| `--xmlviewer-guide-width` | `2px` | Linienbreite |
| `--xmlviewer-guide-style` | `solid` | Linienstil (`solid`/`dashed`/`dotted`) |

```css
/* Beispiel 1 – andere Unicode-/Emoji-Zeichen + auffälligere Linie */
.xmlviewer {
    --xmlviewer-marker-expanded: "📂";
    --xmlviewer-marker-collapsed: "📁";
    --xmlviewer-marker-endtag: "⤷";
    --xmlviewer-guide-color: #e11d48;
    --xmlviewer-guide-width: 2px;
    --xmlviewer-guide-style: dashed;
}

/* Beispiel 2 – eigenes SVG für den aufgeklappten Marker.
   Der aufgeklappte Marker wird per CSS-Maske gezeichnet (siehe .xml-toggle::before): Es zählt nur
   der Alpha-Kanal des SVG, die Farbe kommt aus --xmlviewer-marker-color. Daher genügt ein SVG ohne
   eigene Farbe; einfach die url(...) ersetzen (data-URI oder Datei-URL). */
.xmlviewer {
    --xmlviewer-marker-expanded: url("/icons/folder-open.svg");
}
```

> **SVG vs. Zeichen:** Der aufgeklappte Marker ist standardmäßig ein SVG (Quadrat mit Minus) und per
> Maske einfärbbar. Für `--xmlviewer-marker-collapsed` und `--xmlviewer-marker-endtag` werden
> Text-Zeichen über `content` gesetzt – dort eignen sich Unicode/Emoji am besten (leicht, scharf,
> über `--xmlviewer-marker-color` einfärbbar). Wer auch dort ein SVG braucht, kann die jeweilige
> `::before`-Regel analog zu `.xml-toggle::before` auf `mask` umstellen.

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

Die Quellen sind in zwei Packages getrennt: die wiederverwendbare Komponente in
`de.makno.xmlviewer.component`, die Demo-App in `de.makno.xmlviewer.app`.

| Klasse | Aufgabe |
|---|---|
| `component.XmlViewer` | Öffentliche API + Highlight/Collapse/Scroll; orchestriert Renderer & Suche (kein Spring) |
| `component.XmlTreeRenderer` | Rekursives Rendern eines `Element`-Baums in `Div`/`Span` |
| `component.XmlSearchController` | Textsuche: Treffer markieren, navigieren, Änderungen melden |
| `component.CssClasses` | Zentrale CSS-Klassennamen (keine Magic-Strings) |
| `component.RenderedTree` / `SearchableToken` | Records: Render-Ergebnis bzw. durchsuchbares Token |
| `frontend/styles/xml-viewer.css` | Farb-/Layout-Regeln + Custom Properties |
| `app.Application` | Spring-Boot-Start der Demo |
| `app.MainView` / `app.SampleXmlFactory` | Demo-View mit eigener Such-UI / großer Beispielbaum |

Rendering-Prinzip: pro `Element` eine Start-Tag-Zeile (`Span`-Folge), ein eingerückter
Kinder-Container und eine End-Tag-Zeile. Ein `IdentityHashMap<Element, Div>` bildet Elemente auf ihre
Knoten ab (für `highlight`); eine Token-Liste trägt die durchsuchbaren Spans.

## Eclipse

Import als **Existing Gradle Project** (Buildship). Encoding (UTF-8) und Java-21-Compliance kommen
über `build.gradle`.

## Lizenz

Noch nicht festgelegt.
