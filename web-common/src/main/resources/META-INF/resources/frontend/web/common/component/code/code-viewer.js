/*
 * Frontend-Glue für den CodeViewer: kapselt einen read-only CodeMirror-6-Editor pro Host-Element.
 *
 * Der Editor wird in einem Shadow-Root der Host-Komponente gemountet. Das isoliert CodeMirrors per
 * style-mod injizierte Styles vom Style-Management der einbettenden Vaadin-App (im Light-DOM gehen die
 * CM6-Basis-Styles sonst verloren -> kaputtes Layout). CSS Custom Properties vererben durch die
 * Shadow-Grenze, daher bleiben die Trefferfarben über --codeviewer-search-*-bg von außen anpassbar.
 *
 * Die Java-Komponente steuert den Editor über window.MaknoCodeViewer.<fn>(this, …); der EditorView
 * lebt ausschließlich hier (clientseitig). Die Suche wird selbst gesteuert (kein CM6-Suchpanel),
 * damit die externe SearchNavigator-UI sie über MatchNavigable bedienen kann: Treffer werden via
 * SearchCursor gezählt, als Decorations markiert und der Stand per host.$server.onMatchChange(count,
 * index) an den Server zurückgemeldet.
 */
import { Compartment, EditorState, RangeSetBuilder, StateEffect, StateField } from "@codemirror/state";
import { Decoration, EditorView, ViewPlugin, WidgetType, lineNumbers } from "@codemirror/view";
import {
    StreamLanguage,
    bracketMatching,
    codeFolding,
    defaultHighlightStyle,
    foldAll as cmFoldAll,
    foldEffect,
    foldService,
    foldable,
    foldedRanges,
    syntaxHighlighting,
    unfoldAll as cmUnfoldAll,
    unfoldEffect,
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

// Host-Element -> { root, view, Compartments, matches, current, caseSensitive }
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

// Falt-„Connected-Tree" im XmlViewer-Look (vier SVGs als Bild): Kopf-offen (Quadrat-Minus mit
// Linien-Stummel nach unten), Kopf-zu (Quadrat-Plus), durchgehende Linie und End-Elbow „└". Je eine
// Variante für hell (Strich #64748b, weiße Quadrat-Füllung) und dunkel (Strich #94a3b8, ohne Füllung);
// umgeschaltet über die CM6-Selektoren &light/&dark. Alle vier nutzen viewBox 24×36, stroke-width 2
// und werden mit background-size: auto 100% gerendert → gleiche Strichstärke und gleiche Farbe für
// Marker, Stummel, Elbow und Linie; die Linie (y=0..36) füllt die volle Zellhöhe und läuft so
// lückenlos über die Zeilen hinweg.
const SVG_OPEN_LIGHT = `url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 36' fill='none' stroke='%2364748b' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3E%3Cline x1='12' y1='27' x2='12' y2='36'/%3E%3Crect x='3' y='9' width='18' height='18' rx='2' ry='2' fill='%23ffffff'/%3E%3Cline x1='8' y1='18' x2='16' y2='18'/%3E%3C/svg%3E")`;
const SVG_OPEN_DARK = `url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 36' fill='none' stroke='%2394a3b8' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3E%3Cline x1='12' y1='27' x2='12' y2='36'/%3E%3Crect x='3' y='9' width='18' height='18' rx='2' ry='2'/%3E%3Cline x1='8' y1='18' x2='16' y2='18'/%3E%3C/svg%3E")`;
const SVG_CLOSED_LIGHT = `url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 36' fill='none' stroke='%2364748b' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3E%3Crect x='3' y='9' width='18' height='18' rx='2' ry='2'/%3E%3Cline x1='12' y1='14' x2='12' y2='22'/%3E%3Cline x1='8' y1='18' x2='16' y2='18'/%3E%3C/svg%3E")`;
const SVG_CLOSED_DARK = `url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 36' fill='none' stroke='%2394a3b8' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3E%3Crect x='3' y='9' width='18' height='18' rx='2' ry='2'/%3E%3Cline x1='12' y1='14' x2='12' y2='22'/%3E%3Cline x1='8' y1='18' x2='16' y2='18'/%3E%3C/svg%3E")`;
const SVG_LINE_LIGHT = `url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 36' fill='none' stroke='%2364748b' stroke-width='2' stroke-linecap='round'%3E%3Cline x1='12' y1='0' x2='12' y2='36'/%3E%3C/svg%3E")`;
const SVG_LINE_DARK = `url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 36' fill='none' stroke='%2394a3b8' stroke-width='2' stroke-linecap='round'%3E%3Cline x1='12' y1='0' x2='12' y2='36'/%3E%3C/svg%3E")`;
const SVG_END_LIGHT = `url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 36' fill='none' stroke='%2364748b' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3E%3Cpath d='M12 0 L12 18 L24 18'/%3E%3C/svg%3E")`;
const SVG_END_DARK = `url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 36' fill='none' stroke='%2394a3b8' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3E%3Cpath d='M12 0 L12 18 L24 18'/%3E%3C/svg%3E")`;

// Führende Whitespace-Breite einer Zeile in Spalten (Tabs auf die Tab-Weite aufgerundet) – bestimmt,
// an welcher Einrückungs-Spalte die Baum-Linie der Region läuft.
function leadingColumns(text, tabSize) {
    let col = 0;
    for (let i = 0; i < text.length; i++) {
        const ch = text[i];
        if (ch === " ") {
            col += 1;
        } else if (ch === "\t") {
            col += tabSize - (col % tabSize);
        } else {
            break;
        }
    }
    return col;
}

// Faltbare, mehrzeilige Regionen je Dokument-Stand: Kopfzeile, letzte Zeile, Einrückungs-Spalte und
// Falt-Range. Zusätzlich pro Zeile die Liste aller umschließenden Regionen – daraus entstehen die
// parallel nebeneinander laufenden Ebenen-Linien (eingerückter Baum wie beim XmlViewer).
function computeFoldRegions(state) {
    const doc = state.doc;
    const tabSize = state.tabSize;
    const regions = [];
    for (let n = 1; n <= doc.lines; n++) {
        const line = doc.line(n);
        const range = foldable(state, line.from, line.to);
        if (!range) {
            continue;
        }
        let lastLine = doc.lineAt(range.to).number;
        // Einrückungsbasierte Fold-Bereiche (z. B. Python) enden oft auf einer Leerzeile; das Elbow
        // soll aber auf der letzten Inhaltszeile sitzen, nicht im Leeren → nachlaufende Leerzeilen
        // abschneiden.
        while (lastLine > n && doc.line(lastLine).text.trim() === "") {
            lastLine--;
        }
        if (lastLine <= n) {
            continue; // einzeilige (oder leer-getrimmte) Faltung – kein Baum zu zeichnen
        }
        // Nur echte verschachtelte Blöcke als Baum zeichnen: die erste Inhaltszeile muss tiefer
        // eingerückt sein als der Kopf. Sonst entstünde ein Baum aus Geschwistern – etwa bei einer
        // flachen YAML-Sequenz („- eins / - zwei / - drei"), deren Einträge auf gleicher Ebene liegen.
        const headIndent = leadingColumns(line.text, tabSize);
        let bodyLine = n + 1;
        while (bodyLine <= lastLine && doc.line(bodyLine).text.trim() === "") {
            bodyLine++;
        }
        if (bodyLine > lastLine || leadingColumns(doc.line(bodyLine).text, tabSize) <= headIndent) {
            continue;
        }
        regions.push({ headLine: n, lastLine, indent: headIndent, range });
    }
    const coverers = new Map();
    for (const region of regions) {
        for (let m = region.headLine; m <= region.lastLine; m++) {
            let list = coverers.get(m);
            if (!list) {
                list = [];
                coverers.set(m, list);
            }
            list.push(region);
        }
    }
    return { coverers };
}

// Memoisiert die Regionen am State, damit pro Render-Zyklus nur einmal über das Dokument gescannt wird
// (Scrollen ändert den State nicht → Cache bleibt gültig).
function foldRegionsFor(entry, state) {
    if (entry.foldRegionsState !== state) {
        entry.foldRegionsState = state;
        entry.foldRegions = computeFoldRegions(state);
    }
    return entry.foldRegions;
}

function isFolded(state, range) {
    let folded = false;
    foldedRanges(state).between(range.from, range.from, (a, b) => {
        if (a === range.from && b === range.to) {
            folded = true;
        }
    });
    return folded;
}

function toggleRegion(view, range) {
    view.dispatch({ effects: (isFolded(view.state, range) ? unfoldEffect : foldEffect).of(range) });
}

// Inline-Widget am Zeilenanfang: zeichnet pro umschließender Region eine absolut positionierte Zelle
// (Marker/Linie/Elbow) an deren Einrückungs-Spalte. Mehrere Ebenen → mehrere Zellen nebeneinander =
// der eingerückte „Connected-Tree". Das Bild je Zelle liefert markerBaseTheme über den Klassennamen.
class FoldTreeWidget extends WidgetType {
    constructor(cells) {
        super();
        this.cells = cells; // [{ kind, indent, range }]
    }
    eq(other) {
        return (
            other.cells.length === this.cells.length &&
            this.cells.every((c, i) => c.kind === other.cells[i].kind && c.indent === other.cells[i].indent)
        );
    }
    toDOM(view) {
        const wrap = document.createElement("span");
        wrap.className = "cm-ftree";
        wrap.setAttribute("aria-hidden", "true");
        for (const cell of this.cells) {
            const el = document.createElement("span");
            el.className = "cm-ftree-cell cm-ftree-" + cell.kind;
            el.style.left = `calc(${cell.indent}ch - var(--ftree-back))`;
            if (cell.kind === "open" || cell.kind === "closed") {
                el.addEventListener("mousedown", (event) => {
                    event.preventDefault();
                    event.stopPropagation();
                    toggleRegion(view, cell.range);
                });
            }
            wrap.appendChild(el);
        }
        return wrap;
    }
    ignoreEvent() {
        return true; // Marker-Klicks behandeln wir selbst; CM soll daraus keine Cursor-Bewegung machen.
    }
}

// Baut die Baum-Dekorationen nur für die sichtbaren Zeilen (out-of-core bei großen Dateien).
function buildFoldTreeDeco(view, entry) {
    const { coverers } = foldRegionsFor(entry, view.state);
    const doc = view.state.doc;
    const builder = new RangeSetBuilder();
    for (const { from, to } of view.visibleRanges) {
        for (let pos = from; pos <= to; ) {
            const line = doc.lineAt(pos);
            const regions = coverers.get(line.number);
            if (regions && regions.length) {
                const cells = [];
                for (const region of regions) {
                    if (line.number === region.headLine) {
                        cells.push({
                            kind: isFolded(view.state, region.range) ? "closed" : "open",
                            indent: region.indent,
                            range: region.range,
                        });
                    } else if (!isFolded(view.state, region.range)) {
                        // Eine gefaltete Region zeigt nur ihren Kopf-Marker. Ihre noch sichtbare
                        // schließende Zeile bekäme sonst ein „schwebendes" Elbow ohne Linie darüber
                        // (die ist ja eingeklappt) – also Linie/Elbow gefalteter Regionen weglassen.
                        cells.push({
                            kind: line.number === region.lastLine ? "end" : "line",
                            indent: region.indent,
                            range: region.range,
                        });
                    }
                }
                if (cells.length) {
                    builder.add(line.from, line.from, Decoration.widget({ widget: new FoldTreeWidget(cells), side: -1 }));
                }
            }
            pos = line.to + 1;
        }
    }
    return builder.finish();
}

// ViewPlugin, das den eingerückten Falt-Baum als Inhalts-Overlay hält. Pro Editor instanziiert, da die
// Regionen am entry memoisiert werden. Neu aufgebaut bei Doc-, Viewport- oder Faltzustands-Änderung.
function foldTreePlugin(entry) {
    return ViewPlugin.fromClass(
        class {
            constructor(view) {
                this.decorations = buildFoldTreeDeco(view, entry);
            }
            update(update) {
                if (
                    update.docChanged ||
                    update.viewportChanged ||
                    foldedRanges(update.startState) !== foldedRanges(update.state)
                ) {
                    this.decorations = buildFoldTreeDeco(update.view, entry);
                }
            }
        },
        { decorations: (plugin) => plugin.decorations },
    );
}

// Host-Theme: füllt den Host, setzt Monospace, Trefferfarben und die Geometrie des eingerückten
// Falt-Baums (Inhalts-Overlay). Wird per style-mod in den Shadow-Root injiziert.
//   --ftree-back: wie weit links der Textspalte die Ebenen-Linie/der Marker sitzt (in der Einrückung).
//   --ftree-cellw: Breite der Marker-/Linien-/Elbow-Zelle (an der Zeilenhöhe orientiert).
//   --ftree-pad:  linker Inhalts-Innenabstand, damit die äußerste Ebene (Einrückung 0) Platz hat.
const hostTheme = EditorView.theme({
    "&": {
        flex: "1 1 auto",
        minHeight: "0",
        "--ftree-back": "0.55ch",
        "--ftree-cellw": "1.1em",
        "--ftree-pad": "1.5ch",
    },
    ".cm-scroller": { fontFamily: "ui-monospace, 'Cascadia Code', 'Consolas', monospace" },
    ".cm-search-match": { backgroundColor: "var(--codeviewer-search-match-bg, #ffd9a3)" },
    ".cm-search-current": { backgroundColor: "var(--codeviewer-search-current-bg, #ff9d4d)" },
    // Platz links für die Marker der äußersten Ebene; der Baum selbst läuft im Inhalt (eingerückt).
    ".cm-content": { paddingLeft: "var(--ftree-pad)" },
    ".cm-line": { position: "relative" },
    // Nullbreite-Anker am Zeilenanfang über die volle Zeilenhöhe; trägt die absolut gesetzten Zellen.
    ".cm-ftree": { position: "absolute", left: "0", top: "0", height: "100%", width: "0" },
    ".cm-ftree-cell": {
        position: "absolute",
        top: "0",
        height: "100%",
        width: "var(--ftree-cellw)",
        transform: "translateX(-50%)",
        backgroundPosition: "center",
        backgroundRepeat: "no-repeat",
        pointerEvents: "none",
    },
    // Alle vier identisch skaliert (auto 100% = seitenverhältnistreu, mittig) → Linie, Stummel und
    // Elbow haben dieselbe Strichstärke; die Linie (viewBox 24×36, volle Höhe) läuft nahtlos durch.
    ".cm-ftree-line": { backgroundSize: "auto 100%" },
    ".cm-ftree-open": { backgroundSize: "auto 100%", cursor: "pointer", pointerEvents: "auto" },
    ".cm-ftree-closed": { backgroundSize: "auto 100%", cursor: "pointer", pointerEvents: "auto" },
    ".cm-ftree-end": { backgroundSize: "auto 100%" },
});

// Theme-abhängige Marker-Bilder. Die Selektoren &light/&dark sind nur in baseTheme erlaubt
// (nicht in EditorView.theme); sie schalten automatisch mit dem aktiven Editor-Theme um.
const markerBaseTheme = EditorView.baseTheme({
    "&light .cm-ftree-open": { backgroundImage: SVG_OPEN_LIGHT },
    "&light .cm-ftree-closed": { backgroundImage: SVG_CLOSED_LIGHT },
    "&light .cm-ftree-line": { backgroundImage: SVG_LINE_LIGHT },
    "&light .cm-ftree-end": { backgroundImage: SVG_END_LIGHT },
    "&dark .cm-ftree-open": { backgroundImage: SVG_OPEN_DARK },
    "&dark .cm-ftree-closed": { backgroundImage: SVG_CLOSED_DARK },
    "&dark .cm-ftree-line": { backgroundImage: SVG_LINE_DARK },
    "&dark .cm-ftree-end": { backgroundImage: SVG_END_DARK },
});

// Klammer-basierte Faltung für StreamLanguage-Modi (C#), die keinen Lezer-Faltbaum mitbringen: faltet
// vom letzten '{' einer Zeile bis zur passenden '}'. Best-effort – ohne String-/Kommentar-Analyse,
// für die Quelltext-Vorschau ausreichend. Nur dort aktiv, wo unten ausdrücklich eingehängt.
const MAX_BRACE_SCAN = 100000; // Schutz gegen Worst-Case-Vorwärtssuche bei sehr großen Dateien
const braceFoldService = foldService.of((state, lineStart, lineEnd) => {
    const lineText = state.doc.sliceString(lineStart, lineEnd);
    const open = lineText.lastIndexOf("{");
    if (open < 0) {
        return null;
    }
    const from = lineStart + open + 1;
    const text = state.doc.sliceString(from, Math.min(state.doc.length, from + MAX_BRACE_SCAN));
    let depth = 1;
    for (let i = 0; i < text.length; i++) {
        const ch = text[i];
        if (ch === "{") {
            depth += 1;
        } else if (ch === "}") {
            depth -= 1;
            if (depth === 0) {
                return i > 0 ? { from, to: from + i } : null;
            }
        }
    }
    return null;
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
            // Legacy-Modus ohne Lezer-Faltbaum → Klammer-Faltung ergänzen.
            return [StreamLanguage.define(csharp), braceFoldService];
        default:
            return [];
    }
}

function buildExtensions(entry, langId, dark, wrap, lineNumbersOn) {
    return [
        EditorState.readOnly.of(true),
        EditorView.editable.of(false),
        codeFolding(),
        // Zeilennummern-Gutter bleibt links; der Falt-Baum läuft als eingerücktes Inhalts-Overlay mit
        // der Code-Einrückung mit (Marker/Linie/Elbow pro Ebene) – Reihenfolge bleibt „Zahl, dann Baum".
        entry.lineNumbersConf.of(lineNumbersOn ? lineNumbers() : []),
        foldTreePlugin(entry),
        bracketMatching(),
        syntaxHighlighting(defaultHighlightStyle, { fallback: true }),
        searchField,
        hostTheme,
        markerBaseTheme,
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
        // Shadow-Root isoliert die CodeMirror-Styles von Vaadins Style-Verwaltung.
        const root = host.shadowRoot || host.attachShadow({ mode: "open" });
        const entry = {
            root,
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
        entry.view = new EditorView({ state, parent: root, root });
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
