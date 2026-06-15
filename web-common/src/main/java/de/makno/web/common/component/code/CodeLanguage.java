package de.makno.web.common.component.code;

import java.util.List;

/**
 * Vom {@link CodeViewer} unterstützte Sprachen. Jede Konstante kennt ihre CodeMirror-6-Language-Id
 * (vom Frontend-Glue zur Auswahl der passenden CM6-Spracherweiterung genutzt) und typische
 * Datei-Endungen (für die Erkennung über {@link CodeLanguageDetector}).
 *
 * <p>Enums sind in Java implizit {@link java.io.Serializable} (Serialisierung über den Namen), daher
 * ohne {@code serialVersionUID}.
 */
public enum CodeLanguage {

    /** Kein Syntax-Highlighting (reiner Text). */
    PLAIN(""),
    JAVA("java", "java"),
    CSHARP("csharp", "cs", "csx"),
    PYTHON("python", "py", "pyw"),
    JSON("json", "json"),
    YAML("yaml", "yaml", "yml"),
    HTML("html", "html", "htm", "xhtml"),
    XML("xml", "xml", "xsd", "xsl", "xslt", "svg"),
    CSS("css", "css"),
    JAVASCRIPT("javascript", "js", "mjs", "cjs"),
    SQL("sql", "sql");

    private final String cm6Id;
    private final List<String> extensions;

    CodeLanguage(String cm6Id, String... extensions) {
        this.cm6Id = cm6Id;
        this.extensions = List.of(extensions);
    }

    /**
     * Die CodeMirror-6-Language-Id (leer für {@link #PLAIN}); das Frontend-Glue wählt darüber die
     * passende CM6-Spracherweiterung.
     */
    public String cm6Id() {
        return cm6Id;
    }

    /** Typische Datei-Endungen (klein, ohne führenden Punkt) für die Erkennung. */
    public List<String> extensions() {
        return extensions;
    }
}
