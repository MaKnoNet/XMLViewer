package de.makno.xmlviewer.component;

import java.util.List;

/**
 * Schlanke Abstraktion für das <em>Zeichnen</em> der Suchtreffer – getrennt von deren Ermittlung im
 * {@link XmlSearchController}. Die Standard-Implementierung lagert das Highlighting ins Frontend aus
 * (CSS Custom Highlight API): Treffer werden als Text-Ranges markiert, statt server-seitig den
 * Komponentenbaum in Treffer-/Nicht-Treffer-Spans zu zerlegen. Dadurch entsteht pro Treffer
 * <strong>kein</strong> zusätzlicher DOM-Knoten und kein zusätzlicher Session-Heap.
 *
 * <p>Durch diese Entkopplung (Dependency Inversion) bleibt der {@link XmlSearchController} rein
 * server-seitig testbar: Tests übergeben einen aufzeichnenden Renderer statt eines echten Clients.
 */
interface SearchHighlightRenderer {

    /**
     * Zeichnet alle Treffer und hebt den Treffer an {@code currentIndex} hervor. Eine leere Liste
     * (bzw. {@code currentIndex == -1}) entfernt vorhandene Markierungen.
     *
     * @param matches alle Treffer in Dokumentreihenfolge
     * @param currentIndex Index des aktuellen Treffers in {@code matches}, oder {@code -1}
     */
    void render(List<TokenMatch> matches, int currentIndex);

    /**
     * Verschiebt nur die Hervorhebung des aktuellen Treffers (Vor-/Zurück-Navigation), ohne die
     * Treffermenge neu zu übertragen.
     *
     * @param currentIndex Index des neuen aktuellen Treffers
     */
    void moveCurrent(int currentIndex);

    /** Entfernt alle Such-Markierungen. */
    void clear();
}
