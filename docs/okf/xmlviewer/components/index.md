# Konzepte

* [CodeViewer](/components/codeviewer.md) - Read-only Quelltext-Ansicht für viele Sprachen (Java, C#, Python, JSON, YAML, HTML, CSS, JS, XML, SQL …) mit Syntax-Highlighting, sprachgenauem Falten und hell/dunkel-Theme; dünner Wrapper um CodeMirror 6.
* [Dummy](/components/dummy.md) - KB-Hook-Test (wird revertiert).
* [SearchNavigator und MatchNavigable](/components/search-navigator.md) - Such-Pille (Eingabefeld + Treffer-Label + Vor/Zurück) und das entkoppelnde Interface MatchNavigable, über das alle Viewer-Komponenten suchbar werden.
* [TextViewer](/components/textviewer.md) - Read-only Klartext-Anzeige mit Zeilennummern, Zeilen-Highlight und umschaltbarem Umbruch; nutzt dieselbe Such-Engine und denselben SearchNavigator wie XmlViewer.
* [XmlViewer](/components/xmlviewer.md) - Vaadin-Flow-Komponente, die einen org.jdom2.Element-Baum als eingefärbte, einrückende Quelltext-Ansicht mit Klappen, Hervorheben und Suche rendert.
