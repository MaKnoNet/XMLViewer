package de.makno.web.common.component.text;

/**
 * Zentrale Sammlung aller CSS-Klassennamen, die der {@link TextViewer} verwendet.
 *
 * <p>Die Namen sind hier gebündelt, damit sie nicht als Magic-Strings über den Code verstreut sind
 * und exakt zu den Regeln in {@code web/common/component/text/styles/text-viewer.css} passen.
 */
final class TextCssClasses {

    /** Wurzel-Container der Komponente. */
    static final String ROOT = "textviewer";
    /** Scrollbarer Zeilen-Container. */
    static final String CONTENT = "textviewer-content";
    /** Platzhaltertext, wenn kein Text gesetzt ist. */
    static final String EMPTY = "textviewer-empty";

    /** Eine Textzeile (Gutter + Inhalt). */
    static final String LINE = "text-line";
    /** Optionale Zeilennummern-Spalte. */
    static final String GUTTER = "text-gutter";
    /** Der eigentliche Zeilentext (zugleich durchsuchbares Token). */
    static final String LINE_CONTENT = "text-line-content";

    /**
     * Markiert den durchsuchbaren Zeilen-Span. Über diese (mit dem XmlViewer geteilte) Klasse findet
     * das Frontend-Highlighting die Treffer-Knoten positionsbasiert. Muss exakt dem
     * {@code TOKEN_SELECTOR} in {@code search/search-highlighter.js} entsprechen.
     */
    static final String SEARCH_TOKEN = "search-token";

    /** Per {@link TextViewer#highlight(int)} hervorgehobene Zeile. */
    static final String HIGHLIGHT = "text-highlight";

    /** Modifier am Wurzelelement: aktiviert den Zeilenumbruch ({@link TextViewer#setWrap(boolean)}). */
    static final String WRAP = "textviewer--wrap";

    private TextCssClasses() {}
}
