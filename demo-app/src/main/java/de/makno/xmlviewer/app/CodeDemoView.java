package de.makno.xmlviewer.app;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import de.makno.web.common.component.code.CodeLanguage;
import de.makno.web.common.component.code.CodeViewer;
import de.makno.web.common.component.navigation.SearchNavigator;

/**
 * Demo-View: zeigt Quelltext verschiedener Sprachen im {@link CodeViewer} (CodeMirror 6) mit
 * Syntax-Highlighting, Falten und Theme-Umschaltung. Die Such-UI liefert dieselbe eigenständige
 * {@link SearchNavigator}-Komponente wie bei XmlViewer/TextViewer (über {@code MatchNavigable}).
 */
@Route("code")
@PageTitle("CodeViewer – Demo")
public class CodeDemoView extends VerticalLayout {

    private static final long serialVersionUID = 1L;

    private final CodeViewer viewer;

    public CodeDemoView() {
        setSizeFull();

        viewer = new CodeViewer(SampleCodeFactory.sampleFor(CodeLanguage.JAVA), CodeLanguage.JAVA);
        viewer.setShowLineNumbers(true);
        viewer.setSizeFull();

        add(
                new H3("CodeViewer – Demo"),
                new Paragraph("Syntax-Highlighting, sprachgenaues Falten und Theme-Umschaltung über CodeMirror 6. "
                        + "Die Such-UI ist der eigenständige SearchNavigator – wie bei XmlViewer und TextViewer."),
                new RouterLink("← Zur XmlViewer-Demo", MainView.class),
                createToolbar(),
                viewer);
        setFlexGrow(1, viewer);
    }

    private HorizontalLayout createToolbar() {
        HorizontalLayout toolbar =
                new HorizontalLayout(new SearchNavigator(viewer), createLanguageSelect(), createActionBar());
        toolbar.setAlignItems(FlexComponent.Alignment.BASELINE);
        return toolbar;
    }

    /** Sprach-Auswahl: lädt das passende Beispiel und stellt die Sprache am Viewer ein. */
    private Select<CodeLanguage> createLanguageSelect() {
        Select<CodeLanguage> select = new Select<>();
        select.setLabel("Sprache");
        select.setItems(CodeLanguage.values());
        select.setValue(CodeLanguage.JAVA);
        select.addValueChangeListener(event -> {
            CodeLanguage language = event.getValue();
            // Erst die Sprache, dann den Text setzen – sonst rendert setText kurz mit der alten Sprache.
            viewer.setLanguage(language);
            viewer.setText(SampleCodeFactory.sampleFor(language));
        });
        return select;
    }

    private HorizontalLayout createActionBar() {
        Button toggleTheme = new Button("Theme hell/dunkel", event -> viewer.setDark(!viewer.isDark()));
        Button toggleWrap = new Button("Umbruch an/aus", event -> viewer.setWrap(!viewer.isWrap()));
        Button toggleNumbers =
                new Button("Zeilennummern an/aus", event -> viewer.setShowLineNumbers(!viewer.isShowLineNumbers()));
        Button foldAll = new Button("Alles falten", event -> viewer.foldAll());
        Button unfoldAll = new Button("Alles aufklappen", event -> viewer.unfoldAll());
        return new HorizontalLayout(toggleTheme, toggleWrap, toggleNumbers, foldAll, unfoldAll);
    }
}
