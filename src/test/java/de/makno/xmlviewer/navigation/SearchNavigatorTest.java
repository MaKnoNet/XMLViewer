package de.makno.xmlviewer.navigation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.shared.Registration;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit-Tests für {@link SearchNavigator} – laufen ohne Servlet/UI gegen eine Fake-Such-Quelle.
 */
class SearchNavigatorTest {

    private FakeNavigable navigable;
    private SearchNavigator navigator;

    @BeforeEach
    void setUp() {
        navigable = new FakeNavigable();
        navigator = new SearchNavigator(navigable);
    }

    @Test
    void zeigtKeineTrefferUndDeaktiviertButtonsOhneTreffer() {
        assertEquals("Keine Treffer", label());
        assertFalse(previousButton().isEnabled());
        assertFalse(nextButton().isEnabled());
    }

    @Test
    void zeigtPositionUndAktiviertButtonsBeiTreffern() {
        navigable.fire(62, 11);
        assertEquals("Treffer 12 von 62", label());
        assertTrue(previousButton().isEnabled());
        assertTrue(nextButton().isEnabled());
    }

    @Test
    void nextButtonRuftNextMatch() {
        navigable.fire(62, 11);
        clickButton(nextButton());
        assertEquals(1, navigable.nextCalls);
        assertEquals(0, navigable.previousCalls);
    }

    @Test
    void previousButtonRuftPreviousMatch() {
        navigable.fire(62, 11);
        clickButton(previousButton());
        assertEquals(1, navigable.previousCalls);
        assertEquals(0, navigable.nextCalls);
    }

    @Test
    void wechseltZurueckZuKeineTrefferWennSucheGeleert() {
        navigable.fire(62, 11);
        navigable.fire(0, -1);
        assertEquals("Keine Treffer", label());
        assertFalse(nextButton().isEnabled());
    }

    // ---- Helfer -------------------------------------------------------------

    private String label() {
        return descendantsOf(navigator)
                .filter(Span.class::isInstance)
                .map(c -> ((Span) c).getText())
                .filter(t -> t != null && !t.isEmpty())
                .findFirst()
                .orElse("");
    }

    private Button previousButton() {
        return buttons().get(0);
    }

    private Button nextButton() {
        return buttons().get(1);
    }

    private List<Button> buttons() {
        return descendantsOf(navigator)
                .filter(Button.class::isInstance)
                .map(Button.class::cast)
                .toList();
    }

    private static void clickButton(Button button) {
        ComponentUtil.fireEvent(button, new com.vaadin.flow.component.ClickEvent<>(button));
    }

    private static java.util.stream.Stream<Component> descendantsOf(Component root) {
        return java.util.stream.Stream.concat(
                java.util.stream.Stream.of(root), root.getChildren().flatMap(SearchNavigatorTest::descendantsOf));
    }

    /** Minimale {@link MatchNavigable}-Quelle: zählt Navigationsaufrufe und kann Events feuern. */
    @com.vaadin.flow.component.Tag("div")
    private static class FakeNavigable extends Component implements MatchNavigable {

        private int matchCount = 0;
        private int currentMatchIndex = -1;
        private int nextCalls = 0;
        private int previousCalls = 0;

        @Override
        public void nextMatch() {
            nextCalls++;
        }

        @Override
        public void previousMatch() {
            previousCalls++;
        }

        @Override
        public int getMatchCount() {
            return matchCount;
        }

        @Override
        public int getCurrentMatchIndex() {
            return currentMatchIndex;
        }

        @Override
        public Registration addMatchChangeListener(ComponentEventListener<MatchChangeEvent> listener) {
            return ComponentUtil.addListener(this, MatchChangeEvent.class, listener);
        }

        void fire(int count, int index) {
            this.matchCount = count;
            this.currentMatchIndex = index;
            ComponentUtil.fireEvent(this, new MatchChangeEvent(this, count, index));
        }
    }
}
