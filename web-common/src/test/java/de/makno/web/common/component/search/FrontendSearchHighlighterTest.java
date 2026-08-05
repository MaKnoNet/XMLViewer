package de.makno.web.common.component.search;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.vaadin.flow.component.html.Div;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Unit-Tests für {@link FrontendSearchHighlighter}: prüfen das an das Frontend übertragene Format
 * der Treffer-Offsets sowie die No-op-Zusicherung ohne gebundene UI – ohne Browser.
 *
 * <p>Das Format ist bewusst eine Zeichenkette und kein JSON-Typ: {@code String} ist der einzige
 * {@code executeJs}-Parametertyp, den alle Vaadin-Generationen unverändert unterstützen. Diese Tests
 * halten den Vertrag zum Gegenstück {@code search-highlighter.js} fest.
 */
class FrontendSearchHighlighterTest {

    @Test
    void leereTrefferlisteErgibtLeereZeichenkette() {
        assertEquals("", FrontendSearchHighlighter.toFlatCsv(List.of()));
    }

    @Test
    void einTrefferWirdAlsTokenIndexStartEndeUebertragen() {
        List<TokenMatch> matches = List.of(new TokenMatch(0, 6, 10));

        assertEquals("0,6,10", FrontendSearchHighlighter.toFlatCsv(matches));
    }

    @Test
    void mehrereTrefferBleibenInDokumentreihenfolgeAneinandergereiht() {
        List<TokenMatch> matches =
                List.of(new TokenMatch(0, 6, 10), new TokenMatch(1, 6, 10), new TokenMatch(1, 12, 16));

        assertEquals("0,6,10,1,6,10,1,12,16", FrontendSearchHighlighter.toFlatCsv(matches));
    }

    @Test
    void nullTrefferlisteWirftNullPointerException() {
        assertThrows(NullPointerException.class, () -> FrontendSearchHighlighter.toFlatCsv(null));
    }

    @Test
    void ohneGebundeneUiSindAlleAufrufeWirkungsloseNoOps() {
        // Ein nicht attachter Host hat keine UI; die Guards in render/moveCurrent/clear muessen den
        // executeJs-Aufruf verhindern, statt an einem fehlenden Element zu scheitern.
        FrontendSearchHighlighter highlighter = new FrontendSearchHighlighter(new Div());

        assertDoesNotThrow(() -> {
            highlighter.render(List.of(new TokenMatch(0, 6, 10)), 0);
            highlighter.moveCurrent(0);
            highlighter.clear();
        });
    }

    @Test
    void konstruktorOhneHostWirftNullPointerException() {
        assertThrows(NullPointerException.class, () -> new FrontendSearchHighlighter(null));
    }
}
