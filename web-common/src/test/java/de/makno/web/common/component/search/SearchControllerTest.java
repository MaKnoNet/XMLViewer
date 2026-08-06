package de.makno.web.common.component.search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.vaadin.flow.function.SerializableRunnable;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Unit-Tests für {@link SearchController}: prüfen die server-seitige Treffer-Ermittlung (Offsets,
 * Mehrfach-/Überlappungstreffer, Navigation, Reveal) anhand eines aufzeichnenden
 * {@link SearchHighlightRenderer} – ohne Browser. Genau diese {@link TokenMatch}-Offsets nutzt das
 * Frontend, um die Ranges zu zeichnen.
 */
class SearchControllerTest {

    /** Zeichnet nicht, sondern merkt sich die übergebenen Treffer für die Prüfung. */
    private static final class RecordingRenderer implements SearchHighlightRenderer {

        private static final long serialVersionUID = 1L;

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

    private final RecordingRenderer renderer = new RecordingRenderer();

    private SearchController controllerFor(String... tokenTexts) {
        List<SearchToken> tokens = new ArrayList<>();
        for (String text : tokenTexts) {
            tokens.add(SearchToken.of(text));
        }
        return new SearchController(tokens, renderer, () -> {});
    }

    @Test
    void liefertTrefferAlsTokenRangesInDokumentreihenfolge() {
        SearchController controller = controllerFor("alpha beta", "gamma beta");

        controller.search("beta");

        assertEquals(2, controller.getMatchCount());
        assertEquals(0, controller.getCurrentMatchIndex());
        assertEquals(new TokenMatch(0, 6, 10), renderer.lastMatches.get(0));
        assertEquals(new TokenMatch(1, 6, 10), renderer.lastMatches.get(1));
    }

    @Test
    void mehrereVorkommenImSelbenTokenWerdenEinzelnGemeldet() {
        SearchController controller = controllerFor("aXaXa");

        controller.search("X");

        assertEquals(2, controller.getMatchCount());
        assertEquals(new TokenMatch(0, 1, 2), renderer.lastMatches.get(0));
        assertEquals(new TokenMatch(0, 3, 4), renderer.lastMatches.get(1));
    }

    @Test
    void ueberlappendeBegriffeErgebenEinenZusammengefuehrtenRange() {
        SearchController controller = controllerFor("findme");

        controller.search("findme find");

        assertEquals(1, controller.getMatchCount());
        assertEquals(new TokenMatch(0, 0, 6), renderer.lastMatches.get(0));
    }

    @Test
    void sucheIstStandardmaessigCaseInsensitive() {
        SearchController controller = controllerFor("Beta");

        controller.search("beta");

        assertEquals(1, controller.getMatchCount());
        assertEquals(new TokenMatch(0, 0, 4), renderer.lastMatches.get(0));
    }

    @Test
    void caseInsensitiveTrefferOffsetsZeigenInDenOriginaltext() {
        // Gross-/Kleinschreibung wird ignoriert, die gemeldeten Offsets adressieren aber den
        // unveraenderten Token-Text (Treffer "BETA" an Position 2..6).
        SearchController controller = controllerFor("xxBETAxx");

        controller.search("beta");

        assertEquals(1, controller.getMatchCount());
        assertEquals(new TokenMatch(0, 2, 6), renderer.lastMatches.get(0));
    }

    @Test
    void caseInsensitiveTrefferBeiLaengenaendernderLowercaseForm() {
        // 'İ' (U+0130) wird durch toLowerCase zu zwei Zeichen ("i̇"); ein zuvor lowercase
        // normalisierter Vergleich haette den Treffer verfehlt und die Offsets verschoben. Der
        // zeichenweise Abgleich findet ihn korrekt mit Offsets in den 8 Zeichen langen Originaltext.
        SearchController controller = controllerFor("İstanbul");

        controller.search("istanbul");

        assertEquals(1, controller.getMatchCount());
        assertEquals(new TokenMatch(0, 0, 8), renderer.lastMatches.get(0));
    }

    @Test
    void navigationAktualisiertNurDenAktuellenIndex() {
        SearchController controller = controllerFor("beta beta beta");

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
    void machtTrefferTokensEinmalProRevealSichtbar() {
        // Zwei Tokens teilen sich DIESELBE Reveal-Aktion (z. B. zwei Tokens desselben XML-Elements):
        // bei einer Suche mit Treffern in beiden Tokens darf die Aktion nur EINMAL laufen (Dedup).
        int[] reveals = {0};
        SerializableRunnable shared = () -> reveals[0]++;
        List<SearchToken> tokens = List.of(new SearchToken("beta", shared), new SearchToken("beta", shared));
        SearchController controller = new SearchController(tokens, renderer, () -> {});

        controller.search("beta");

        assertEquals(1, reveals[0], "Geteilte Reveal-Aktion soll nur einmal je Suche laufen");
    }

    @Test
    void navigationMachtDenAktuellenTrefferWiederSichtbar() {
        // Zwei Treffer in Tokens mit unterschiedlichen Reveal-Aktionen. Nach der Suche „vergessen" wir
        // die bisherigen Reveals; die Navigation muss die Aktion des neuen aktuellen Treffers ausfuehren.
        List<String> revealed = new ArrayList<>();
        List<SearchToken> tokens = List.of(
                new SearchToken("beta", () -> revealed.add("a")), new SearchToken("beta", () -> revealed.add("b")));
        SearchController controller = new SearchController(tokens, renderer, () -> {});

        controller.search("beta"); // Treffer 0 -> "a", Treffer 1 -> "b"
        revealed.clear();

        controller.nextMatch(); // aktueller Treffer 1 -> "b"

        assertTrue(revealed.contains("b"), "Reveal-Aktion des aktuellen Treffers soll erneut laufen");
    }

    @Test
    void clearSearchLeertTrefferUndRuftRendererClear() {
        SearchController controller = controllerFor("beta");
        controller.search("beta");

        controller.clearSearch();

        assertEquals(0, controller.getMatchCount());
        assertEquals(-1, controller.getCurrentMatchIndex());
        assertTrue(renderer.clearCalls >= 1);
    }
}
