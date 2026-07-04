# Konzepte

* [CodeViewer](/components/codeviewer.md) - Read-only Quelltext-Ansicht für viele Sprachen (Java, C#, Python, JSON, YAML, HTML, CSS, JS, XML, SQL …) mit Syntax-Highlighting, sprachgenauem Falten und hell/dunkel-Theme; dünner Wrapper um CodeMirror 6.
* [Demo-App (Application, MainView, SampleXmlFactory)](/components/demo-app.md) - Spring-Boot-Einstiegspunkt und XmlViewer-Startseite der Demo; erzeugt einen großen Beispiel-XML-Baum inkl. Namespace, Kommentar, CDATA und XML-Sonderzeichen zum Testen von Scrollen, Highlight und Suche.
* [SearchNavigator und MatchNavigable](/components/search-navigator.md) - Such-Pille (Eingabefeld + Treffer-Label + Vor/Zurück) und das entkoppelnde Interface MatchNavigable, über das alle Viewer-Komponenten suchbar werden.
* [TextViewer](/components/textviewer.md) - Read-only Klartext-Anzeige mit Zeilennummern, Zeilen-Highlight und umschaltbarem Umbruch; nutzt dieselbe Such-Engine und denselben SearchNavigator wie XmlViewer.
* [XmlViewer](/components/xmlviewer.md) - Vaadin-Flow-Komponente, die einen org.jdom2.Element-Baum als eingefärbte, einrückende Quelltext-Ansicht mit Klappen, Hervorheben und Suche rendert.
