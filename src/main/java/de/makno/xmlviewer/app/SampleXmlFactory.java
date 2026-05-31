package de.makno.xmlviewer.app;

import org.jdom2.CDATA;
import org.jdom2.Comment;
import org.jdom2.Element;
import org.jdom2.Namespace;

/**
 * Erzeugt einen absichtlich <em>großen</em> Beispiel-XML-Baum für die Demo: viele Knoten (vertikales
 * Scrollen) und teils sehr lange Zeilen durch lange Attributwerte/Texte (horizontales Scrollen).
 *
 * <p>Deckt zugleich die Sonderfälle ab: Namespace, Kommentar, CDATA und XML-Sonderzeichen
 * ({@code < & " '}) zum Prüfen des Escapings.
 */
final class SampleXmlFactory {

    private static final Namespace LIB = Namespace.getNamespace("lib", "https://makno.de/library");
    private static final int BOOK_COUNT = 40;
    private static final String[] GENRES = {"fiction", "science", "history", "poetry", "technology"};
    private static final String LOREM =
            "Dies ist ein bewusst sehr langer Beschreibungstext, der nicht umbrochen wird, damit der "
                    + "horizontale Scrollbalken der XmlViewer-Komponente sichtbar und testbar ist – "
                    + "inklusive Sonderzeichen wie < & \" ' und einer Fortsetzung bis zum rechten Rand.";

    private SampleXmlFactory() {}

    /** Baut den kompletten Beispielbaum mit {@value #BOOK_COUNT} Büchern. */
    static Element createLibrary() {
        Element library = new Element("library", LIB);
        library.setAttribute("name", "Makno Demo-Bibliothek");
        library.setAttribute("location", "Musterstadt, Beispielweg 1, sehr lange Adresszeile zum Testen des Scrollens");
        library.addContent(new Comment("Generierter Beispielkatalog mit Sonderzeichen: < & \" '"));

        for (int i = 1; i <= BOOK_COUNT; i++) {
            library.addContent(createBook(i));
        }
        return library;
    }

    /** Liefert ein tief liegendes Element für die Highlight-Demo (erstes Kapitel des 25. Buchs). */
    static Element findHighlightTarget(Element library) {
        Element book = library.getChildren("book", LIB).get(24);
        return book.getChild("chapters", LIB).getChildren("chapter", LIB).get(0);
    }

    private static Element createBook(int index) {
        Element book = new Element("book", LIB);
        book.setAttribute("id", "b-%03d".formatted(index));
        book.setAttribute("genre", GENRES[index % GENRES.length]);
        book.setAttribute("description", "Buch %d – %s".formatted(index, LOREM));

        Element title = new Element("title", LIB);
        title.setAttribute("lang", index % 2 == 0 ? "de" : "en");
        title.setText("Band %d: Der <Würfel> & die Zukunft".formatted(index));
        book.addContent(title);

        Element author = new Element("author", LIB);
        author.setText("Autor Nummer %d".formatted(index));
        book.addContent(author);

        Element price = new Element("price", LIB);
        price.setAttribute("currency", "EUR");
        price.setText("%,.2f".formatted(9.99 + index));
        book.addContent(price);

        book.addContent(createChapters(index));
        return book;
    }

    private static Element createChapters(int bookIndex) {
        Element chapters = new Element("chapters", LIB);
        int count = 2 + (bookIndex % 3);
        for (int c = 1; c <= count; c++) {
            Element chapter = new Element("chapter", LIB);
            chapter.setAttribute("no", Integer.toString(c));
            chapter.setText("Kapitel %d von Buch %d".formatted(c, bookIndex));
            chapters.addContent(chapter);
        }
        Element summary = new Element("summary", LIB);
        summary.addContent(new CDATA("Zusammenfassung mit Markup-Zeichen: if (a < b && b > c) { gilt; }"));
        chapters.addContent(summary);
        return chapters;
    }
}
