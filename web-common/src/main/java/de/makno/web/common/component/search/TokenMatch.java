package de.makno.web.common.component.search;

import java.io.Serializable;

/**
 * Ein einzelner Suchtreffer, beschrieben als Zeichen-Bereich innerhalb eines durchsuchbaren Tokens.
 *
 * <p>Bewusst <strong>ohne</strong> Bezug auf konkrete UI-Knoten: Der Treffer wird als reiner
 * Offset-Deskriptor an den {@link SearchHighlightRenderer} übergeben, der ihn im Frontend (CSS Custom
 * Highlight API) zeichnet.
 *
 * @param tokenIndex Position des Tokens in der Token-Liste (= Dokumentreihenfolge der
 *     {@code .search-token}-Spans im DOM, sodass das Frontend den Knoten positionsbasiert findet)
 * @param start 0-basierter Start-Offset des Treffers im Token-Text (inklusive)
 * @param end End-Offset des Treffers im Token-Text (exklusive)
 */
public record TokenMatch(int tokenIndex, int start, int end) implements Serializable {

    private static final long serialVersionUID = 1L;
}
