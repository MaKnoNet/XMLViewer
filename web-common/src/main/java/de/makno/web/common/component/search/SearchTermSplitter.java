package de.makno.web.common.component.search;

import java.io.Serializable;
import java.util.List;

/**
 * Zerlegt einen Sucheingabe-Text in die einzelnen Begriffe, die hervorgehoben werden sollen.
 *
 * <p>Ermöglicht es der einbettenden Anwendung, das Trennverhalten frei zu bestimmen – z.&nbsp;B.
 * Trennung an Leerzeichen (Standard), an Komma, an mehreren Trennzeichen oder gar keine Trennung
 * (gesamter Text als ein Begriff).
 */
@FunctionalInterface
public interface SearchTermSplitter extends Serializable {

    /**
     * @param query der eingegebene Suchtext (kann leer oder {@code null} sein)
     * @return die zu suchenden/hervorzuhebenden Begriffe; eine leere Liste bedeutet „keine Suche"
     */
    List<String> split(String query);
}
