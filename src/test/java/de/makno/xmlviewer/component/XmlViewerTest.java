package de.makno.xmlviewer.component;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.vaadin.flow.component.Component;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jdom2.Element;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit-Tests für {@link XmlViewer} – laufen ohne Servlet/UI (reine Server-seitige Komponenten).
 */
class XmlViewerTest {

    private XmlViewer viewer;
    private Element root;
    private Element child;
    private Element item;

    @BeforeEach
    void setUp() {
        root = new Element("root");
        root.setAttribute("attr", "value");

        child = new Element("child");
        child.setText("Hello & <world>"); // Sonderzeichen für den Escaping-Test
        root.addContent(child);

        item = new Element("item");
        item.setText("findme here findme");
        root.addContent(item);

        viewer = new XmlViewer(root);
    }

    @Test
    void rendertKnotenFuerJedesElement() {
        assertNotNull(viewer.headerOf(root), "Wurzel sollte eine Start-Tag-Zeile haben");
        assertNotNull(viewer.headerOf(child));
        assertNotNull(viewer.headerOf(item));
    }

    @Test
    void setztFarbklassen() {
        Set<String> classes = new HashSet<>();
        for (Component c : descendants(viewer.headerOf(root))) {
            classes.addAll(c.getElement().getClassList());
        }
        for (Component c : descendants(viewer.headerOf(child))) {
            classes.addAll(c.getElement().getClassList());
        }
        assertTrue(classes.contains(CssClasses.TAG), "Tag-Klasse fehlt");
        assertTrue(classes.contains(CssClasses.ATTR_NAME), "Attributnamen-Klasse fehlt");
        assertTrue(classes.contains(CssClasses.ATTR_VALUE), "Attributwert-Klasse fehlt");
        assertTrue(classes.contains(CssClasses.TEXT), "Text-Klasse fehlt");
        assertTrue(classes.contains(CssClasses.PUNCT), "Punctuation-Klasse fehlt");
    }

    @Test
    void haeltTextRohVorUndUeberlaesstEscapingDemFramework() {
        // Der Rohtext (inkl. < & >) wird per setText gespeichert; Vaadin escaped erst beim Rendern.
        // Kein manuelles Markup -> kein XSS / kein zerstörtes Layout.
        assertTrue(viewer.searchableTexts().contains("Hello & <world>"));
        assertTrue(viewer.searchableTexts().contains("value"));
    }

    @Test
    void highlightMehrerElementeGleichzeitig() {
        // Beide Elemente können gleichzeitig hervorgehoben sein.
        viewer.highlight(child);
        viewer.highlight(item);
        assertTrue(viewer.headerOf(child).getElement().getClassList().contains(CssClasses.HIGHLIGHT));
        assertTrue(viewer.headerOf(item).getElement().getClassList().contains(CssClasses.HIGHLIGHT));
    }

    @Test
    void clearHighlightEinzelnEntferntNurDasEineElement() {
        viewer.highlight(child);
        viewer.highlight(item);

        viewer.clearHighlight(child);

        assertFalse(viewer.headerOf(child).getElement().getClassList().contains(CssClasses.HIGHLIGHT));
        assertTrue(viewer.headerOf(item).getElement().getClassList().contains(CssClasses.HIGHLIGHT));
    }

    @Test
    void clearHighlightAlleEntferntAlleHervorhebungen() {
        viewer.highlight(child);
        viewer.highlight(item);

        viewer.clearHighlight();

        assertFalse(viewer.headerOf(child).getElement().getClassList().contains(CssClasses.HIGHLIGHT));
        assertFalse(viewer.headerOf(item).getElement().getClassList().contains(CssClasses.HIGHLIGHT));
    }

    @Test
    void sucheFindetUndZaehltTreffer() {
        viewer.search("findme");
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
    void sucheIstStandardmaessigCaseInsensitive() {
        viewer.search("FINDME");
        assertEquals(2, viewer.getMatchCount());
    }

    @Test
    void caseSensitiveSucheUnterscheidetGrossKlein() {
        viewer.setSearchCaseSensitive(true);
        viewer.search("FINDME");
        assertEquals(0, viewer.getMatchCount());

        viewer.search("findme");
        assertEquals(2, viewer.getMatchCount());
    }

    @Test
    void clearSearchEntferntMarkierungen() {
        viewer.search("findme");
        assertEquals(2, viewer.getMatchCount());

        viewer.clearSearch();
        assertEquals(0, viewer.getMatchCount());
        assertEquals(-1, viewer.getCurrentMatchIndex());
    }

    @Test
    void leereSucheLiefertKeineTreffer() {
        viewer.search("");
        assertEquals(0, viewer.getMatchCount());
        assertEquals(-1, viewer.getCurrentMatchIndex());
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
    void collapseUndExpandSchaltenSichtbarkeit() {
        assertTrue(viewer.isExpanded(root));
        viewer.collapseAll();
        assertFalse(viewer.isExpanded(root));
        viewer.expandAll();
        assertTrue(viewer.isExpanded(root));
    }

    @Test
    void zuklappenBlendetSchliessendenTagAus() {
        assertTrue(viewer.endTagVisible(root), "End-Tag sollte initial sichtbar sein");

        viewer.collapseAll();
        assertFalse(viewer.endTagVisible(root), "End-Tag soll beim Zuklappen ausgeblendet werden");

        viewer.expandAll();
        assertTrue(viewer.endTagVisible(root), "End-Tag soll beim Aufklappen wieder sichtbar sein");
    }

    @Test
    void nullWurzelLeertOhneFehler() {
        viewer.setRoot(null);
        assertEquals(0, viewer.getMatchCount());
        // headerOf liefert für unbekannte Elemente null
        assertNull(viewer.headerOf(root));
    }

    /** Sammelt eine Komponente und alle ihre Nachfahren. */
    private static List<Component> descendants(Component c) {
        List<Component> result = new ArrayList<>();
        result.add(c);
        c.getChildren().forEach(child -> result.addAll(descendants(child)));
        return result;
    }
}
