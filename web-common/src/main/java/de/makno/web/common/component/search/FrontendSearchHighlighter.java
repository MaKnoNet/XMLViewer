package de.makno.web.common.component.search;

import com.vaadin.flow.component.Component;
import elemental.json.Json;
import elemental.json.JsonArray;
import java.util.List;
import java.util.Objects;

/**
 * Standard-{@link SearchHighlightRenderer}: überträgt die Suchtreffer als Offset-Deskriptoren an das
 * Frontend-Modul {@code search-highlighter.js}, das sie via CSS Custom Highlight API zeichnet (kein
 * server-seitiger DOM-/Heap-Aufwand pro Treffer). Ohne gebundene UI (z.&nbsp;B. im Unit-Test) sind die
 * Aufrufe wirkungslose No-ops.
 *
 * <p>Wird mit der Wirts-Komponente konstruiert; deren Wurzel-Element ({@code this} im JS) dient dem
 * Highlighter als Geltungsbereich. Mehrere Komponenten je Seite werden über dieses Wurzel-Scoping
 * unterstützt.
 */
public final class FrontendSearchHighlighter implements SearchHighlightRenderer {

    private static final long serialVersionUID = 1L;

    // JS-Aufrufe an das Frontend-Highlighting-Modul; {@code this} ist das Wurzel-Element der Komponente.
    private static final String JS_APPLY = "window.SearchHighlighter.apply(this, $0, $1)";
    private static final String JS_MOVE_CURRENT = "window.SearchHighlighter.moveCurrent(this, $0)";
    private static final String JS_CLEAR = "window.SearchHighlighter.clear(this)";

    private final Component host;

    public FrontendSearchHighlighter(Component host) {
        this.host = Objects.requireNonNull(host, "host");
    }

    @Override
    public void render(List<TokenMatch> matches, int currentIndex) {
        if (host.getUI().isEmpty()) {
            return;
        }
        host.getElement().executeJs(JS_APPLY, toFlatArray(matches), currentIndex);
    }

    @Override
    public void moveCurrent(int currentIndex) {
        if (host.getUI().isEmpty()) {
            return;
        }
        host.getElement().executeJs(JS_MOVE_CURRENT, currentIndex);
    }

    @Override
    public void clear() {
        if (host.getUI().isEmpty()) {
            return;
        }
        host.getElement().executeJs(JS_CLEAR);
    }

    /** Flaches Zahlen-Array [tokenIndex, start, end, …] für eine kompakte Übertragung. */
    private JsonArray toFlatArray(List<TokenMatch> matches) {
        JsonArray array = Json.createArray();
        int position = 0;
        for (TokenMatch match : matches) {
            array.set(position++, match.tokenIndex());
            array.set(position++, match.start());
            array.set(position++, match.end());
        }
        return array;
    }
}
