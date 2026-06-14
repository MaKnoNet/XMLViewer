/*
 * Frontend-Glue für den CodeViewer: kapselt einen read-only CodeMirror-6-Editor pro Host-Element.
 *
 * Die Java-Komponente steuert den Editor über window.MaknoCodeViewer.<fn>(this, …); der EditorView
 * lebt ausschließlich hier (clientseitig). Die Suche wird selbst gesteuert (kein CM6-Suchpanel),
 * damit die externe SearchNavigator-UI sie über MatchNavigable bedienen kann: Treffer werden via
 * SearchCursor gezählt, als Decorations markiert und der Stand per host.$server.onMatchChange(count,
 * index) an den Server zurückgemeldet.
 */
import { Compartment, EditorState, StateEffect, StateField } from "@codemirror/state";
import { Decoration, EditorView, lineNumbers } from "@codemirror/view";
import {
    StreamLanguage,
    bracketMatching,
    codeFolding,
    defaultHighlightStyle,
    foldAll as cmFoldAll,
    foldGutter,
    syntaxHighlighting,
    unfoldAll as cmUnfoldAll,
} from "@codemirror/language";
import { SearchCursor } from "@codemirror/search";
import { oneDark } from "@codemirror/theme-one-dark";
import { java } from "@codemirror/lang-java";
import { python } from "@codemirror/lang-python";
import { json } from "@codemirror/lang-json";
import { html } from "@codemirror/lang-html";
import { css } from "@codemirror/lang-css";
import { javascript } from "@codemirror/lang-javascript";
import { xml } from "@codemirror/lang-xml";
import { yaml } from "@codemirror/lang-yaml";
import { sql } from "@codemirror/lang-sql";
import { csharp } from "@codemirror/legacy-modes/mode/clike";

// Host-Element -> { view, Compartments, matches, current, caseSensitive }
const editors = new Map();

// Suchtreffer-Decorations: alle Treffer + der aktuelle (eigene Marker, da wir die Suche selbst führen).
const setMatches = StateEffect.define();
const matchMark = Decoration.mark({ class: "cm-search-match" });
const currentMark = Decoration.mark({ class: "cm-search-current" });
const searchField = StateField.define({
    create() {
        return Decoration.none;
    },
    update(deco, tr) {
        deco = deco.map(tr.changes);
        for (const effect of tr.effects) {
            if (effect.is(setMatches)) {
                const { ranges, current } = effect.value;
                deco = Decoration.set(
                    ranges.map((r, i) => (i === current ? currentMark : matchMark).range(r.from, r.to)),
                    true,
                );
            }
        }
        return deco;
    },
    provide: (f) => EditorView.decorations.from(f),
});

function languageExtension(id) {
    switch (id) {
        case "java":
            return java();
        case "python":
            return python();
        case "json":
            return json();
        case "html":
            return html();
        case "css":
            return css();
        case "javascript":
            return javascript();
        case "xml":
            return xml();
        case "yaml":
            return yaml();
        case "sql":
            return sql();
        case "csharp":
            return StreamLanguage.define(csharp);
        default:
            return [];
    }
}

function buildExtensions(entry, langId, dark, wrap, lineNumbersOn) {
    return [
        EditorState.readOnly.of(true),
        EditorView.editable.of(false),
        codeFolding(),
        foldGutter(),
        bracketMatching(),
        syntaxHighlighting(defaultHighlightStyle, { fallback: true }),
        searchField,
        entry.lineNumbersConf.of(lineNumbersOn ? lineNumbers() : []),
        entry.languageConf.of(languageExtension(langId)),
        entry.themeConf.of(dark ? oneDark : []),
        entry.wrapConf.of(wrap ? EditorView.lineWrapping : []),
    ];
}

// Entfernt Editoren, deren Host nicht mehr im Dokument hängt (View-Wechsel/Detach) -> kein Leak.
function prune() {
    for (const host of Array.from(editors.keys())) {
        if (!host.isConnected) {
            destroyEntry(host);
        }
    }
}

function destroyEntry(host) {
    const entry = editors.get(host);
    if (entry) {
        try {
            entry.view.destroy();
        } catch (ignored) {
            /* bereits zerstört */
        }
        editors.delete(host);
    }
}

function collectMatches(view, query, caseSensitive) {
    const found = [];
    if (query) {
        const normalize = caseSensitive ? undefined : (s) => s.toLowerCase();
        const cursor = new SearchCursor(view.state.doc, query, 0, view.state.doc.length, normalize);
        while (!cursor.next().done) {
            found.push({ from: cursor.value.from, to: cursor.value.to });
        }
    }
    return found;
}

function applyMatches(entry) {
    const effects = [setMatches.of({ ranges: entry.matches, current: entry.current })];
    if (entry.current >= 0) {
        const match = entry.matches[entry.current];
        effects.push(EditorView.scrollIntoView(match.from, { y: "center" }));
        entry.view.dispatch({ selection: { anchor: match.from, head: match.to }, effects });
    } else {
        entry.view.dispatch({ effects });
    }
}

function report(host, entry) {
    if (host.$server && typeof host.$server.onMatchChange === "function") {
        host.$server.onMatchChange(entry.matches.length, entry.current);
    }
}

window.MaknoCodeViewer = {
    create(host, text, langId, dark, wrap, lineNumbersOn) {
        prune();
        destroyEntry(host);
        const entry = {
            languageConf: new Compartment(),
            themeConf: new Compartment(),
            wrapConf: new Compartment(),
            lineNumbersConf: new Compartment(),
            matches: [],
            current: -1,
            caseSensitive: false,
        };
        const state = EditorState.create({
            doc: text || "",
            extensions: buildExtensions(entry, langId, dark, wrap, lineNumbersOn),
        });
        entry.view = new EditorView({ state, parent: host });
        editors.set(host, entry);
    },

    destroy(host) {
        destroyEntry(host);
    },

    setDoc(host, text, langId) {
        const entry = editors.get(host);
        if (!entry) {
            return;
        }
        entry.matches = [];
        entry.current = -1;
        entry.view.dispatch({
            changes: { from: 0, to: entry.view.state.doc.length, insert: text || "" },
            effects: [
                entry.languageConf.reconfigure(languageExtension(langId)),
                setMatches.of({ ranges: [], current: -1 }),
            ],
        });
    },

    setLanguage(host, langId) {
        const entry = editors.get(host);
        if (entry) {
            entry.view.dispatch({ effects: entry.languageConf.reconfigure(languageExtension(langId)) });
        }
    },

    setTheme(host, dark) {
        const entry = editors.get(host);
        if (entry) {
            entry.view.dispatch({ effects: entry.themeConf.reconfigure(dark ? oneDark : []) });
        }
    },

    setWrap(host, wrap) {
        const entry = editors.get(host);
        if (entry) {
            entry.view.dispatch({ effects: entry.wrapConf.reconfigure(wrap ? EditorView.lineWrapping : []) });
        }
    },

    setLineNumbers(host, on) {
        const entry = editors.get(host);
        if (entry) {
            entry.view.dispatch({ effects: entry.lineNumbersConf.reconfigure(on ? lineNumbers() : []) });
        }
    },

    foldAll(host) {
        const entry = editors.get(host);
        if (entry) {
            cmFoldAll(entry.view);
        }
    },

    unfoldAll(host) {
        const entry = editors.get(host);
        if (entry) {
            cmUnfoldAll(entry.view);
        }
    },

    search(host, query, caseSensitive) {
        const entry = editors.get(host);
        if (!entry) {
            return;
        }
        entry.caseSensitive = caseSensitive;
        entry.matches = collectMatches(entry.view, query, caseSensitive);
        entry.current = entry.matches.length ? 0 : -1;
        applyMatches(entry);
        report(host, entry);
    },

    move(host, delta) {
        const entry = editors.get(host);
        if (!entry || entry.matches.length === 0) {
            return;
        }
        entry.current = (entry.current + delta + entry.matches.length) % entry.matches.length;
        applyMatches(entry);
        report(host, entry);
    },

    clearSearch(host) {
        const entry = editors.get(host);
        if (!entry) {
            return;
        }
        entry.matches = [];
        entry.current = -1;
        entry.view.dispatch({ effects: setMatches.of({ ranges: [], current: -1 }) });
        report(host, entry);
    },
};
