package de.makno.xmlviewer.component;

import com.vaadin.flow.component.html.Span;
import java.util.ArrayList;
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

    /** Markiert alle Treffer von {@code query} und springt zum ersten. Leer/{@code null} löscht die Suche. */
    void search(String query) {
        clearMarks();
        currentQuery = query;
        if (query == null || query.isEmpty()) {
            notifyChange();
            return;
        }
        Set<Element> ownersToExpand = new LinkedHashSet<>();
        for (SearchableToken token : tokens) {
            if (markMatchesIn(token, query)) {
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
     * Zerlegt den Span eines Tokens in Treffer- und Nicht-Treffer-Teile und markiert die Treffer.
     *
     * @return {@code true}, wenn mindestens ein Treffer gefunden wurde
     */
    private boolean markMatchesIn(SearchableToken token, String query) {
        String haystack = normalize(token.text());
        String needle = normalize(query);
        int index = haystack.indexOf(needle);
        if (index < 0) {
            return false;
        }
        Span span = token.span();
        span.removeAll();
        int from = 0;
        while (index >= 0) {
            if (index > from) {
                span.add(new Span(token.text().substring(from, index)));
            }
            Span match = new Span(token.text().substring(index, index + query.length()));
            match.addClassName(CssClasses.SEARCH_MATCH);
            span.add(match);
            matchSpans.add(match);
            from = index + query.length();
            index = haystack.indexOf(needle, from);
        }
        if (from < token.text().length()) {
            span.add(new Span(token.text().substring(from)));
        }
        return true;
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
