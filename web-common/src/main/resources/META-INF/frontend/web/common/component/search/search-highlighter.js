/*
 * Frontend-Highlighting der Suchtreffer über die CSS Custom Highlight API.
 *
 * Statt server-seitig den Komponentenbaum in Treffer-/Nicht-Treffer-Spans zu zerlegen (1 DOM-Knoten
 * + Heap pro Treffer, plus DOM-Mutationen über die Leitung), markiert dieses Modul Treffer als reine
 * Text-Ranges: kein zusätzlicher DOM-Knoten, kein Session-Heap pro Treffer. Gezeichnet wird über die
 * registrierten Highlights `search-match` (alle Treffer) und `search-current` (aktueller Treffer);
 * ihr Aussehen kommt aus den `::highlight(...)`-Regeln in styles/search.css.
 *
 * Der Server übergibt Treffer als flache Zahlenfolge "tokenIndex,start,end,tokenIndex,…" – bewusst
 * als Zeichenkette und nicht als JSON-Array: Vaadin 25 hat elemental.json entfernt, und eine
 * Bindung an Jackson waere nur die naechste Kopplung an eine JSON-Bibliothek. Über die
 * Klasse `.search-token` (Dokumentreihenfolge == Token-Reihenfolge) wird der Treffer-Knoten
 * positionsbasiert gefunden.
 *
 * Geteilt von allen Viewer-Komponenten (XmlViewer, TextViewer, …). Mehrere Instanzen je Seite werden
 * unterstützt: Die Highlight-Registry ist global pro Name, daher hält dieses Modul die Ranges je
 * Wurzel-Element und baut die globalen Highlights jeweils aus allen Wurzeln zusammen.
 */
(function () {
    const MATCH_HIGHLIGHT = "search-match";
    const CURRENT_HIGHLIGHT = "search-current";
    const TOKEN_SELECTOR = ".search-token";
    const VALUES_PER_MATCH = 3;
    // Trennzeichen der vom Server gelieferten Zahlenfolge; identisch im Gegenstueck
    // FrontendSearchHighlighter.java (SEPARATOR).
    const SEPARATOR = ",";

    // Wurzel-Element -> flaches Treffer-Array [tokenIndex, start, end, …]
    const matchesByRoot = new Map();
    // Wurzel-Element -> aktueller Treffer {token, start, end} oder null
    const currentByRoot = new Map();

    function isSupported() {
        return typeof CSS !== "undefined" && CSS.highlights && typeof Highlight !== "undefined";
    }

    function rangeFor(root, token, start, end) {
        const spans = root.querySelectorAll(TOKEN_SELECTOR);
        const span = spans[token];
        if (!span) {
            return null;
        }
        const textNode = span.firstChild;
        if (!textNode) {
            return null;
        }
        const length = textNode.textContent.length;
        const from = Math.min(start, length);
        const to = Math.min(end, length);
        if (to <= from) {
            return null;
        }
        const range = document.createRange();
        range.setStart(textNode, from);
        range.setEnd(textNode, to);
        return range;
    }

    // Zerlegt die Server-Zahlenfolge "tokenIndex,start,end,…" in ein Zahlen-Array. Leerer/fehlender
    // Wert (keine Treffer) ergibt ein leeres Array – split("") lieferte sonst [""] -> [NaN].
    function parseFlat(csv) {
        if (!csv) {
            return [];
        }
        return csv.split(SEPARATOR).map(Number);
    }

    function currentFromFlat(flat, index) {
        if (index < 0) {
            return null;
        }
        const base = index * VALUES_PER_MATCH;
        if (base + VALUES_PER_MATCH > flat.length) {
            return null;
        }
        return { token: flat[base], start: flat[base + 1], end: flat[base + 2] };
    }

    // Entfernt Einträge, deren Wurzel-Element nicht mehr im Dokument hängt (z. B. nach View-Wechsel/
    // Detach der Komponente). Ohne diese Bereinigung hielten die Modul-Maps das detachte DOM-Subtree
    // dauerhaft fest -> Speicher-Leak in langlebigen Single-Page-Apps mit vielen Viewer-Instanzen.
    function pruneDetachedRoots() {
        for (const root of Array.from(matchesByRoot.keys())) {
            if (!root.isConnected) {
                matchesByRoot.delete(root);
                currentByRoot.delete(root);
            }
        }
    }

    function rebuild() {
        if (!isSupported()) {
            return;
        }
        pruneDetachedRoots();
        const matchHighlight = new Highlight();
        for (const [root, flat] of matchesByRoot) {
            for (let i = 0; i + VALUES_PER_MATCH <= flat.length; i += VALUES_PER_MATCH) {
                const range = rangeFor(root, flat[i], flat[i + 1], flat[i + 2]);
                if (range) {
                    matchHighlight.add(range);
                }
            }
        }
        CSS.highlights.set(MATCH_HIGHLIGHT, matchHighlight);

        // Der aktuelle Treffer wird zuletzt registriert und überzeichnet so den Match-Highlight.
        const currentHighlight = new Highlight();
        for (const [root, current] of currentByRoot) {
            if (current) {
                const range = rangeFor(root, current.token, current.start, current.end);
                if (range) {
                    currentHighlight.add(range);
                }
            }
        }
        CSS.highlights.set(CURRENT_HIGHLIGHT, currentHighlight);
    }

    function scrollToCurrent(root, current) {
        if (!current) {
            return;
        }
        const span = root.querySelectorAll(TOKEN_SELECTOR)[current.token];
        if (span) {
            span.scrollIntoView({ block: "center" });
        }
    }

    window.SearchHighlighter = {
        apply(root, flat, currentIndex) {
            const numbers = parseFlat(flat);
            matchesByRoot.set(root, numbers);
            const current = currentFromFlat(numbers, currentIndex);
            currentByRoot.set(root, current);
            rebuild();
            scrollToCurrent(root, current);
        },
        moveCurrent(root, currentIndex) {
            const numbers = matchesByRoot.get(root) || [];
            const current = currentFromFlat(numbers, currentIndex);
            currentByRoot.set(root, current);
            rebuild();
            scrollToCurrent(root, current);
        },
        clear(root) {
            matchesByRoot.delete(root);
            currentByRoot.delete(root);
            rebuild();
        },
    };
})();
