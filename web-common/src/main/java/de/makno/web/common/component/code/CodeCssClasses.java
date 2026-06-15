package de.makno.web.common.component.code;

/**
 * Zentrale CSS-Klassennamen des {@link CodeViewer} (keine Magic-Strings). Das eigentliche Editor-
 * Styling liefert CodeMirror selbst (per JS injiziert); hier nur der Rahmen des Host-Elements und die
 * Suchtreffer-Markierungen aus {@code code/styles/code-viewer.css}.
 */
final class CodeCssClasses {

    /** Wurzel-/Host-Element, in das CodeMirror den Editor rendert. */
    static final String ROOT = "codeviewer";

    private CodeCssClasses() {}
}
