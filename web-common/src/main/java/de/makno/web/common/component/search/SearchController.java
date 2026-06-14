package de.makno.web.common.component.search;

import com.vaadin.flow.function.SerializableRunnable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Ermittelt die Textsuche über die durchsuchbaren {@link SearchToken}s einer Komponente: findet und
 * zählt Treffer, navigiert zwischen ihnen und meldet jede Änderung an die einbettende Komponente.
 *
 * <p>Das <em>Zeichnen</em> der Treffer ist bewusst ausgelagert: Der Controller erzeugt nur
 * {@link TokenMatch}-Deskriptoren (Token-Index + Zeichen-Offsets) und übergibt sie an einen
 * {@link SearchHighlightRenderer}. Ebenso ist das <em>Sichtbarmachen</em> eines Treffers entkoppelt:
 * Jedes {@link SearchToken} trägt seine eigene {@link SearchToken#onReveal()}-Aktion (z.&nbsp;B. den
 * Element-Zweig aufklappen); für reinen Text ist sie ein No-Op. Dadurch ist der Controller von der
 * konkreten Komponente unabhängig (Dependency Inversion) und rein server-seitig testbar.
 *
 * <p>Nicht thread-safe: hält veränderlichen Such-Zustand und gehört zu genau einer Komponente (also
 * zu einer UI/Session); Zugriff nur aus dem Session-Thread.
 */
public final class SearchController implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Standard-Aufteilung: an Whitespace trennen, leere Begriffe verwerfen. */
    public static final SearchTermSplitter DEFAULT_TERM_SPLITTER = query -> {
        if (query == null || query.isBlank()) {
            return List.of();
        }
        return Arrays.stream(query.trim().split("\\s+"))
                .filter(term -> !term.isEmpty())
                .toList();
    };

    private final List<SearchToken> tokens;
    private final SearchHighlightRenderer highlightRenderer;
    private final SerializableRunnable onMatchChange;

    private List<TokenMatch> matches = List.of();
    private int currentMatchIndex = -1;
    private String currentQuery;
    private boolean caseSensitive;
    private SearchTermSplitter termSplitter = DEFAULT_TERM_SPLITTER;

    public SearchController(
            List<SearchToken> tokens, SearchHighlightRenderer highlightRenderer, SerializableRunnable onMatchChange) {
        this.tokens = tokens;
        this.highlightRenderer = highlightRenderer;
        this.onMatchChange = onMatchChange;
    }

    public int getMatchCount() {
        return matches.size();
    }

    public int getCurrentMatchIndex() {
        return currentMatchIndex;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        if (this.caseSensitive == caseSensitive) {
            return;
        }
        this.caseSensitive = caseSensitive;
        if (hasActiveQuery()) {
            search(currentQuery);
        }
    }

    /** Setzt die Begriff-Aufteilung; eine aktive Suche wird mit dem neuen Splitter neu ausgeführt. */
    public void setTermSplitter(SearchTermSplitter termSplitter) {
        this.termSplitter = Objects.requireNonNull(termSplitter, "termSplitter");
        if (hasActiveQuery()) {
            search(currentQuery);
        }
    }

    /**
     * Sucht alle Treffer von {@code query}, macht deren Tokens sichtbar, lässt sie zeichnen und springt
     * zum ersten. Mehrere durch Whitespace getrennte Begriffe werden einzeln gesucht (ODER-Verknüpfung).
     * Leer/{@code null} löscht die Suche.
     */
    public void search(String query) {
        currentQuery = query;
        List<String> terms = splitTerms(query);
        matches = terms.isEmpty() ? List.of() : collectMatches(terms);
        currentMatchIndex = matches.isEmpty() ? -1 : 0;
        revealMatches();
        highlightRenderer.render(matches, currentMatchIndex);
        notifyChange();
    }

    /** Springt umlaufend zum nächsten Treffer. */
    public void nextMatch() {
        if (!matches.isEmpty()) {
            moveCurrentTo((currentMatchIndex + 1) % matches.size());
        }
    }

    /** Springt umlaufend zum vorherigen Treffer. */
    public void previousMatch() {
        if (!matches.isEmpty()) {
            moveCurrentTo((currentMatchIndex - 1 + matches.size()) % matches.size());
        }
    }

    /** Entfernt alle Such-Markierungen. */
    public void clearSearch() {
        currentQuery = null;
        matches = List.of();
        currentMatchIndex = -1;
        highlightRenderer.clear();
        notifyChange();
    }

    /** Sammelt alle Treffer aller Begriffe in Dokumentreihenfolge (Token-Index, dann Offset). */
    private List<TokenMatch> collectMatches(List<String> terms) {
        List<TokenMatch> found = new ArrayList<>();
        for (int index = 0; index < tokens.size(); index++) {
            String text = tokens.get(index).text();
            for (int[] range : findMatchRanges(text, terms)) {
                found.add(new TokenMatch(index, range[0], range[1]));
            }
        }
        return found;
    }

    /**
     * Führt die Reveal-Aktion jedes Tokens aus, das mindestens einen Treffer enthält. Tokens, die sich
     * dieselbe {@link SearchToken#onReveal()}-Instanz teilen (z.&nbsp;B. mehrere Tokens desselben
     * XML-Elements), werden über Identität dedupliziert – so klappt ein Element nur einmal auf.
     */
    private void revealMatches() {
        Set<SerializableRunnable> reveals = Collections.newSetFromMap(new IdentityHashMap<>());
        for (TokenMatch match : matches) {
            reveals.add(tokens.get(match.tokenIndex()).onReveal());
        }
        reveals.forEach(Runnable::run);
    }

    private void moveCurrentTo(int newIndex) {
        currentMatchIndex = newIndex;
        // Den aktuellen Treffer (wieder) sichtbar machen – sonst bliebe er bei zugeklappten Bereichen
        // unsichtbar und der Frontend-Scroll liefe ins Leere.
        revealOf(matches.get(newIndex));
        highlightRenderer.moveCurrent(currentMatchIndex);
        notifyChange();
    }

    /** Führt die Reveal-Aktion des Tokens aus, das den Treffer enthält. */
    private void revealOf(TokenMatch match) {
        tokens.get(match.tokenIndex()).onReveal().run();
    }

    /** Zerlegt die Eingabe über den (anpassbaren) {@link SearchTermSplitter}; leere Begriffe werden verworfen. */
    private List<String> splitTerms(String query) {
        List<String> terms = termSplitter.split(query);
        if (terms == null) {
            return List.of();
        }
        return terms.stream().filter(term -> term != null && !term.isEmpty()).toList();
    }

    /**
     * Findet alle Treffer-Intervalle aller Begriffe im Text, sortiert nach Start und mit
     * verschmolzenen Überlappungen (z.&nbsp;B. „EUR" und „EU"), sodass sich Bereiche nicht überlagern.
     *
     * <p>Der Abgleich erfolgt zeichenweise direkt auf dem unveränderten Originaltext
     * ({@link String#regionMatches(boolean, int, String, int, int)}). Dadurch zeigen die gelieferten
     * Offsets unabhängig von der Groß-/Kleinschreibung immer in den Token-Text – auch bei Zeichen,
     * deren Lowercase-Form eine andere Länge hätte (z.&nbsp;B. {@code İ}); ein vorher lowercase
     * normalisierter Vergleich hätte die Offsets in solchen Fällen verschoben.
     */
    private List<int[]> findMatchRanges(String text, List<String> terms) {
        List<int[]> ranges = new ArrayList<>();
        for (String term : terms) {
            int termLength = term.length();
            if (termLength == 0) {
                continue;
            }
            int lastStart = text.length() - termLength;
            for (int index = 0; index <= lastStart; index++) {
                if (text.regionMatches(!caseSensitive, index, term, 0, termLength)) {
                    ranges.add(new int[] {index, index + termLength});
                }
            }
        }
        ranges.sort(Comparator.comparingInt(range -> range[0]));
        return mergeOverlaps(ranges);
    }

    /** Verschmilzt überlappende/anschließende Intervalle einer nach Start sortierten Liste. */
    private static List<int[]> mergeOverlaps(List<int[]> sortedRanges) {
        List<int[]> merged = new ArrayList<>();
        for (int[] range : sortedRanges) {
            int[] last = merged.isEmpty() ? null : merged.get(merged.size() - 1);
            if (last != null && range[0] <= last[1]) {
                last[1] = Math.max(last[1], range[1]);
            } else {
                merged.add(new int[] {range[0], range[1]});
            }
        }
        return merged;
    }

    private boolean hasActiveQuery() {
        return currentQuery != null && !currentQuery.isBlank();
    }

    private void notifyChange() {
        onMatchChange.run();
    }
}
