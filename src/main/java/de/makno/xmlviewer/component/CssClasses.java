package de.makno.xmlviewer.component;

/**
 * Zentrale Sammlung aller CSS-Klassennamen, die der {@link XmlViewer} verwendet.
 *
 * <p>Die Namen sind hier gebündelt, damit sie nicht als Magic-Strings über den Code verstreut sind
 * und exakt zu den Regeln in {@code frontend/styles/xml-viewer.css} passen.
 */
final class CssClasses {

    /** Wurzel-Container der Komponente. */
    static final String ROOT = "xmlviewer";
    /** Scrollbarer Baum-Container. */
    static final String TREE = "xmlviewer-tree";
    /** Platzhaltertext, wenn kein Wurzelelement gesetzt ist. */
    static final String EMPTY = "xmlviewer-empty";

    /** Eine Zeile (Start-Tag, Text, Kommentar oder End-Tag). */
    static final String LINE = "xml-line";
    /** Eingerückter Container für die Kindknoten. */
    static final String CHILDREN = "xml-children";
    /** Schließende-Tag-Zeile (führt die Führungslinie bis zum Tag-Ende weiter). */
    static final String ENDTAG = "xml-endtag";
    /** Klickbares Auf-/Zuklapp-Dreieck. */
    static final String TOGGLE = "xml-toggle";
    /** Einrückungs-Platzhalter (gleiche Breite wie das Dreieck). */
    static final String INDENT = "xml-indent";

    /** Tag-Name. */
    static final String TAG = "xml-tag";
    /** Attributname. */
    static final String ATTR_NAME = "xml-attr-name";
    /** Attributwert. */
    static final String ATTR_VALUE = "xml-attr-value";
    /** Textinhalt / CDATA. */
    static final String TEXT = "xml-text";
    /** Kommentar. */
    static final String COMMENT = "xml-comment";
    /** Satzzeichen ({@code < > / = "}). */
    static final String PUNCT = "xml-punct";

    /** Per {@link XmlViewer#highlight} hervorgehobenes Element. */
    static final String HIGHLIGHT = "xml-highlight";
    /** Ein Suchtreffer. */
    static final String SEARCH_MATCH = "xml-search-match";
    /** Der aktuell fokussierte Suchtreffer. */
    static final String SEARCH_CURRENT = "xml-search-current";

    private CssClasses() {}
}
