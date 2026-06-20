package de.makno.web.common.component.xmlviewer;

/**
 * Zentrale Sammlung aller CSS-Klassennamen, die der {@link XmlViewer} verwendet.
 *
 * <p>Die Namen sind hier gebündelt, damit sie nicht als Magic-Strings über den Code verstreut sind
 * und exakt zu den Regeln in {@code web/common/component/xmlviewer/styles/xml-viewer.css} passen.
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
    /** Schließende-Tag-Zeile. */
    static final String ENDTAG = "xml-endtag";
    /** Zentrierte Marker-Box am Ende der Linie; das Abschluss-Symbol liefert CSS {@code ::before}. */
    static final String ENDTAG_MARKER = "xml-endtag-marker";
    /** Klickbares Auf-/Zuklapp-Dreieck (Marker kommt aus CSS {@code ::before}). */
    static final String TOGGLE = "xml-toggle";
    /** Zustands-Modifier am Toggle: gesetzt = zugeklappt (wählt den Collapsed-Marker per CSS). */
    static final String TOGGLE_COLLAPSED = "xml-toggle--collapsed";
    /** Einrückungs-Platzhalter (gleiche Breite wie das Dreieck). */
    static final String INDENT = "xml-indent";
    /**
     * Eine Einrückungs-/Führungslinien-Zelle pro Vorfahr-Ebene, die einer Zeile vorangestellt wird
     * (gleiche Breite wie der Toggle). Trägt das senkrechte Linien-Segment dieser Ebene – analog zu den
     * Pro-Zeile-Zellen des CodeViewers, sodass Marker, Linie und Elbow exakt gleich gerendert werden.
     */
    static final String RAIL = "xml-rail";

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

    /**
     * Markiert einen durchsuchbaren Token-Span. Über diese (geteilte) Klasse findet das
     * Frontend-Highlighting die Treffer-Knoten positionsbasiert
     * ({@code querySelectorAll('.search-token')[tokenIndex]}); zugleich begrenzt sie die
     * {@code ::highlight()}-Stilregeln auf Token-Texte. Die Reihenfolge dieser Spans im DOM entspricht
     * exakt der Token-Liste (Dokumentreihenfolge). Muss exakt dem {@code TOKEN_SELECTOR} in
     * {@code search/search-highlighter.js} entsprechen.
     */
    static final String SEARCH_TOKEN = "search-token";

    private CssClasses() {}
}
