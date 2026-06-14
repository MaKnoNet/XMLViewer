package de.makno.web.common.component.text;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import de.makno.web.common.component.navigation.SearchNavigator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit-Tests für {@link TextViewer} – laufen ohne Servlet/UI (reine Server-seitige Komponenten).
 */
class TextViewerTest {

    private TextViewer viewer;

    @BeforeEach
    void setUp() {
        // Zeile 0: "alpha beta" – Zeile 1: "gamma beta" – Zeile 2: "findme here findme"
        viewer = new TextViewer("alpha beta\ngamma beta\nfindme here findme");
    }

    @Test
    void rendertEineZeileProTextzeile() {
        assertEquals(3, viewer.lineCount());
        assertEquals(List.of("alpha beta", "gamma beta", "findme here findme"), viewer.searchableTexts());
    }

    @Test
    void behaeltLeerzeilenUndAbschliessendeLeerzeile() {
        TextViewer v = new TextViewer("a\n\nb\n");
        assertEquals(4, v.lineCount());
        assertEquals(List.of("a", "", "b", ""), v.searchableTexts());
    }

    @Test
    void leererTextZeigtPlatzhalterOhneFehler() {
        TextViewer v = new TextViewer();
        assertEquals(0, v.lineCount());
        assertEquals(List.of(), v.searchableTexts());
        assertEquals(0, v.getMatchCount());
    }

    @Test
    void haeltTextRohVorUndUeberlaesstEscapingDemFramework() {
        TextViewer v = new TextViewer("Hello & <world>");
        // Der Rohtext (inkl. < & >) wird per setText gespeichert; Vaadin escaped erst beim Rendern.
        assertTrue(v.searchableTexts().contains("Hello & <world>"));
    }

    @Test
    void sucheFindetUndZaehltTreffer() {
        viewer.search("findme"); // zwei Vorkommen in Zeile 2
        assertEquals(2, viewer.getMatchCount());
        assertEquals(0, viewer.getCurrentMatchIndex());

        viewer.nextMatch();
        assertEquals(1, viewer.getCurrentMatchIndex());

        viewer.nextMatch(); // umlaufend
        assertEquals(0, viewer.getCurrentMatchIndex());

        viewer.previousMatch();
        assertEquals(1, viewer.getCurrentMatchIndex());
    }

    @Test
    void sucheFindetTrefferUeberMehrereZeilen() {
        viewer.search("beta"); // je ein Treffer in Zeile 0 und Zeile 1
        assertEquals(2, viewer.getMatchCount());
    }

    @Test
    void sucheFindetMehrereBegriffeGetrenntDurchLeerzeichen() {
        viewer.search("alpha findme"); // alpha (1x) + findme (2x) = 3
        assertEquals(3, viewer.getMatchCount());
    }

    @Test
    void sucheIstStandardmaessigCaseInsensitive() {
        viewer.search("BETA");
        assertEquals(2, viewer.getMatchCount());
    }

    @Test
    void caseSensitiveSucheUnterscheidetGrossKlein() {
        viewer.setSearchCaseSensitive(true);
        viewer.search("BETA");
        assertEquals(0, viewer.getMatchCount());

        viewer.search("beta");
        assertEquals(2, viewer.getMatchCount());
    }

    @Test
    void eigenerSplitterBestimmtDasTrennzeichen() {
        viewer.setSearchTermSplitter(query -> java.util.Arrays.stream(query.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList());
        viewer.search("alpha, findme"); // alpha (1x) + findme (2x) = 3
        assertEquals(3, viewer.getMatchCount());
    }

    @Test
    void leereSucheLiefertKeineTreffer() {
        viewer.search("");
        assertEquals(0, viewer.getMatchCount());
        assertEquals(-1, viewer.getCurrentMatchIndex());
    }

    @Test
    void zeigtZeilennummernWennAktiviert() {
        assertFalse(hasGutter(0), "ohne setShowLineNumbers keine Nummern-Spalte");

        viewer.setShowLineNumbers(true);

        assertTrue(hasGutter(0));
        assertEquals("1", gutterText(0));
        assertEquals("3", gutterText(2));
    }

    @Test
    void highlightMarkiertZeileUndClearEntfernt() {
        viewer.highlight(1);
        assertTrue(viewer.isLineHighlighted(1));

        viewer.clearHighlight();
        assertFalse(viewer.isLineHighlighted(1));
    }

    @Test
    void highlightUngueltigerIndexWirdIgnoriert() {
        viewer.highlight(-1);
        viewer.highlight(99);
        // kein Fehler; nichts ist markiert
        for (int line = 0; line < viewer.lineCount(); line++) {
            assertFalse(viewer.isLineHighlighted(line));
        }
    }

    @Test
    void neuerTextFeuertResetEvent() {
        List<String> events = new ArrayList<>();
        viewer.addMatchChangeListener(
                e -> events.add(e.getMatchCount() + "/" + e.getCurrentMatchIndex() + "/" + e.isReset()));

        viewer.setText("ganz neuer Text");

        assertTrue(events.contains("0/-1/true"), "Reset-Event beim Textwechsel fehlt: " + events);
    }

    @Test
    void matchChangeListenerWirdMitKorrektenWertenGefeuert() {
        List<String> events = new ArrayList<>();
        viewer.addMatchChangeListener(e -> events.add(e.getMatchCount() + "/" + e.getCurrentMatchIndex()));

        viewer.search("findme"); // 2 Treffer, springt auf den ersten (Index 0)
        viewer.nextMatch(); // Index 1
        viewer.clearSearch(); // keine Treffer mehr

        assertEquals(List.of("2/0", "2/1", "0/-1"), events);
    }

    @Test
    void istVollstaendigSerialisierbarUndBleibtFunktionsfaehig() throws Exception {
        TextViewer restored = deserialize(serialize(viewer));

        restored.search("findme");
        assertEquals(2, restored.getMatchCount(), "Suche muss nach Deserialisierung weiter funktionieren");
        assertEquals(3, restored.lineCount(), "Zeilen müssen nach Deserialisierung vorhanden sein");
    }

    @Test
    void arbeitetMitSearchNavigatorZusammen() {
        SearchNavigator navigator = new SearchNavigator(viewer);

        viewer.search("beta"); // 2 Treffer, aktueller Treffer 1/2
        assertTrue(labels(navigator).contains("1/2"), "Navigator soll den Treffer-Stand des TextViewer zeigen");

        viewer.nextMatch();
        assertTrue(labels(navigator).contains("2/2"));
    }

    // ---- Helfer -------------------------------------------------------------

    private boolean hasGutter(int line) {
        return viewer.lineOf(line)
                .getChildren()
                .anyMatch(c -> c.getElement().getClassList().contains(TextCssClasses.GUTTER));
    }

    private String gutterText(int line) {
        return viewer.lineOf(line)
                .getChildren()
                .filter(c -> c.getElement().getClassList().contains(TextCssClasses.GUTTER))
                .map(c -> c.getElement().getText())
                .findFirst()
                .orElse(null);
    }

    private static List<String> labels(Component root) {
        return descendants(root)
                .filter(Span.class::isInstance)
                .map(c -> ((Span) c).getText())
                .filter(t -> t != null && !t.isEmpty())
                .toList();
    }

    private static Stream<Component> descendants(Component root) {
        return Stream.concat(Stream.of(root), root.getChildren().flatMap(TextViewerTest::descendants));
    }

    private static byte[] serialize(Object object) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(object);
        }
        return bos.toByteArray();
    }

    private static TextViewer deserialize(byte[] bytes) throws Exception {
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            return (TextViewer) ois.readObject();
        }
    }
}
