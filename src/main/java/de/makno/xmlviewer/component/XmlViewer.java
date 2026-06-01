package de.makno.xmlviewer.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.shared.Registration;
import de.makno.xmlviewer.navigation.MatchChangeEvent;
import de.makno.xmlviewer.navigation.MatchNavigable;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import org.jdom2.Element;

/**
 * Vaadin-Flow-Komponente, die einen {@link org.jdom2.Element}-Baum als eingefärbte, einrückende
 * Quelltext-Ansicht darstellt – vergleichbar mit dem Syntax-Highlighting eines Editors.
 *
 * <h2>Funktionen</h2>
 * <ul>
 *   <li>Tag-Namen, Attributnamen, Attributwerte, Textinhalte und Kommentare je eigene CSS-Klasse
 *       (und damit eigene Farbe).</li>
 *   <li>Element-Blöcke ein-/ausklappbar.</li>
 *   <li>{@link #highlight(Element)}: ein konkretes Element programmatisch hervorheben.</li>
 *   <li>Programmatische Textsuche ({@link #search(String)}, {@link #nextMatch()},
 *       {@link #previousMatch()}, {@link #clearSearch()}) mit Treffer-Status über
 *       {@link #getMatchCount()} / {@link #getCurrentMatchIndex()} und Änderungs-Events über
 *       {@link #addMatchChangeListener}.</li>
 * </ul>
 *
 * <p>Die Komponente enthält bewusst <strong>keine</strong> Such-UI (Suchfeld, Buttons, Zähler);
 * eine solche baut die einbettende Anwendung selbst und ruft die Such-API auf. Ebenso hat die
 * Komponente keine Spring-Abhängigkeit und ist in jedes Vaadin-Projekt einbettbar.
 *
 * <h2>Styling</h2>
 * Im Java-Code werden ausschließlich CSS-Klassen gesetzt, keine Farben inline. Alle Stilwerte kommen
 * aus {@code frontend/styles/xml-viewer.css} und sind über CSS Custom Properties
 * (z.&nbsp;B. {@code --xmlviewer-tag-color}) von außen ohne Java-Änderung override-bar.
 */
@CssImport("./styles/xml-viewer.css")
public class XmlViewer extends Composite<Div> implements HasSize, HasStyle, MatchNavigable {

    private static final long serialVersionUID = 1L;

    private static final String EMPTY_TEXT = "Kein XML-Element gesetzt.";

    private Element root;
    private boolean collapsible = true;
    private boolean searchCaseSensitive = false;

    private RenderedTree tree;
    private XmlSearchController search;

    /**
     * Identitätsbasierte Menge aller aktuell hervorgehobenen Elemente. {@link IdentityHashMap} als
     * Backing stellt sicher, dass dieselbe {@link Element}-Instanz verglichen wird (kein equals).
     */
    private final Set<Element> highlightedElements = Collections.newSetFromMap(new IdentityHashMap<>());

    public XmlViewer() {
        getContent().addClassName(CssClasses.ROOT);
        render();
    }

    public XmlViewer(Element root) {
        this();
        setRoot(root);
    }

    // ------------------------------------------------------------------------
    // Inhalt
    // ------------------------------------------------------------------------

    /** Setzt das anzuzeigende Wurzelelement und rendert den Baum neu. {@code null} leert die Ansicht. */
    public void setRoot(Element root) {
        this.root = root;
        render();
    }

    public Element getRoot() {
        return root;
    }

    // ------------------------------------------------------------------------
    // Hervorhebung
    // ------------------------------------------------------------------------

    /**
     * Hebt das angegebene Element hervor ({@link CssClasses#HIGHLIGHT}), klappt dessen Vorfahren auf
     * und scrollt es in den sichtbaren Bereich. Bereits hervorgehobene Elemente bleiben erhalten –
     * mehrere Elemente können gleichzeitig aktiv sein. Das Element muss dieselbe Instanz sein wie im
     * gesetzten Baum (Identitätsvergleich).
     */
    public void highlight(Element element) {
        if (element == null) {
            return;
        }
        Div header = tree.elementHeaders().get(element);
        if (header == null) {
            return;
        }
        expandTo(element);
        header.addClassName(CssClasses.HIGHLIGHT);
        highlightedElements.add(element);
        scrollTo(header);
    }

    /**
     * Entfernt die Hervorhebung des angegebenen Elements. Alle anderen hervorgehobenen Elemente
     * bleiben unberührt.
     */
    public void clearHighlight(Element element) {
        if (element == null) {
            return;
        }
        Div header = tree.elementHeaders().get(element);
        if (header != null) {
            header.removeClassName(CssClasses.HIGHLIGHT);
        }
        highlightedElements.remove(element);
    }

    /** Entfernt alle aktiven Hervorhebungen. */
    public void clearHighlight() {
        highlightedElements.forEach(element -> {
            Div header = tree.elementHeaders().get(element);
            if (header != null) {
                header.removeClassName(CssClasses.HIGHLIGHT);
            }
        });
        highlightedElements.clear();
    }

    // ------------------------------------------------------------------------
    // Auf-/Zuklappen
    // ------------------------------------------------------------------------

    /** Klappt alle Elemente auf. */
    public void expandAll() {
        tree.childContainers().keySet().forEach(element -> setCollapsed(element, false));
    }

    /** Klappt alle Elemente zu. */
    public void collapseAll() {
        tree.childContainers().keySet().forEach(element -> setCollapsed(element, true));
    }

    /** Schaltet die Aufklapp-Dreiecke an/aus (rendert neu). */
    public void setCollapsible(boolean collapsible) {
        this.collapsible = collapsible;
        render();
    }

    public boolean isCollapsible() {
        return collapsible;
    }

    // ------------------------------------------------------------------------
    // Suche (programmatisch – UI liegt in der einbettenden Anwendung)
    // ------------------------------------------------------------------------

    /**
     * Sucht {@code query} im dargestellten Inhalt, markiert alle Treffer und springt zum ersten.
     * Leerer/{@code null}-Text löscht die Suche.
     */
    public void search(String query) {
        search.search(query);
    }

    /** Springt zum nächsten Suchtreffer (umlaufend). */
    @Override
    public void nextMatch() {
        search.nextMatch();
    }

    /** Springt zum vorherigen Suchtreffer (umlaufend). */
    @Override
    public void previousMatch() {
        search.previousMatch();
    }

    /** Entfernt alle Such-Markierungen. */
    public void clearSearch() {
        search.clearSearch();
    }

    /** Anzahl der aktuellen Suchtreffer. */
    @Override
    public int getMatchCount() {
        return search.getMatchCount();
    }

    /** 0-basierter Index des aktuellen Treffers, oder {@code -1}, wenn keiner aktiv ist. */
    @Override
    public int getCurrentMatchIndex() {
        return search.getCurrentMatchIndex();
    }

    /** Schaltet Groß-/Kleinschreibungsabgleich der Suche um (Standard: aus). */
    public void setSearchCaseSensitive(boolean caseSensitive) {
        this.searchCaseSensitive = caseSensitive;
        search.setCaseSensitive(caseSensitive);
    }

    public boolean isSearchCaseSensitive() {
        return searchCaseSensitive;
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

        if (root == null) {
            tree = emptyTree();
            getContent().add(newEmptyPlaceholder());
        } else {
            tree = new XmlTreeRenderer(collapsible).render(root);
            wireToggles();
            getContent().add(tree.root());
        }
        search = new XmlSearchController(tree.tokens(), this::expandTo, this::scrollTo, this::fireMatchChange);
    }

    /** Verbindet die vom Renderer erzeugten Aufklapp-Dreiecke mit der Klapp-Logik des Viewers. */
    private void wireToggles() {
        tree.toggles().forEach((element, toggle) -> toggle.addClickListener(event -> toggleCollapse(element)));
    }

    private void fireMatchChange() {
        fireEvent(new MatchChangeEvent(this, getMatchCount(), getCurrentMatchIndex()));
    }

    private void toggleCollapse(Element element) {
        Div container = tree.childContainers().get(element);
        if (container != null) {
            setCollapsed(element, container.isVisible());
        }
    }

    private void setCollapsed(Element element, boolean collapsed) {
        Div container = tree.childContainers().get(element);
        if (container == null) {
            return;
        }
        container.setVisible(!collapsed);
        Div endTag = tree.endTags().get(element);
        if (endTag != null) {
            endTag.setVisible(!collapsed);
        }
        Span toggle = tree.toggles().get(element);
        if (toggle != null) {
            // Marker-Zeichen liefert CSS; hier nur den Zustand umschalten.
            toggle.getElement().getClassList().set(CssClasses.TOGGLE_COLLAPSED, collapsed);
        }
    }

    /** Klappt das Element und alle seine Vorfahren auf. */
    private void expandTo(Element element) {
        for (Element ancestor = element; ancestor != null; ancestor = ancestor.getParentElement()) {
            setCollapsed(ancestor, false);
        }
    }

    private void scrollTo(Component target) {
        // Nur scrollen, wenn die Komponente an eine UI gebunden ist (im Unit-Test gibt es keinen Client).
        target.getUI().ifPresent(ui -> target.getElement().executeJs("this.scrollIntoView({block:'center'})"));
    }

    private Div newEmptyPlaceholder() {
        Span placeholder = new Span(EMPTY_TEXT);
        placeholder.addClassName(CssClasses.EMPTY);
        Div container = new Div(placeholder);
        container.addClassName(CssClasses.TREE);
        return container;
    }

    private static RenderedTree emptyTree() {
        return new RenderedTree(
                new Div(),
                Collections.emptyMap(),
                Collections.emptyMap(),
                Collections.emptyMap(),
                Collections.emptyMap(),
                List.of());
    }

    // ------------------------------------------------------------------------
    // Paket-sichtbare Helfer (nur für Tests)
    // ------------------------------------------------------------------------

    Div headerOf(Element element) {
        return tree.elementHeaders().get(element);
    }

    boolean isExpanded(Element element) {
        Div container = tree.childContainers().get(element);
        return container == null || container.isVisible();
    }

    boolean endTagVisible(Element element) {
        Div endTag = tree.endTags().get(element);
        return endTag != null && endTag.isVisible();
    }

    List<String> searchableTexts() {
        return tree.tokens().stream().map(SearchableToken::text).toList();
    }
}
