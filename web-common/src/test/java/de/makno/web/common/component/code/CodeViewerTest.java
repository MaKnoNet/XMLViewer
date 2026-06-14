package de.makno.web.common.component.code;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
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
 * Unit-Tests für den server-seitigen Vertrag des {@link CodeViewer} – ohne Servlet/UI. Der eigentliche
 * CodeMirror-Editor lebt clientseitig und ist hier nicht abbildbar; getestet werden Zustand, der
 * {@link CodeViewer#onMatchChange(int, int)}-Callback, das Reset-Event, die Serialisierung und das
 * Zusammenspiel mit dem {@link SearchNavigator}.
 */
class CodeViewerTest {

    private CodeViewer viewer;

    @BeforeEach
    void setUp() {
        viewer = new CodeViewer("public class Foo {}", CodeLanguage.JAVA);
    }

    @Test
    void haeltTextUndSprache() {
        assertEquals("public class Foo {}", viewer.getText());
        assertEquals(CodeLanguage.JAVA, viewer.getLanguage());
    }

    @Test
    void setLanguageNullAktiviertAutoErkennung() {
        viewer.setLanguage(null);
        assertNull(viewer.getLanguage(), "null = Auto-Erkennung");
    }

    @Test
    void leitetSpracheAusDateinamenAb() {
        viewer.setLanguageFromFileName("script.py");
        assertEquals(CodeLanguage.PYTHON, viewer.getLanguage());
    }

    @Test
    void toggleSetzenDenZustand() {
        viewer.setDark(true);
        viewer.setWrap(true);
        viewer.setShowLineNumbers(false);
        viewer.setSearchCaseSensitive(true);

        assertTrue(viewer.isDark());
        assertTrue(viewer.isWrap());
        assertFalse(viewer.isShowLineNumbers());
        assertTrue(viewer.isSearchCaseSensitive());
    }

    @Test
    void onMatchChangeAktualisiertGetterUndFeuertEvent() {
        List<String> events = new ArrayList<>();
        viewer.addMatchChangeListener(e -> events.add(e.getMatchCount() + "/" + e.getCurrentMatchIndex()));

        viewer.onMatchChange(5, 2);

        assertEquals(5, viewer.getMatchCount());
        assertEquals(2, viewer.getCurrentMatchIndex());
        assertEquals(List.of("5/2"), events);
    }

    @Test
    void neuerTextFeuertResetEventUndLeertTrefferstand() {
        viewer.onMatchChange(3, 1); // es gibt aktive Treffer
        List<String> events = new ArrayList<>();
        viewer.addMatchChangeListener(
                e -> events.add(e.getMatchCount() + "/" + e.getCurrentMatchIndex() + "/" + e.isReset()));

        viewer.setText("def neu(): pass");

        assertEquals(0, viewer.getMatchCount());
        assertEquals(-1, viewer.getCurrentMatchIndex());
        assertTrue(events.contains("0/-1/true"), "Reset-Event beim Textwechsel fehlt: " + events);
        assertEquals("def neu(): pass", viewer.getText());
    }

    @Test
    void istVollstaendigSerialisierbarUndBleibtFunktionsfaehig() throws Exception {
        viewer.setDark(true);
        viewer.onMatchChange(4, 1);

        CodeViewer restored = deserialize(serialize(viewer));

        assertEquals("public class Foo {}", restored.getText());
        assertEquals(CodeLanguage.JAVA, restored.getLanguage());
        assertTrue(restored.isDark());
        assertEquals(4, restored.getMatchCount());
        assertEquals(1, restored.getCurrentMatchIndex());
    }

    @Test
    void arbeitetMitSearchNavigatorZusammen() {
        SearchNavigator navigator = new SearchNavigator(viewer);

        viewer.onMatchChange(2, 0); // wie eine vom Client gemeldete Suche: 2 Treffer, aktueller 1/2
        assertTrue(labels(navigator).contains("1/2"), "Navigator soll den Treffer-Stand des CodeViewer zeigen");

        viewer.onMatchChange(2, 1);
        assertTrue(labels(navigator).contains("2/2"));
    }

    // ---- Helfer -------------------------------------------------------------

    private static List<String> labels(Component root) {
        return descendants(root)
                .filter(Span.class::isInstance)
                .map(c -> ((Span) c).getText())
                .filter(t -> t != null && !t.isEmpty())
                .toList();
    }

    private static Stream<Component> descendants(Component root) {
        return Stream.concat(Stream.of(root), root.getChildren().flatMap(CodeViewerTest::descendants));
    }

    private static byte[] serialize(Object object) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(object);
        }
        return bos.toByteArray();
    }

    private static CodeViewer deserialize(byte[] bytes) throws Exception {
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            return (CodeViewer) ois.readObject();
        }
    }
}
