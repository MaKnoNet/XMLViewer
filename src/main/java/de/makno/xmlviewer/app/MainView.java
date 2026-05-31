package de.makno.xmlviewer.app;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.makno.xmlviewer.component.XmlViewer;
import org.jdom2.Element;

/**
 * Demo-View: zeigt einen großen Beispiel-XML-Baum im {@link XmlViewer} und stellt die Such-UI
 * (Suchfeld, Vor-/Zurück-Buttons, Treffer-Zähler) selbst bereit – die Komponente liefert nur die
 * programmatische Such-API. Demonstriert außerdem Hervorheben sowie Auf-/Zuklappen.
 */
@Route("")
@PageTitle("XmlViewer – Demo")
public class MainView extends VerticalLayout {

    private static final long serialVersionUID = 1L;

    private final XmlViewer viewer;
    private final Span matchCounter = new Span();

    public MainView() {
        setSizeFull();

        Element library = SampleXmlFactory.createLibrary();
        Element highlightTarget = SampleXmlFactory.findHighlightTarget(library);

        viewer = new XmlViewer(library);
        viewer.setSizeFull();
        viewer.addMatchChangeListener(event -> updateCounter(event.getMatchCount(), event.getCurrentMatchIndex()));

        add(
                new H3("XmlViewer – Demo"),
                new Paragraph("Such-UI und Buttons liegen in der Demo-App; die Komponente bietet nur die API. "
                        + "Großer Baum zum Testen von vertikalem und horizontalem Scrollen."),
                createSearchBar(),
                createActionBar(highlightTarget),
                viewer);
        setFlexGrow(1, viewer);
    }

    private HorizontalLayout createSearchBar() {
        TextField searchField = new TextField();
        searchField.setPlaceholder("Suchen…");
        searchField.setClearButtonVisible(true);
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(event -> viewer.search(event.getValue()));

        Button previous = new Button("‹", event -> viewer.previousMatch());
        Button next = new Button("›", event -> viewer.nextMatch());

        updateCounter(0, -1);

        HorizontalLayout bar = new HorizontalLayout(searchField, previous, next, matchCounter);
        bar.setAlignItems(FlexComponent.Alignment.BASELINE);
        return bar;
    }

    private HorizontalLayout createActionBar(Element highlightTarget) {
        Button highlight = new Button("Kapitel (Buch 25) hervorheben", event -> viewer.highlight(highlightTarget));
        Button clearHighlight = new Button("Highlight löschen", event -> viewer.clearHighlight());
        Button expandAll = new Button("Alle aufklappen", event -> viewer.expandAll());
        Button collapseAll = new Button("Alle zuklappen", event -> viewer.collapseAll());
        return new HorizontalLayout(highlight, clearHighlight, expandAll, collapseAll);
    }

    private void updateCounter(int matchCount, int currentMatchIndex) {
        matchCounter.setText(matchCount == 0 ? "0/0" : (currentMatchIndex + 1) + "/" + matchCount);
    }
}
