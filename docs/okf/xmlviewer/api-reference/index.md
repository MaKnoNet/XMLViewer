# Konzepte

* [CodeCssClasses](/api-reference/code-css-classes.md) - Package-private Konstantenklasse mit dem einzigen CSS-Klassennamen des CodeViewer-Host-Elements.
* [CodeLanguageDetector](/api-reference/code-language-detector.md) - Zustandslose Utility-Klasse mit Best-effort-Heuristiken zur Spracherkennung über Datei-Endung oder Inhaltsmuster.
* [CodeLanguage](/api-reference/code-language.md) - Enum der vom CodeViewer unterstützten Sprachen mit CodeMirror-6-Language-Id und typischen Datei-Endungen je Konstante.
* [CodeViewer](/api-reference/code-viewer.md) - Vaadin-Flow-Komponente, dünner Wrapper um CodeMirror 6, für read-only Quelltextanzeige mit Syntax-Highlighting, Code-Falten und Theme-Umschaltung.
* [CssClasses](/api-reference/css-classes.md) - Package-private Konstantenklasse mit allen CSS-Klassennamen, die der XmlViewer und sein Renderer verwenden.
* [FrontendSearchHighlighter](/api-reference/frontend-search-highlighter.md) - Standard-SearchHighlightRenderer, der Treffer als Offset-Daten an das Frontend-Modul search-highlighter.js überträgt (CSS Custom Highlight API statt Server-DOM).
* [MatchChangeEvent](/api-reference/match-change-event.md) - Vaadin-ComponentEvent, das eine MatchNavigable-Quelle bei jeder Änderung ihrer Suchtreffer oder Treffer-Navigation feuert — verifizierte Konstruktoren und Zugriffsmethoden.
* [MatchLabelFormatter](/api-reference/match-label-formatter.md) - Funktionales Interface zur freien Formatierung des Treffer-Labels im SearchNavigator.
* [MatchNavigable](/api-reference/match-navigable.md) - Zentrales Entkopplungs-Interface für durchsuchbare, treffer-navigierbare Quellen (Dependency Inversion zwischen SearchNavigator und den konkreten Viewer-Komponenten).
* [RenderedTree](/api-reference/rendered-tree.md) - Package-private Record mit dem Ergebnis eines Render-Durchlaufs von XmlTreeRenderer — Wurzel-Div, identitätsbasierte Element-Abbildungen und die Token-Liste für die Suche.
* [SearchController](/api-reference/search-controller.md) - Zustandsbehafteter Controller, der Textsuche über SearchToken-Listen ermittelt, navigiert und Änderungen meldet; Zeichnen und Sichtbarmachen sind ausgelagert.
* [SearchHighlightRenderer](/api-reference/search-highlight-renderer.md) - Schlanke, serialisierbare Abstraktion für das Zeichnen von Suchtreffern, entkoppelt von deren Ermittlung im SearchController.
* [SearchNavigator](/api-reference/search-navigator.md) - Vollständige, verifizierte Methodenreferenz der eigenständigen Such-Leiste SearchNavigator — Konstruktor, Lifecycle-Methoden, öffentliche API mit Null-Verhalten und Exceptions.
* [SearchTermSplitter](/api-reference/search-term-splitter.md) - Funktionales Interface, das den Sucheingabe-Text in einzelne hervorzuhebende Begriffe zerlegt; das Trennverhalten ist frei austauschbar.
* [SearchToken](/api-reference/search-token.md) - Record für ein durchsuchbares Text-Token (Klartext + Reveal-Aktion), die Grundeinheit der Textsuche im search-Package.
* [SearchableToken](/api-reference/searchable-token.md) - Package-private Record, der einen durchsuchbaren Text-Span mit seinem Klartext und dem besitzenden Element bündelt.
* [TextCssClasses](/api-reference/text-css-classes.md) - Package-private Konstantensammlung aller CSS-Klassennamen, die TextViewer verwendet.
* [TextViewer](/api-reference/text-viewer.md) - Vaadin-Flow-Komponente für read-only mehrzeiligen Klartext mit Zeilennummern, Zeilen-Highlight, umschaltbarem Umbruch und geteilter Textsuche.
* [TokenMatch](/api-reference/token-match.md) - Record für einen einzelnen Suchtreffer als Zeichen-Bereich innerhalb eines durchsuchbaren Tokens.
* [XmlTreeRenderer](/api-reference/xml-tree-renderer.md) - Package-private, single-use Renderer, der einen org.jdom2.Element-Baum in eine Vaadin-Div/Span-Struktur überführt und dabei Highlight-/Klapp-Nachschlage-Strukturen sowie die durchsuchbare Token-Liste aufbaut.
* [XmlViewer](/api-reference/xml-viewer.md) - Vollständige, verifizierte Methodenreferenz der Vaadin-Komponente XmlViewer — Konstruktoren, alle öffentlichen und package-private Methoden mit Null-Verhalten, Rückgabewerten und Exceptions.
