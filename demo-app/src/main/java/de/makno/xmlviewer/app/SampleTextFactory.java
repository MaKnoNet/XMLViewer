package de.makno.xmlviewer.app;

/**
 * Erzeugt einen absichtlich <em>großen</em> Beispiel-Klartext für die {@code TextViewer}-Demo: viele
 * Zeilen (vertikales Scrollen), sehr lange Zeilen (horizontales Scrollen), gelegentliche Leerzeilen
 * sowie ein wiederkehrendes Such-Token ({@value #SEARCH_TOKEN}) und XML-Sonderzeichen zum Prüfen des
 * Escapings.
 */
final class SampleTextFactory {

    private static final int LINE_COUNT = 120;
    static final String SEARCH_TOKEN = "findme";

    private SampleTextFactory() {}

    /** Baut den kompletten Beispieltext mit {@value #LINE_COUNT} Inhaltszeilen. */
    static String createSampleText() {
        StringBuilder text = new StringBuilder();
        text.append("TextViewer-Demo – mehrzeiliger Klartext mit Suche, Zeilennummern und Highlight.\n");
        text.append("Sonderzeichen werden korrekt escaped: < & \" ' – kein innerHTML, kein XSS.\n");
        text.append('\n');
        for (int line = 1; line <= LINE_COUNT; line++) {
            text.append("Zeile %03d: ".formatted(line));
            text.append("Dies ist ein bewusst langer Text zum Testen des horizontalen Scrollens – ");
            text.append(
                    "%s erscheint mehrfach, damit die Treffer-Navigation etwas zu tun hat. ".formatted(SEARCH_TOKEN));
            text.append("Betrag: %,.2f EUR. Markup: if (a < b && b > c) { tu_was(); }\n".formatted(9.99 + line));
            if (line % 10 == 0) {
                text.append('\n'); // gelegentliche Leerzeile – prüft den Index-Erhalt der Token-Liste
            }
        }
        return text.toString();
    }

    /** Kurzer Beispieltext für den Wechsel zur Laufzeit (zeigt das Reset-Verhalten). */
    static String createShortText() {
        return "Nur\nein\nkurzer\nBeispieltext\nmit findme.";
    }
}
