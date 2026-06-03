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
 * Eigenständige Such-Leiste: ein Eingabefeld, ein Treffer-Label (Standard „12/66") und zwei
 * Pfeil-Icon-Buttons (zurück/vor). Die Teile sind optisch zu einer zusammenhängenden Einheit
 * („Pille") gruppiert. Die Buttons sind nur aktiv, wenn Suchtreffer vorhanden sind.
 *
 * <p>Das Label-Format ist über {@link #setLabelFormatter(MatchLabelFormatter)} frei bestimmbar.
 *
 * <p>Die Komponente steuert eine beliebige {@link MatchNavigable}-Quelle (suchen + navigieren) und
 * hält ihre Anzeige über deren {@link MatchChangeEvent} automatisch synchron. Sie kennt die
 * Such-Implementierung nicht – dadurch ist sie für jede durchsuchbare Quelle wiederverwendbar.
 *
 * <p><strong>Nicht thread-safe</strong> – wie jede Vaadin-Komponente an genau eine {@code UI}/Session
 * gebunden; Methoden nur aus dem Session-Thread (mit Session-Lock) aufrufen, eine Instanz pro UI.
 */
@CssImport("./styles/search-navigator.css")
public class SearchNavigator extends Composite<Div> {

    private static final long serialVersionUID = 1L;

    private static final String CSS_CLASS = "xmlviewer-search-navigator";
    private static final String FIELD_CSS_CLASS = "xmlviewer-search-navigator-field";
    private static final String LABEL_CSS_CLASS = "xmlviewer-search-navigator-label";
    private static final String FIELD_PLACEHOLDER = "Suchen…";
    private static final String PREVIOUS_TOOLTIP = "Vorheriger Treffer";
    private static final String NEXT_TOOLTIP = "Nächster Treffer";

    /** Standardformat „aktuell/gesamt", z.&nbsp;B. „12/66" (bzw. „0/0" ohne Treffer). */
    private static final MatchLabelFormatter DEFAULT_LABEL_FORMATTER =
            (matchCount, currentPosition) -> currentPosition + "/" + matchCount;

    private final transient MatchNavigable navigable;
    private final TextField searchField = new TextField();
    private final Span positionLabel = new Span();
    private final Button previousButton = createIconButton(VaadinIcon.CHEVRON_LEFT, PREVIOUS_TOOLTIP);
    private final Button nextButton = createIconButton(VaadinIcon.CHEVRON_RIGHT, NEXT_TOOLTIP);

    private MatchLabelFormatter labelFormatter = DEFAULT_LABEL_FORMATTER;

    public SearchNavigator(MatchNavigable navigable) {
        this.navigable = Objects.requireNonNull(navigable, "navigable");
        getContent().addClassName(CSS_CLASS);
        getContent().add(buildLayout());

        navigable.addMatchChangeListener(event -> update(event.getMatchCount(), event.getCurrentMatchIndex()));
        update(navigable.getMatchCount(), navigable.getCurrentMatchIndex());
    }

    /**
     * Setzt das Format des Treffer-Labels (z.&nbsp;B. {@code (count, pos) -> "Treffer " + pos + " von "
     * + count}). Aktualisiert die Anzeige sofort. Standard ist „aktuell/gesamt" (z.&nbsp;B. „12/66").
     */
    public void setLabelFormatter(MatchLabelFormatter labelFormatter) {
        this.labelFormatter = Objects.requireNonNull(labelFormatter, "labelFormatter");
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
        int currentPosition = matchCount > 0 ? currentMatchIndex + 1 : 0;
        positionLabel.setText(labelFormatter.format(matchCount, currentPosition));
        setButtonsEnabled(matchCount > 0);
    }

    private void setButtonsEnabled(boolean enabled) {
        previousButton.setEnabled(enabled);
        nextButton.setEnabled(enabled);
    }
}
