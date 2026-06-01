package de.makno.xmlviewer.component;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import org.jdom2.Attribute;
import org.jdom2.CDATA;
import org.jdom2.Comment;
import org.jdom2.Content;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.Text;

/**
 * Rendert einen {@link Element}-Baum in eine Vaadin-Komponentenstruktur aus {@link Div}- und
 * {@link Span}-Knoten – pro Element eine Start-Tag-Zeile, ein eingerückter Kinder-Container und eine
 * End-Tag-Zeile.
 *
 * <p>Reines Rendering ohne Such- oder Highlight-Zustand. Texte werden über {@code setText} gesetzt,
 * sodass Vaadin XML-Sonderzeichen automatisch escaped (kein innerHTML, kein XSS).
 *
 * <p>Eine Instanz ist auf <em>einen</em> {@link #render(Element)}-Aufruf ausgelegt.
 */
final class XmlTreeRenderer {

    private final boolean collapsible;

    private final Map<Element, Div> elementHeaders = new IdentityHashMap<>();
    private final Map<Element, Div> childContainers = new IdentityHashMap<>();
    private final Map<Element, Div> endTags = new IdentityHashMap<>();
    private final Map<Element, Span> toggles = new IdentityHashMap<>();
    private final List<SearchableToken> tokens = new ArrayList<>();

    XmlTreeRenderer(boolean collapsible) {
        this.collapsible = collapsible;
    }

    /** Rendert {@code root} und liefert den Baum samt der Nachschlage-Strukturen. */
    RenderedTree render(Element root) {
        Div container = new Div();
        container.addClassName(CssClasses.TREE);
        renderElement(root, container);
        return new RenderedTree(container, elementHeaders, childContainers, endTags, toggles, tokens);
    }

    private void renderElement(Element element, Div container) {
        List<Content> meaningful = meaningfulContent(element);
        boolean hasContent = !meaningful.isEmpty();
        boolean inlineText = isSingleInlineText(meaningful);

        Div header = newLine();
        elementHeaders.put(element, header);
        header.add(collapsible && hasContent && !inlineText ? newToggle(element) : newIndent());
        header.add(punct("<"));
        header.add(tag(element));
        renderNamespaces(element, header);
        renderAttributes(element, header);

        if (!hasContent) {
            header.add(punct("/>"));
            container.add(header);
            return;
        }
        if (inlineText) {
            String text = ((Text) meaningful.get(0)).getText().trim();
            header.add(punct(">"));
            header.add(token(CssClasses.TEXT, text, element));
            header.add(punct("</"));
            header.add(tag(element));
            header.add(punct(">"));
            container.add(header);
            return;
        }

        header.add(punct(">"));
        container.add(header);
        container.add(renderChildren(element, meaningful));
        Div endTag = endTagLine(element);
        endTags.put(element, endTag);
        container.add(endTag);
    }

    private Div renderChildren(Element element, List<Content> meaningful) {
        Div children = new Div();
        children.addClassName(CssClasses.CHILDREN);
        childContainers.put(element, children);
        for (Content content : meaningful) {
            switch (content) {
                case Element child -> renderElement(child, children);
                case CDATA cdata -> children.add(cdataLine(cdata.getText(), element));
                case Comment comment -> children.add(commentLine(comment.getText(), element));
                case Text text -> children.add(textLine(text.getText().trim(), element));
                default -> {
                    /* andere Knotentypen werden nicht dargestellt */
                }
            }
        }
        return children;
    }

    private void renderNamespaces(Element element, Div header) {
        for (Namespace ns : element.getNamespacesIntroduced()) {
            String name = ns.getPrefix().isEmpty() ? "xmlns" : "xmlns:" + ns.getPrefix();
            header.add(plain(" "));
            header.add(token(CssClasses.ATTR_NAME, name, element));
            header.add(quotedValue(ns.getURI(), element));
        }
    }

    private void renderAttributes(Element element, Div header) {
        for (Attribute attribute : element.getAttributes()) {
            header.add(plain(" "));
            header.add(token(CssClasses.ATTR_NAME, attribute.getQualifiedName(), element));
            header.add(quotedValue(attribute.getValue(), element));
        }
    }

    /** Erzeugt {@code ="wert"} als Span-Folge und gibt einen umschließenden Span zurück. */
    private Span quotedValue(String value, Element owner) {
        Span group = new Span();
        group.add(punct("="));
        group.add(punct("\""));
        group.add(token(CssClasses.ATTR_VALUE, value, owner));
        group.add(punct("\""));
        return group;
    }

    private Div endTagLine(Element element) {
        Div line = newLine();
        // Marker am Linienende kommt aus CSS (.xml-endtag::before) – keine Layoutbreite, kein Java-Text.
        line.addClassName(CssClasses.ENDTAG);
        line.add(newIndent());
        line.add(punct("</"));
        line.add(tag(element));
        line.add(punct(">"));
        return line;
    }

    private Div textLine(String text, Element owner) {
        Div line = newLine();
        line.add(newIndent());
        line.add(token(CssClasses.TEXT, text, owner));
        return line;
    }

    private Div commentLine(String text, Element owner) {
        Div line = newLine();
        line.add(newIndent());
        line.add(punct("<!--"));
        line.add(plain(" "));
        line.add(token(CssClasses.COMMENT, text, owner));
        line.add(plain(" "));
        line.add(punct("-->"));
        return line;
    }

    private Div cdataLine(String text, Element owner) {
        Div line = newLine();
        line.add(newIndent());
        line.add(punct("<![CDATA["));
        line.add(token(CssClasses.TEXT, text, owner));
        line.add(punct("]]>"));
        return line;
    }

    /** Klick-Marker; das sichtbare Zeichen liefert CSS ({@code .xml-toggle::before}), kein Java-Text. */
    private Span newToggle(Element element) {
        Span toggle = new Span();
        toggle.addClassName(CssClasses.TOGGLE);
        toggles.put(element, toggle);
        return toggle;
    }

    private Span tag(Element element) {
        return token(CssClasses.TAG, element.getQualifiedName(), element);
    }

    private static Div newLine() {
        Div line = new Div();
        line.addClassName(CssClasses.LINE);
        return line;
    }

    private static Span newIndent() {
        Span indent = new Span();
        indent.addClassName(CssClasses.INDENT);
        return indent;
    }

    private static Span punct(String text) {
        Span span = new Span(text);
        span.addClassName(CssClasses.PUNCT);
        return span;
    }

    /** Schlichter Span ohne Klasse; {@code setText} escaped automatisch. */
    private static Span plain(String text) {
        return new Span(text);
    }

    /** Erzeugt einen eingefärbten, durchsuchbaren Span und registriert ihn für die Suche. */
    private Span token(String cssClass, String text, Element owner) {
        String value = text == null ? "" : text;
        Span span = new Span(value);
        span.addClassName(cssClass);
        tokens.add(new SearchableToken(span, value, owner));
        return span;
    }

    private static boolean isSingleInlineText(List<Content> meaningful) {
        return meaningful.size() == 1 && meaningful.get(0) instanceof Text && !(meaningful.get(0) instanceof CDATA);
    }

    /** Sinnvoller Inhalt: Elemente, Kommentare, CDATA und nicht-leerer Text (reine Whitespace-Knoten raus). */
    private static List<Content> meaningfulContent(Element element) {
        List<Content> result = new ArrayList<>();
        for (Content content : element.getContent()) {
            boolean keep =
                    switch (content) {
                        case CDATA ignored -> true;
                        case Text text -> !text.getText().trim().isEmpty();
                        case Element ignored -> true;
                        case Comment ignored -> true;
                        default -> false;
                    };
            if (keep) {
                result.add(content);
            }
        }
        return result;
    }
}
