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
import de.makno.web.common.component.text.TextViewer;

/**
 * Demo-View: zeigt einen großen Beispiel-Klartext im {@link TextViewer}. Die komplette Such-UI
 * (Eingabefeld, Treffer-Zähler, Vor-/Zurück) liefert dieselbe eigenständige {@link SearchNavigator}
 * -Komponente wie bei der XmlViewer-Demo. Demonstriert außerdem Zeilennummern, Umbruch-Umschaltung
 * und programmatisches Zeilen-Highlight.
 */
@Route("text")
@PageTitle("TextViewer – Demo")
public class TextDemoView extends VerticalLayout {

    private static final long serialVersionUID = 1L;

    private final TextViewer viewer;

    public TextDemoView() {
        setSizeFull();

        viewer = new TextViewer(SampleTextFactory.createSampleText());
        viewer.setShowLineNumbers(true);
        viewer.setSizeFull();

        add(
                new H3("TextViewer – Demo"),
                new Paragraph("Such-UI und Buttons liegen in der Demo-App; die Komponente bietet nur die API. "
                        + "Großer Klartext zum Testen von vertikalem und horizontalem Scrollen."),
                new RouterLink("← Zur XmlViewer-Demo", MainView.class),
                createToolbar(),
                viewer);
        setFlexGrow(1, viewer);
    }

    /** Such-Navigation und die Demo-Aktions-Buttons nebeneinander in einer Zeile. */
    private HorizontalLayout createToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout(new SearchNavigator(viewer), createActionBar());
        toolbar.setAlignItems(FlexComponent.Alignment.CENTER);
        return toolbar;
    }

    private HorizontalLayout createActionBar() {
        Button toggleWrap = new Button("Umbruch an/aus", event -> viewer.setWrap(!viewer.isWrap()));
        Button toggleNumbers =
                new Button("Zeilennummern an/aus", event -> viewer.setShowLineNumbers(!viewer.isShowLineNumbers()));
        Button highlightLine = new Button("Zeile 5 markieren", event -> viewer.highlight(4));
        Button clearHighlights = new Button("Markierungen löschen", event -> viewer.clearHighlight());
        Button shortText =
                new Button("Kurzen Text setzen", event -> viewer.setText(SampleTextFactory.createShortText()));
        Button longText = new Button("Demo-Text setzen", event -> {
            viewer.setText(SampleTextFactory.createSampleText());
            viewer.setShowLineNumbers(true);
        });
        return new HorizontalLayout(toggleWrap, toggleNumbers, highlightLine, clearHighlights, shortText, longText);
    }
}
