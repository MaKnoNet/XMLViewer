package de.makno.xmlviewer.app;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import de.makno.web.common.component.navigation.SearchNavigator;
import de.makno.web.common.component.xmlviewer.XmlViewer;
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
                new RouterLink("→ Zur TextViewer-Demo", TextDemoView.class),
                new RouterLink("→ Zur CodeViewer-Demo", CodeDemoView.class),
                createToolbar(highlightTarget, book025),
                viewer);
        setFlexGrow(1, viewer);
    }

    /** Such-Navigation und die Demo-Aktions-Buttons nebeneinander in einer Zeile. */
    private HorizontalLayout createToolbar(Element highlightTarget, Element book025) {
        HorizontalLayout toolbar =
                new HorizontalLayout(new SearchNavigator(viewer), createActionBar(highlightTarget, book025));
        toolbar.setAlignItems(FlexComponent.Alignment.CENTER);
        return toolbar;
    }

    private HorizontalLayout createActionBar(Element highlightTarget, Element book025) {
        // Beide Buttons können gleichzeitig aktiv sein – highlight() ist akkumulativ.
        Button highlight = new Button("+ Kapitel (Buch 25)", event -> viewer.highlight(highlightTarget));
        Button highlightBook025 = new Button("+ Buch b-025", event -> viewer.highlight(book025));
        Button clearOne = new Button("– Buch b-025", event -> viewer.clearHighlight(book025));
        Button clearAll = new Button("Alle Highlights löschen", event -> viewer.clearHighlight());
        Button expandAll = new Button("Alle aufklappen", event -> viewer.expandAll());
        Button collapseAll = new Button("Alle zuklappen", event -> viewer.collapseAll());
        // Wurzelwechsel: zeigt nur das Buch b-025; dabei wird eine aktive Suche zurückgesetzt.
        Element library = viewer.getRoot();
        Button rootBook = new Button("Wurzel: Buch b-025", event -> viewer.setRoot(book025));
        Button rootLibrary = new Button("Wurzel: Bibliothek", event -> viewer.setRoot(library));
        return new HorizontalLayout(
                highlight, highlightBook025, clearOne, clearAll, expandAll, collapseAll, rootBook, rootLibrary);
    }
}
