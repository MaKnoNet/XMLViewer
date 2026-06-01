package de.makno.xmlviewer.app;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.makno.xmlviewer.component.XmlViewer;
import de.makno.xmlviewer.navigation.SearchNavigator;
import org.jdom2.Element;

/**
 * Demo-View: zeigt einen großen Beispiel-XML-Baum im {@link XmlViewer}. Die komplette Such-UI
 * (Eingabefeld, Treffer-Zähler, Vor-/Zurück) liefert die eigenständige {@link SearchNavigator}
 * -Komponente. Demonstriert außerdem Hervorheben sowie Auf-/Zuklappen.
 */
@Route("")
@PageTitle("XmlViewer – Demo")
public class MainView extends VerticalLayout {

    private static final long serialVersionUID = 1L;

    private final XmlViewer viewer;

    public MainView() {
        setSizeFull();

        Element library = SampleXmlFactory.createLibrary();
        Element highlightTarget = SampleXmlFactory.findHighlightTarget(library);
        Element book025 = SampleXmlFactory.findBookById(library, "b-025");

        viewer = new XmlViewer(library);
        viewer.setSizeFull();

        add(
                new H3("XmlViewer – Demo"),
                new Paragraph("Such-UI und Buttons liegen in der Demo-App; die Komponente bietet nur die API. "
                        + "Großer Baum zum Testen von vertikalem und horizontalem Scrollen."),
                new SearchNavigator(viewer),
                createActionBar(highlightTarget, book025),
                viewer);
        setFlexGrow(1, viewer);
    }

    private HorizontalLayout createActionBar(Element highlightTarget, Element book025) {
        // Beide Buttons können gleichzeitig aktiv sein – highlight() ist akkumulativ.
        Button highlight = new Button("+ Kapitel (Buch 25)", event -> viewer.highlight(highlightTarget));
        Button highlightBook025 = new Button("+ Buch b-025", event -> viewer.highlight(book025));
        Button clearOne = new Button("– Buch b-025", event -> viewer.clearHighlight(book025));
        Button clearAll = new Button("Alle Highlights löschen", event -> viewer.clearHighlight());
        Button expandAll = new Button("Alle aufklappen", event -> viewer.expandAll());
        Button collapseAll = new Button("Alle zuklappen", event -> viewer.collapseAll());
        return new HorizontalLayout(highlight, highlightBook025, clearOne, clearAll, expandAll, collapseAll);
    }
}
