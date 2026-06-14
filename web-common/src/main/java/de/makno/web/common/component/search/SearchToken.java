package de.makno.web.common.component.search;

import com.vaadin.flow.function.SerializableRunnable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Ein durchsuchbares Text-Token: der zu durchsuchende Klartext plus eine optionale Reveal-Aktion, die
 * den Treffer sichtbar macht (z.&nbsp;B. einen zugeklappten Zweig aufklappen). Für Komponenten ohne
 * Sichtbarkeits-Logik (reiner Text) ist die Aktion ein No-Op ({@link #of(String)}).
 *
 * <p>Bewusst <strong>ohne</strong> Bezug auf konkrete UI-Knoten oder ein Eingabeformat (kein Vaadin-
 * {@code Span}, kein XML-{@code Element}) – dadurch ist der {@link SearchController} von der konkreten
 * Komponente entkoppelt und wiederverwendbar.
 *
 * @param text der Klartext des Tokens (Grundlage der Treffersuche)
 * @param onReveal Aktion, die einen Treffer in diesem Token sichtbar macht (nie {@code null})
 */
public record SearchToken(String text, SerializableRunnable onReveal) implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Geteilte No-Op-Reveal-Aktion für Tokens ohne Sichtbarkeits-Logik (z.&nbsp;B. reiner Text). */
    private static final SerializableRunnable NO_REVEAL = () -> {};

    public SearchToken {
        Objects.requireNonNull(text, "text");
        Objects.requireNonNull(onReveal, "onReveal");
    }

    /** Token ohne Reveal-Aktion – für Komponenten, deren Treffer ohne Aufklappen sichtbar sind. */
    public static SearchToken of(String text) {
        return new SearchToken(text, NO_REVEAL);
    }
}
