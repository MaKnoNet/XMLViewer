package de.makno.web.common.component.search;

import com.vaadin.flow.component.Component;
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

    // Trennzeichen der Treffer-Zahlenfolge; identisch im Gegenstück search-highlighter.js.
    private static final char SEPARATOR = ',';

    // Grosszuegige Schaetzung je Treffer (3 Zahlen + 3 Trennzeichen) fuer die StringBuilder-Kapazitaet:
    // vermeidet Umkopieren des internen Arrays bei vielen Treffern.
    private static final int CHARS_PER_MATCH = 16;

    private final Component host;

    public FrontendSearchHighlighter(Component host) {
        this.host = Objects.requireNonNull(host, "host");
    }

    @Override
    public void render(List<TokenMatch> matches, int currentIndex) {
        if (host.getUI().isEmpty()) {
            return;
        }
        host.getElement().executeJs(JS_APPLY, toFlatCsv(matches), currentIndex);
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

    /**
     * Flache Zahlenfolge {@code "tokenIndex,start,end,…"} für eine kompakte Übertragung; das
     * Gegenstück {@code search-highlighter.js} zerlegt sie wieder in Zahlen.
     *
     * <p>Bewusst ein {@code String} und kein JSON-Typ: {@code String} ist der einzige
     * {@code executeJs}-Parametertyp, den alle Vaadin-Generationen unverändert unterstützen. Vaadin
     * 25 hat die früher hier genutzte Bibliothek {@code elemental.json} entfernt (Ersatz: Jackson);
     * ein {@code JsonArray} führte dort zum {@code NoClassDefFoundError} bzw. würde als Parameter
     * abgelehnt. Collections akzeptiert erst Vaadin 25, nicht 24 – siehe
     * {@code /conventions/vaadin-versionsunabhaengigkeit.md} im OKF-Bundle.
     *
     * @param matches Treffer in Dokumentreihenfolge; {@code null} ist nicht erlaubt
     * @return die Zahlenfolge, bei leerer Trefferliste der leere String – nie {@code null}
     */
    static String toFlatCsv(List<TokenMatch> matches) {
        StringBuilder csv = new StringBuilder(matches.size() * CHARS_PER_MATCH);
        for (TokenMatch match : matches) {
            if (csv.length() > 0) {
                csv.append(SEPARATOR);
            }
            csv.append(match.tokenIndex())
                    .append(SEPARATOR)
                    .append(match.start())
                    .append(SEPARATOR)
                    .append(match.end());
        }
        return csv.toString();
    }
}
