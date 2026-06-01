package de.makno.xmlviewer.navigation;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.shared.Registration;

/**
 * Abstraktion einer Quelle, deren Suchtreffer vorwärts/rückwärts durchlaufen werden können und die
 * über ihren Treffer-Stand (Anzahl + aktueller Index) Auskunft gibt.
 *
 * <p>Entkoppelt die Such-Navigations-UI (z.&nbsp;B. {@link SearchNavigator}) von der konkreten
 * Such-Implementierung (Dependency Inversion): Die UI kennt nur dieses Interface, nicht den
 * konkreten Komponententyp.
 */
public interface MatchNavigable {

    /**
     * Sucht den angegebenen Text, markiert alle Treffer und springt zum ersten. Leerer/{@code null}
     * Text löscht die Suche.
     */
    void search(String query);

    /** Springt zum nächsten Suchtreffer (umlaufend). */
    void nextMatch();

    /** Springt zum vorherigen Suchtreffer (umlaufend). */
    void previousMatch();

    /** Anzahl der aktuellen Suchtreffer. */
    int getMatchCount();

    /** 0-basierter Index des aktuellen Treffers, oder {@code -1}, wenn keiner aktiv ist. */
    int getCurrentMatchIndex();

    /**
     * Registriert einen Listener, der bei jeder Änderung der Suchtreffer oder der Treffer-Navigation
     * gefeuert wird.
     */
    Registration addMatchChangeListener(ComponentEventListener<MatchChangeEvent> listener);
}
