package de.makno.web.common.component.xmlviewer;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.jdom2.Element;

/**
 * Ergebnis eines Render-Durchlaufs des {@link XmlTreeRenderer}.
 *
 * @param root der Wurzel-Container mit dem gesamten gerenderten Baum
 * @param elementHeaders Abbildung Element → Start-Tag-Zeile (für Highlight und Scroll), identitätsbasiert
 * @param childContainers Abbildung Element → Kinder-Container (für Ein-/Ausklappen), identitätsbasiert
 * @param endTags Abbildung Element → End-Tag-Zeile (mit-einklappen), identitätsbasiert
 * @param toggles Abbildung Element → Aufklapp-Dreieck (Marker aktualisieren), identitätsbasiert
 * @param tokens alle durchsuchbaren Tokens in Dokumentreihenfolge
 */
record RenderedTree(
        Div root,
        Map<Element, Div> elementHeaders,
        Map<Element, Div> childContainers,
        Map<Element, Div> endTags,
        Map<Element, Span> toggles,
        List<SearchableToken> tokens)
        implements Serializable {

    private static final long serialVersionUID = 1L;
}
