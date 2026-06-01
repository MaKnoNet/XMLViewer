package de.makno.xmlviewer.component;

import com.vaadin.flow.component.html.Span;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Consumer;
import org.jdom2.Element;

/**
 * Steuert die Textsuche über die durchsuchbaren Tokens eines gerenderten Baums: markiert Treffer,
 * navigiert zwischen ihnen und meldet jede Änderung an die einbettende {@link XmlViewer}.
 *
 * <p>Hält ausschließlich Such-Zustand; das Rendern und das Aufklappen/Scrollen liegen außerhalb und
 * werden über die übergebenen Callbacks angestoßen.
 */
final class XmlSearchController {

    private final List<SearchableToken> tokens;
    private final Consumer<Element> expandToElement;
    private final Consumer<Span> scrollToSpan;
    private final Runnable onMatchChange;

    private final List<Span> matchSpans = new ArrayList<>();
    private int currentMatchIndex = -1;
    private String currentQuery;
    private boolean caseSensitive;

    XmlSearchController(
            List<SearchableToken> tokens,
            Consumer<Element> expandToElement,
            Consumer<Span> scrollToSpan,
            Runnable onMatchChange) {
        this.tokens = tokens;
        this.expandToElement = expandToElement;
        this.scrollToSpan = scrollToSpan;
        this.onMatchChange = onMatchChange;
    }

    int getMatchCount() {
        return matchSpans.size();
    }

    int getCurrentMatchIndex() {
        return currentMatchIndex;
    }

    boolean isCaseSensitive() {
        return caseSensitive;
    }

    void setCaseSensitive(boolean caseSensitive) {
        if (this.caseSensitive == caseSensitive) {
            return;
        }
        this.caseSensitive = caseSensitive;
        if (hasActiveQuery()) {
            search(currentQuery);
        }
    }

    /**
     * Markiert alle Treffer von {@code query} und springt zum ersten. Mehrere durch Whitespace
     * getrennte Begriffe werden einzeln gesucht (ODER-Verknüpfung). Leer/{@code null} löscht die Suche.
     */
    void search(String query) {
        clearMarks();
        currentQuery = query;
        List<String> terms = splitTerms(query);
        if (terms.isEmpty()) {
            notifyChange();
            return;
        }
        Set<Element> ownersToExpand = new LinkedHashSet<>();
        for (SearchableToken token : tokens) {
            if (markMatchesIn(token, terms)) {
                addIfPresent(ownersToExpand, token.owner());
            }
        }
        ownersToExpand.forEach(expandToElement);

        currentMatchIndex = -1;
        if (matchSpans.isEmpty()) {
            notifyChange();
        } else {
            moveCurrentTo(0);
        }
    }

    /** Zerlegt die Eingabe an Whitespace in einzelne Suchbegriffe (leere werden verworfen). */
    private static List<String> splitTerms(String query) {
        if (query == null || query.isBlank()) {
            return List.of();
        }
        return Arrays.stream(query.trim().split("\\s+"))
                .filter(term -> !term.isEmpty())
                .toList();
    }

    /** Springt umlaufend zum nächsten Treffer. */
    void nextMatch() {
        if (!matchSpans.isEmpty()) {
            moveCurrentTo((currentMatchIndex + 1) % matchSpans.size());
        }
    }

    /** Springt umlaufend zum vorherigen Treffer. */
    void previousMatch() {
        if (!matchSpans.isEmpty()) {
            moveCurrentTo((currentMatchIndex - 1 + matchSpans.size()) % matchSpans.size());
        }
    }

    /** Entfernt alle Such-Markierungen. */
    void clearSearch() {
        clearMarks();
        currentQuery = null;
        notifyChange();
    }

    /**
     * Zerlegt den Span eines Tokens in Treffer- und Nicht-Treffer-Teile und markiert die Treffer
     * aller Suchbegriffe.
     *
     * @return {@code true}, wenn mindestens ein Treffer gefunden wurde
     */
    private boolean markMatchesIn(SearchableToken token, List<String> terms) {
        List<int[]> ranges = findMatchRanges(token.text(), terms);
        if (ranges.isEmpty()) {
            return false;
        }
        Span span = token.span();
        span.removeAll();
        int from = 0;
        for (int[] range : ranges) {
            if (range[0] > from) {
                span.add(new Span(token.text().substring(from, range[0])));
            }
            Span match = new Span(token.text().substring(range[0], range[1]));
            match.addClassName(CssClasses.SEARCH_MATCH);
            span.add(match);
            matchSpans.add(match);
            from = range[1];
        }
        if (from < token.text().length()) {
            span.add(new Span(token.text().substring(from)));
        }
        return true;
    }

    /**
     * Findet alle Treffer-Intervalle aller Begriffe im Text, sortiert nach Start und mit
     * verschmolzenen Überlappungen (z.&nbsp;B. „EUR" und „EU"), sodass die Span-Zerlegung lückenlos ist.
     */
    private List<int[]> findMatchRanges(String text, List<String> terms) {
        String haystack = normalize(text);
        List<int[]> ranges = new ArrayList<>();
        for (String term : terms) {
            String needle = normalize(term);
            for (int index = haystack.indexOf(needle); index >= 0; index = haystack.indexOf(needle, index + 1)) {
                ranges.add(new int[] {index, index + term.length()});
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

    private void moveCurrentTo(int newIndex) {
        if (isValidIndex(currentMatchIndex)) {
            matchSpans.get(currentMatchIndex).removeClassName(CssClasses.SEARCH_CURRENT);
        }
        currentMatchIndex = newIndex;
        Span current = matchSpans.get(currentMatchIndex);
        current.addClassName(CssClasses.SEARCH_CURRENT);
        scrollToSpan.accept(current);
        notifyChange();
    }

    private void clearMarks() {
        for (SearchableToken token : tokens) {
            token.span().removeAll();
            token.span().setText(token.text());
        }
        matchSpans.clear();
        currentMatchIndex = -1;
    }

    private boolean hasActiveQuery() {
        return currentQuery != null && !currentQuery.isEmpty();
    }

    private boolean isValidIndex(int index) {
        return index >= 0 && index < matchSpans.size();
    }

    private String normalize(String value) {
        return caseSensitive ? value : value.toLowerCase(Locale.ROOT);
    }

    private void notifyChange() {
        onMatchChange.run();
    }

    private static void addIfPresent(Set<Element> set, Element element) {
        if (element != null) {
            set.add(element);
        }
    }
}
