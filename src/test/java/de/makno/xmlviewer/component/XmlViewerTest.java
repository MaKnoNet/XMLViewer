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
    void sucheFindetMehrereBegriffeGetrenntDurchLeerzeichen() {
        // "Hello" (1x in child) + "findme" (2x in item) -> 3 Treffer gesamt.
        viewer.search("Hello findme");
        assertEquals(3, viewer.getMatchCount());
    }

    @Test
    void mehrfacheLeerzeichenZwischenBegriffenWerdenIgnoriert() {
        viewer.search("  Hello    findme  ");
        assertEquals(3, viewer.getMatchCount());
    }

    @Test
    void ueberlappendeBegriffeWerdenNichtDoppeltGezaehlt() {
        // "findme" und "find" ueberlappen im selben Text -> ein zusammengefuehrter Treffer je Vorkommen.
        viewer.search("findme find");
        assertEquals(2, viewer.getMatchCount());
    }

    @Test
    void eigenerSplitterBestimmtDasTrennzeichen() {
        // Komma-Splitter: "Hello,findme" -> "Hello" (1x) + "findme" (2x) = 3 Treffer.
        viewer.setSearchTermSplitter(query -> java.util.Arrays.stream(query.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList());
        viewer.search("Hello, findme");
        assertEquals(3, viewer.getMatchCount());
    }

    @Test
    void eigenerSplitterTrenntNichtAnLeerzeichen() {
        // Mit Komma-Splitter ist "findme here" EIN Begriff -> kommt im item-Text nicht vor (0 Treffer).
        viewer.setSearchTermSplitter(query -> java.util.List.of(query));
        viewer.search("findme here findme");
        assertEquals(1, viewer.getMatchCount()); // ganzer Text "findme here findme" = genau 1 Vorkommen
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
    void aufeinanderfolgendeSuchenSetzenVorherigeTrefferZurueck() {
        // Folge-Suchen liefern jeweils nur ihre eigenen Treffer. Da Treffer im Frontend (als Ranges)
        // gezeichnet werden, bleiben die Token-Spans grundsätzlich unverändert/ungeteilt.
        viewer.search("findme"); // Treffer nur in item
        assertEquals(2, viewer.getMatchCount());

        viewer.search("Hello"); // Treffer nur in child
        assertEquals(1, viewer.getMatchCount());

        // Token-Texte sind unverändert vorhanden (kein server-seitiges Aufsplitten der Spans).
        assertTrue(viewer.searchableTexts().contains("findme here findme"));
        assertTrue(viewer.searchableTexts().contains("Hello & <world>"));
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

    @Test
    void rendertKommentarMitKommentarKlasse() {
        Element withComment = new Element("doc");
        withComment.addContent(new org.jdom2.Comment("ein Hinweis"));
        viewer.setRoot(withComment);

        assertTrue(viewer.searchableTexts().contains("ein Hinweis"), "Kommentartext fehlt");
        assertTrue(viewer.cssClassesOfTokenText("ein Hinweis").contains(CssClasses.COMMENT), "Kommentar-Klasse fehlt");
    }

    @Test
    void rendertCdataMitTextKlasse() {
        Element withCdata = new Element("doc");
        withCdata.addContent(new org.jdom2.CDATA("a < b && c"));
        viewer.setRoot(withCdata);

        // CDATA wird roh übernommen (nur Vaadin escaped beim Rendern) und als Text-Token geführt.
        assertTrue(viewer.searchableTexts().contains("a < b && c"), "CDATA-Text fehlt");
        assertTrue(viewer.cssClassesOfTokenText("a < b && c").contains(CssClasses.TEXT), "CDATA-Text-Klasse fehlt");
    }

    @Test
    void rendertNamespaceMitPraefixUndOhne() {
        org.jdom2.Namespace lib = org.jdom2.Namespace.getNamespace("lib", "urn:lib");
        Element withNs = new Element("catalog", lib);
        org.jdom2.Namespace defaultNs = org.jdom2.Namespace.getNamespace("urn:default");
        withNs.addNamespaceDeclaration(defaultNs);
        viewer.setRoot(withNs);

        // Sowohl der präfixbehaftete (xmlns:lib) als auch der Default-Namespace (xmlns) werden gerendert.
        assertTrue(viewer.searchableTexts().contains("xmlns:lib"), "Präfix-Namespace fehlt");
        assertTrue(viewer.searchableTexts().contains("xmlns"), "Default-Namespace fehlt");
        assertTrue(viewer.searchableTexts().contains("urn:lib"), "Namespace-URI fehlt");
    }

    @Test
    void rendertLeeresElementSelbstschliessend() {
        Element empty = new Element("root");
        empty.addContent(new Element("leer"));
        viewer.setRoot(empty);

        Element leer = empty.getChild("leer");
        // Selbstschließendes Element hat keinen Kinder-Container -> isExpanded liefert true (kein null-Fehler).
        assertNotNull(viewer.headerOf(leer));
        assertTrue(viewer.isExpanded(leer));
    }

    @Test
    void rendertInlineTextImStartTag() {
        Element doc = new Element("doc");
        Element title = new Element("title");
        title.setText("Nur Text");
        doc.addContent(title);
        viewer.setRoot(doc);

        // Ein Element mit reinem Textinhalt wird einzeilig (Inline) gerendert: kein eigener
        // Kinder-Container, Text steht in der Start-Tag-Zeile.
        assertTrue(viewer.searchableTexts().contains("Nur Text"));
        assertTrue(viewer.isExpanded(title), "Inline-Text-Element hat keinen Kinder-Container");
    }

    /** Sammelt eine Komponente und alle ihre Nachfahren. */
    private static List<Component> descendants(Component c) {
        List<Component> result = new ArrayList<>();
        result.add(c);
        c.getChildren().forEach(child -> result.addAll(descendants(child)));
        return result;
    }
}
