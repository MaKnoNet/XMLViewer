package de.makno.xmlviewer.navigation;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;

/**
 * Ereignis, das eine {@link MatchNavigable}-Quelle bei jeder Änderung ihrer Suchtreffer oder der
 * Treffer-Navigation feuert. Trägt die Gesamtzahl der Treffer und den Index des aktuellen Treffers,
 * damit Anzeige-Komponenten (z.&nbsp;B. {@link SearchNavigator}) synchron bleiben.
 *
 * <p>Die Quelle ist generisch ein {@link Component}, damit der Event nicht an einen konkreten
 * Komponententyp gebunden ist und von beliebigen Such-Quellen genutzt werden kann.
 */
public class MatchChangeEvent extends ComponentEvent<Component> {

    private static final long serialVersionUID = 1L;

    private final int matchCount;
    private final int currentMatchIndex;

    /**
     * @param source die auslösende Komponente
     * @param matchCount Anzahl der aktuellen Suchtreffer
     * @param currentMatchIndex 0-basierter Index des aktuellen Treffers, oder {@code -1}
     */
    public MatchChangeEvent(Component source, int matchCount, int currentMatchIndex) {
        super(source, false);
        this.matchCount = matchCount;
        this.currentMatchIndex = currentMatchIndex;
    }

    /** Anzahl der Treffer der aktuellen Suche. */
    public int getMatchCount() {
        return matchCount;
    }

    /** 0-basierter Index des aktuellen Treffers, oder {@code -1}, wenn keiner aktiv ist. */
    public int getCurrentMatchIndex() {
        return currentMatchIndex;
    }
}
