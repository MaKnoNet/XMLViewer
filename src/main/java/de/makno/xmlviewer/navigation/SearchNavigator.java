package de.makno.xmlviewer.navigation;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import java.util.Objects;

/**
 * Eigenständige UI-Leiste zum Durchlaufen von Suchtreffern: ein ausgeschriebenes Treffer-Label
 * („Treffer X von Y") zwischen zwei Pfeil-Buttons für zurück/vor.
 *
 * <p>Die Komponente steuert eine beliebige {@link MatchNavigable}-Quelle und hält ihre Anzeige über
 * deren {@link MatchChangeEvent} automatisch synchron. Sie kennt die Such-Implementierung nicht –
 * dadurch ist sie für jede Treffer-Navigation wiederverwendbar.
 */
public class SearchNavigator extends Composite<Div> {

    private static final long serialVersionUID = 1L;

    private static final String CSS_CLASS = "xmlviewer-search-navigator";
    private static final String LABEL_FORMAT = "Treffer %d von %d";
    private static final String LABEL_NO_MATCHES = "Keine Treffer";
    private static final String PREVIOUS_SYMBOL = "‹";
    private static final String NEXT_SYMBOL = "›";

    private final transient MatchNavigable navigable;
    private final Span positionLabel = new Span();
    private final Button previousButton = new Button(PREVIOUS_SYMBOL);
    private final Button nextButton = new Button(NEXT_SYMBOL);

    public SearchNavigator(MatchNavigable navigable) {
        this.navigable = Objects.requireNonNull(navigable, "navigable");
        getContent().addClassName(CSS_CLASS);
        getContent().add(buildLayout());

        navigable.addMatchChangeListener(event -> update(event.getMatchCount(), event.getCurrentMatchIndex()));
        update(navigable.getMatchCount(), navigable.getCurrentMatchIndex());
    }

    private HorizontalLayout buildLayout() {
        previousButton.addClickListener(event -> navigable.previousMatch());
        nextButton.addClickListener(event -> navigable.nextMatch());

        HorizontalLayout layout = new HorizontalLayout(previousButton, positionLabel, nextButton);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        return layout;
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
