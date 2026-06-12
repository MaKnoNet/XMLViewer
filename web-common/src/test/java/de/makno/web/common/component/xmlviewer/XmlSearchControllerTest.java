package de.makno.web.common.component.xmlviewer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.vaadin.flow.component.html.Span;
import java.util.ArrayList;
import java.util.List;
import org.jdom2.Element;
import org.junit.jupiter.api.Test;

/**
 * Unit-Tests für {@link XmlSearchController}: prüfen die server-seitige Treffer-Ermittlung
 * (Offsets, Mehrfach-/Überlappungstreffer, Navigation, Owner-Aufklappen) anhand eines aufzeichnenden
 * {@link SearchHighlightRenderer} – ohne Browser. Genau diese {@link TokenMatch}-Offsets nutzt das
 * Frontend, um die Ranges zu zeichnen.
 */
class XmlSearchControllerTest {

    /** Zeichnet nicht, sondern merkt sich die übergebenen Treffer für die Prüfung. */
    private static final class RecordingRenderer implements SearchHighlightRenderer {
        private List<TokenMatch> lastMatches = List.of();
        private int lastCurrentIndex = -1;
        private int moveCalls;
        private int clearCalls;

        @Override
        public void render(List<TokenMatch> matches, int currentIndex) {
            lastMatches = matches;
            lastCurrentIndex = currentIndex;
        }

        @Override
        public void moveCurrent(int currentIndex) {
            lastCurrentIndex = currentIndex;
            moveCalls++;
        }

        @Override
        public void clear() {
            lastMatches = List.of();
            lastCurrentIndex = -1;
            clearCalls++;
        }
    }

    private final List<Element> expandedOwners = new ArrayList<>();
    private final RecordingRenderer renderer = new RecordingRenderer();

    private XmlSearchController controllerFor(String... tokenTexts) {
        List<SearchableToken> tokens = new ArrayList<>();
        Element owner = new Element("owner");
        for (String text : tokenTexts) {
            tokens.add(new SearchableToken(new Span(text), text, owner));
        }
        return new XmlSearchController(tokens, expandedOwners::add, renderer, () -> {});
    }

    @Test
    void liefertTrefferAlsTokenRangesInDokumentreihenfolge() {
        XmlSearchController controller = controllerFor("alpha beta", "gamma beta");

        controller.search("beta");

        assertEquals(2, controller.getMatchCount());
        assertEquals(0, controller.getCurrentMatchIndex());
        assertEquals(new TokenMatch(0, 6, 10), renderer.lastMatches.get(0));
        assertEquals(new TokenMatch(1, 6, 10), renderer.lastMatches.get(1));
    }

    @Test
    void mehrereVorkommenImSelbenTokenWerdenEinzelnGemeldet() {
        XmlSearchController controller = controllerFor("aXaXa");

        controller.search("X");

        assertEquals(2, controller.getMatchCount());
        assertEquals(new TokenMatch(0, 1, 2), renderer.lastMatches.get(0));
        assertEquals(new TokenMatch(0, 3, 4), renderer.lastMatches.get(1));
    }

    @Test
    void ueberlappendeBegriffeErgebenEinenZusammengefuehrtenRange() {
        XmlSearchController controller = controllerFor("findme");

        controller.search("findme find");

        assertEquals(1, controller.getMatchCount());
        assertEquals(new TokenMatch(0, 0, 6), renderer.lastMatches.get(0));
    }

    @Test
    void sucheIstStandardmaessigCaseInsensitive() {
        XmlSearchController controller = controllerFor("Beta");

        controller.search("beta");

        assertEquals(1, controller.getMatchCount());
        assertEquals(new TokenMatch(0, 0, 4), renderer.lastMatches.get(0));
    }

    @Test
    void caseInsensitiveTrefferOffsetsZeigenInDenOriginaltext() {
        // Gross-/Kleinschreibung wird ignoriert, die gemeldeten Offsets adressieren aber den
        // unveraenderten Token-Text (Treffer "BETA" an Position 2..6).
        XmlSearchController controller = controllerFor("xxBETAxx");

        controller.search("beta");

        assertEquals(1, controller.getMatchCount());
        assertEquals(new TokenMatch(0, 2, 6), renderer.lastMatches.get(0));
    }

    @Test
    void caseInsensitiveTrefferBeiLaengenaendernderLowercaseForm() {
        // 'İ' (U+0130) wird durch toLowerCase zu zwei Zeichen ("i̇"); ein zuvor lowercase
        // normalisierter Vergleich haette den Treffer verfehlt und die Offsets verschoben. Der
        // zeichenweise Abgleich findet ihn korrekt mit Offsets in den 8 Zeichen langen Originaltext.
        XmlSearchController controller = controllerFor("İstanbul");

        controller.search("istanbul");

        assertEquals(1, controller.getMatchCount());
        assertEquals(new TokenMatch(0, 0, 8), renderer.lastMatches.get(0));
    }

    @Test
    void navigationAktualisiertNurDenAktuellenIndex() {
        XmlSearchController controller = controllerFor("beta beta beta");

        controller.search("beta"); // drei Treffer im selben Token
        assertEquals(0, controller.getCurrentMatchIndex());

        controller.nextMatch();
        assertEquals(1, controller.getCurrentMatchIndex());
        assertEquals(1, renderer.lastCurrentIndex);

        controller.previousMatch();
        controller.previousMatch(); // umlaufend
        assertEquals(2, controller.getCurrentMatchIndex());
        assertTrue(renderer.moveCalls >= 3);
    }

    @Test
    void clearSearchLeertTrefferUndRuftRendererClear() {
        XmlSearchController controller = controllerFor("beta");
        controller.search("beta");

        controller.clearSearch();

        assertEquals(0, controller.getMatchCount());
        assertEquals(-1, controller.getCurrentMatchIndex());
        assertTrue(renderer.clearCalls >= 1);
    }

    @Test
    void klapptElementeMitTreffernAuf() {
        // Beide Tokens gehören demselben Element -> es wird genau einmal aufgeklappt.
        XmlSearchController controller = controllerFor("beta", "beta");

        controller.search("beta");

        assertEquals(1, expandedOwners.size());
    }
}
