package de.makno.web.common.component.text;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.shared.Registration;
import de.makno.web.common.component.navigation.MatchChangeEvent;
import de.makno.web.common.component.navigation.MatchNavigable;
import de.makno.web.common.component.search.FrontendSearchHighlighter;
import de.makno.web.common.component.search.SearchController;
import de.makno.web.common.component.search.SearchHighlightRenderer;
import de.makno.web.common.component.search.SearchTermSplitter;
import de.makno.web.common.component.search.SearchToken;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Vaadin-Flow-Komponente, die einen <strong>mehrzeiligen Klartext</strong> read-only anzeigt – mit
 * optionalen Zeilennummern, programmatischem Zeilen-Highlight und derselben Textsuche/-navigation wie
 * der {@code XmlViewer}. Eine Zeile entspricht genau einem durchsuchbaren Token (Token-Index =
 * Zeilenindex).
 *
 * <h2>Funktionen</h2>
 * <ul>
 *   <li>Mehrzeiliger Text über {@link #setText(String)} (CRLF/CR werden zu LF vereinheitlicht).</li>
 *   <li>Optionale Zeilennummern ({@link #setShowLineNumbers(boolean)}).</li>
 *   <li>Umschaltbarer Zeilenumbruch ({@link #setWrap(boolean)}); Standard: kein Umbruch, lange Zeilen
 *       scrollen horizontal.</li>
 *   <li>{@link #highlight(int)}: eine Zeile programmatisch hervorheben und hinscrollen.</li>
 *   <li>Programmatische Textsuche ({@link #search(String)}, {@link #nextMatch()},
 *       {@link #previousMatch()}, {@link #clearSearch()}) mit Treffer-Status über
 *       {@link #getMatchCount()} / {@link #getCurrentMatchIndex()} und Änderungs-Events über
 *       {@link #addMatchChangeListener}; dadurch direkt mit {@code SearchNavigator} koppelbar.</li>
 * </ul>
 *
 * <p>Die Komponente enthält bewusst <strong>keine</strong> Such-UI; eine solche baut die einbettende
 * Anwendung selbst (siehe {@code SearchNavigator}). Sie hat keine Spring-Abhängigkeit.
 *
 * <h2>Thread-Safety</h2>
 * <strong>Nicht thread-safe.</strong> Eine Instanz gehört wie jede Vaadin-Komponente zu genau einer
 * {@code UI}/Session; Methoden nur aus dem an die Session gebundenen Thread (mit Session-Lock)
 * aufrufen, eine Instanz pro UI.
 *
 * <h2>Styling</h2>
 * Im Java-Code werden ausschließlich CSS-Klassen gesetzt. Stilwerte kommen aus
 * {@code web/common/component/text/styles/text-viewer.css} und sind über CSS Custom Properties
 * (z.&nbsp;B. {@code --textviewer-text-color}) ohne Java-Änderung override-bar; die Suchtreffer-Farben
 * über das geteilte {@code search.css} ({@code --search-match-bg} / {@code --search-current-bg}).
 */
@CssImport("./web/common/component/text/styles/text-viewer.css")
@CssImport("./web/common/component/search/styles/search.css")
@JsModule("./web/common/component/search/search-highlighter.js")
public class TextViewer extends Composite<Div> implements HasSize, HasStyle, MatchNavigable {

    private static final long serialVersionUID = 1L;

    private static final String EMPTY_TEXT = "Kein Text gesetzt.";

    private String text = "";
    private boolean wrap = false;
    private boolean showLineNumbers = false;
    private boolean searchCaseSensitive = false;
    private SearchTermSplitter searchTermSplitter = SearchController.DEFAULT_TERM_SPLITTER;

    /** Die gerenderten Zeilen-Zeilen (Index == Zeilenindex) – für {@link #highlight(int)}. */
    private List<Div> lineElements = List.of();
    /** Der Klartext je gerenderter Zeile (Index == Zeilenindex). */
    private List<String> lineTexts = List.of();
    /** Indizes der aktuell per {@link #highlight(int)} hervorgehobenen Zeilen. */
    private final Set<Integer> highlightedLines = new HashSet<>();

    private SearchController searchController;

    /** Zeichnet die Suchtreffer im Frontend (CSS Custom Highlight API); Element-Referenz ist stabil. */
    private final SearchHighlightRenderer highlightRenderer = new FrontendSearchHighlighter(this);

    public TextViewer() {
        getContent().addClassName(TextCssClasses.ROOT);
        render();
    }

    public TextViewer(String text) {
        this();
        setText(text);
    }

    // ------------------------------------------------------------------------
    // Inhalt
    // ------------------------------------------------------------------------

    /** Setzt den anzuzeigenden Text und rendert neu. {@code null} wird als leerer Text behandelt. */
    public void setText(String text) {
        this.text = text == null ? "" : text;
        render();
    }

    public String getText() {
        return text;
    }

    // ------------------------------------------------------------------------
    // Darstellung
    // ------------------------------------------------------------------------

    /** Blendet die Zeilennummern-Spalte ein/aus (rendert neu). */
    public void setShowLineNumbers(boolean showLineNumbers) {
        this.showLineNumbers = showLineNumbers;
        render();
    }

    public boolean isShowLineNumbers() {
        return showLineNumbers;
    }

    /**
     * Schaltet den Zeilenumbruch um. {@code false} (Standard): lange Zeilen brechen nicht um und
     * erzeugen horizontales Scrollen; {@code true}: lange Zeilen werden umbrochen. Kein Neu-Rendern
     * nötig – nur ein Modifier am Wurzelelement.
     */
    public void setWrap(boolean wrap) {
        this.wrap = wrap;
        getContent().getElement().getClassList().set(TextCssClasses.WRAP, wrap);
    }

    public boolean isWrap() {
        return wrap;
    }

    // ------------------------------------------------------------------------
    // Zeilen-Hervorhebung
    // ------------------------------------------------------------------------

    /**
     * Hebt die angegebene Zeile hervor ({@link TextCssClasses#HIGHLIGHT}) und scrollt sie in den
     * sichtbaren Bereich. Bereits hervorgehobene Zeilen bleiben erhalten – mehrere Zeilen können
     * gleichzeitig aktiv sein. Außerhalb des Bereichs liegende Indizes werden ignoriert.
     *
     * @param line 0-basierter Zeilenindex
     */
    public void highlight(int line) {
        if (line < 0 || line >= lineElements.size()) {
            return;
        }
        Div row = lineElements.get(line);
        row.addClassName(TextCssClasses.HIGHLIGHT);
        highlightedLines.add(line);
        scrollTo(row);
    }

    /** Entfernt alle Zeilen-Hervorhebungen. */
    public void clearHighlight() {
        highlightedLines.forEach(line -> lineElements.get(line).removeClassName(TextCssClasses.HIGHLIGHT));
        highlightedLines.clear();
    }

    // ------------------------------------------------------------------------
    // Suche (programmatisch – UI liegt in der einbettenden Anwendung)
    // ------------------------------------------------------------------------

    /**
     * Sucht {@code query} im dargestellten Text, markiert alle Treffer und springt zum ersten.
     * Leerer/{@code null}-Text löscht die Suche.
     */
    @Override
    public void search(String query) {
        searchController.search(query);
    }

    /** Springt zum nächsten Suchtreffer (umlaufend). */
    @Override
    public void nextMatch() {
        searchController.nextMatch();
    }

    /** Springt zum vorherigen Suchtreffer (umlaufend). */
    @Override
    public void previousMatch() {
        searchController.previousMatch();
    }

    /** Entfernt alle Such-Markierungen. */
    public void clearSearch() {
        searchController.clearSearch();
    }

    /** Anzahl der aktuellen Suchtreffer. */
    @Override
    public int getMatchCount() {
        return searchController.getMatchCount();
    }

    /** 0-basierter Index des aktuellen Treffers, oder {@code -1}, wenn keiner aktiv ist. */
    @Override
    public int getCurrentMatchIndex() {
        return searchController.getCurrentMatchIndex();
    }

    /** Schaltet Groß-/Kleinschreibungsabgleich der Suche um (Standard: aus). */
    public void setSearchCaseSensitive(boolean caseSensitive) {
        this.searchCaseSensitive = caseSensitive;
        searchController.setCaseSensitive(caseSensitive);
    }

    public boolean isSearchCaseSensitive() {
        return searchCaseSensitive;
    }

    /**
     * Legt fest, wie der Suchtext in die einzeln hervorzuhebenden Begriffe zerlegt wird. Standard:
     * Trennung an Whitespace. Eine aktive Suche wird mit dem neuen Splitter sofort neu ausgeführt.
     *
     * @see SearchTermSplitter
     */
    public void setSearchTermSplitter(SearchTermSplitter splitter) {
        this.searchTermSplitter = Objects.requireNonNull(splitter, "splitter");
        searchController.setTermSplitter(splitter);
    }

    /**
     * Registriert einen Listener, der bei jeder Änderung der Suchtreffer oder der Treffer-Navigation
     * gefeuert wird – ideal, um eine externe Zähleranzeige (z.&nbsp;B. „3/12") synchron zu halten.
     */
    @Override
    public Registration addMatchChangeListener(ComponentEventListener<MatchChangeEvent> listener) {
        return addListener(MatchChangeEvent.class, listener);
    }

    // ------------------------------------------------------------------------
    // Rendering / interne Helfer
    // ------------------------------------------------------------------------

    private void render() {
        clearHighlight();
        getContent().removeAll();

        List<SearchToken> tokens;
        if (text.isEmpty()) {
            lineElements = List.of();
            lineTexts = List.of();
            tokens = List.of();
            getContent().add(newEmptyPlaceholder());
        } else {
            lineTexts = splitLines(text);
            List<Div> rows = new ArrayList<>(lineTexts.size());
            tokens = new ArrayList<>(lineTexts.size());
            Div content = new Div();
            content.addClassName(TextCssClasses.CONTENT);
            for (int index = 0; index < lineTexts.size(); index++) {
                String lineText = lineTexts.get(index);
                Div row = newLine(index, lineText);
                content.add(row);
                rows.add(row);
                tokens.add(SearchToken.of(lineText));
            }
            lineElements = rows;
            getContent().add(content);
        }

        searchController = new SearchController(tokens, highlightRenderer, this::fireMatchChange);
        searchController.setCaseSensitive(searchCaseSensitive);
        searchController.setTermSplitter(searchTermSplitter);
        // Etwaige Highlights eines vorherigen Texts im Frontend verwerfen (neuer Text = neue Tokens).
        highlightRenderer.clear();
        // Beobachter (z. B. SearchNavigator) informieren, dass die Suche durch den Inhaltswechsel
        // zurückgesetzt wurde, damit sie ihr Eingabefeld/Anzeige leeren. Im Konstruktor noch ohne
        // Wirkung (keine Listener registriert).
        fireSearchReset();
    }

    /**
     * Baut eine Zeilen-Zeile: optionale Zeilennummer (Gutter) plus genau einen durchsuchbaren
     * {@code .search-token}-Span mit dem Zeilentext (auch für Leerzeilen, damit die Token-Indizes mit
     * den Zeilenindizes übereinstimmen).
     */
    private Div newLine(int index, String lineText) {
        Div row = new Div();
        row.addClassName(TextCssClasses.LINE);
        if (showLineNumbers) {
            Span gutter = new Span(Integer.toString(index + 1));
            gutter.addClassName(TextCssClasses.GUTTER);
            row.add(gutter);
        }
        Span content = new Span(lineText);
        content.addClassName(TextCssClasses.LINE_CONTENT);
        content.addClassName(TextCssClasses.SEARCH_TOKEN);
        row.add(content);
        return row;
    }

    private Div newEmptyPlaceholder() {
        Span placeholder = new Span(EMPTY_TEXT);
        placeholder.addClassName(TextCssClasses.EMPTY);
        Div container = new Div(placeholder);
        container.addClassName(TextCssClasses.CONTENT);
        return container;
    }

    /**
     * Zerlegt den Text in Zeilen. CRLF/CR werden zu LF vereinheitlicht (plattformneutral); abschließende
     * Leerzeilen bleiben erhalten ({@code limit == -1}), damit die Zeilenzahl dem Text entspricht.
     */
    private static List<String> splitLines(String text) {
        String unified = text.replace("\r\n", "\n").replace('\r', '\n');
        return List.of(unified.split("\n", -1));
    }

    private void fireMatchChange() {
        fireEvent(new MatchChangeEvent(this, getMatchCount(), getCurrentMatchIndex()));
    }

    /** Signalisiert Beobachtern, dass die Suche wegen eines Inhaltswechsels zurückgesetzt wurde. */
    private void fireSearchReset() {
        fireEvent(new MatchChangeEvent(this, getMatchCount(), getCurrentMatchIndex(), true));
    }

    private void scrollTo(Component target) {
        // Nur scrollen, wenn die Komponente an eine UI gebunden ist (im Unit-Test gibt es keinen Client).
        target.getUI().ifPresent(ui -> target.getElement().executeJs("this.scrollIntoView({block:'center'})"));
    }

    // ------------------------------------------------------------------------
    // Paket-sichtbare Helfer (nur für Tests)
    // ------------------------------------------------------------------------

    int lineCount() {
        return lineElements.size();
    }

    List<String> searchableTexts() {
        return List.copyOf(lineTexts);
    }

    Div lineOf(int line) {
        return lineElements.get(line);
    }

    boolean isLineHighlighted(int line) {
        return lineElements.get(line).getElement().getClassList().contains(TextCssClasses.HIGHLIGHT);
    }
}
