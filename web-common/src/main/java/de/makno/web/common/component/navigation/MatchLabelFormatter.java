package de.makno.web.common.component.navigation;

import java.io.Serializable;

/**
 * Formatiert den Anzeigetext des Treffer-Labels im {@link SearchNavigator}.
 *
 * <p>Ermöglicht es der einbettenden Anwendung, das Format frei zu bestimmen (z.&nbsp;B. „12/66",
 * „Treffer 12 von 66" oder lokalisierte Varianten), ohne die Komponente zu ändern.
 */
@FunctionalInterface
public interface MatchLabelFormatter extends Serializable {

    /**
     * @param matchCount Anzahl der Treffer ({@code 0}, wenn keine vorhanden)
     * @param currentPosition 1-basierte Position des aktuellen Treffers ({@code 0}, wenn kein Treffer
     *     aktiv ist)
     * @return der anzuzeigende Text
     */
    String format(int matchCount, int currentPosition);
}
