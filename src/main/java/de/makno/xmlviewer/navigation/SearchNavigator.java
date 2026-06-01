package de.makno.xmlviewer.navigation;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import java.util.Objects;

/**
 * Eigenständige Such-Leiste: ein Eingabefeld, ein Treffer-Label („Treffer X von Y") und zwei
 * Pfeil-Icon-Buttons (zurück/vor). Die Teile sind optisch zu einer zusammenhängenden Einheit
 * („Pille") gruppiert. Die Buttons sind nur aktiv, wenn Suchtreffer vorhanden sind.
 *
 * <p>Die Komponente steuert eine beliebige {@link MatchNavigable}-Quelle (suchen + navigieren) und
 * hält ihre Anzeige über deren {@link MatchChangeEvent} automatisch synchron. Sie kennt die
 * Such-Implementierung nicht – dadurch ist sie für jede durchsuchbare Quelle wiederverwendbar.
 */
@CssImport("./styles/search-navigator.css")
public class SearchNavigator extends Composite<Div> {

    private static final long serialVersionUID = 1L;

    private static final String CSS_CLASS = "xmlviewer-search-navigator";
    private static final String FIELD_CSS_CLASS = "xmlviewer-search-navigator-field";
    private static final String LABEL_CSS_CLASS = "xmlviewer-search-navigator-label";
    private static final String FIELD_PLACEHOLDER = "Suchen…";
    private static final String LABEL_FORMAT = "Treffer %d von %d";
    private static final String LABEL_NO_MATCHES = "Keine Treffer";
    private static final String PREVIOUS_TOOLTIP = "Vorheriger Treffer";
    private static final String NEXT_TOOLTIP = "Nächster Treffer";

    private final transient MatchNavigable navigable;
    private final TextField searchField = new TextField();
    private final Span positionLabel = new Span();
    private final Button previousButton = createIconButton(VaadinIcon.CHEVRON_LEFT, PREVIOUS_TOOLTIP);
    private final Button nextButton = createIconButton(VaadinIcon.CHEVRON_RIGHT, NEXT_TOOLTIP);

    public SearchNavigator(MatchNavigable navigable) {
        this.navigable = Objects.requireNonNull(navigable, "navigable");
        getContent().addClassName(CSS_CLASS);
        getContent().add(buildLayout());

        navigable.addMatchChangeListener(event -> update(event.getMatchCount(), event.getCurrentMatchIndex()));
        update(navigable.getMatchCount(), navigable.getCurrentMatchIndex());
    }

    private HorizontalLayout buildLayout() {
        searchField.setPlaceholder(FIELD_PLACEHOLDER);
        searchField.setClearButtonVisible(true);
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addClassName(FIELD_CSS_CLASS);
        searchField.addValueChangeListener(event -> navigable.search(event.getValue()));

        previousButton.addClickListener(event -> navigable.previousMatch());
        nextButton.addClickListener(event -> navigable.nextMatch());

        positionLabel.addClassName(LABEL_CSS_CLASS);

        HorizontalLayout layout = new HorizontalLayout(searchField, previousButton, positionLabel, nextButton);
        layout.setSpacing(false);
        layout.setPadding(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        return layout;
    }

    private static Button createIconButton(VaadinIcon icon, String tooltip) {
        Button button = new Button(icon.create());
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE, ButtonVariant.LUMO_ICON);
        button.setTooltipText(tooltip);
        return button;
    }

    /** Aktualisiert Label und Button-Status für den übergebenen Treffer-Stand. */
    private void update(int matchCount, int currentMatchIndex) {
        positionLabel.setText(formatPosition(matchCount, currentMatchIndex));
        setButtonsEnabled(matchCount > 0);
    }

    private static String formatPosition(int matchCount, int currentMatchIndex) {
        if (matchCount <= 0) {
            return LABEL_NO_MATCHES;
        }
        return LABEL_FORMAT.formatted(currentMatchIndex + 1, matchCount);
    }

    private void setButtonsEnabled(boolean enabled) {
        previousButton.setEnabled(enabled);
        nextButton.setEnabled(enabled);
    }
}
