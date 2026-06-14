package de.makno.web.common.component.code;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.shared.Registration;
import de.makno.web.common.component.navigation.MatchChangeEvent;
import de.makno.web.common.component.navigation.MatchNavigable;
import java.io.Serializable;

/**
 * Read-only Quelltext-Ansicht für viele text-basierte Formate (Java, C#, Python, JSON, YAML, HTML,
 * CSS, JS, XML, SQL …) – ein dünner Vaadin-Wrapper um den Editor <a
 * href="https://codemirror.net/">CodeMirror 6</a>. Liefert <strong>Syntax-Highlighting</strong>,
 * <strong>sprachgenaues Code-Falten</strong>, <strong>hell/dunkel</strong>-Theme, Zeilennummern und
 * umschaltbaren Zeilenumbruch direkt aus der Bibliothek.
 *
 * <p>Die Such-UI bringt die Komponente bewusst <strong>nicht</strong> selbst mit: Sie implementiert
 * {@link MatchNavigable} und wird damit – wie {@code XmlViewer}/{@code TextViewer} – von der
 * eigenständigen {@code SearchNavigator}-Komponente gesteuert. Die Suche selbst führt CodeMirror aus;
 * der CodeViewer spiegelt nur Trefferanzahl/-index serverseitig und feuert {@link MatchChangeEvent}.
 *
 * <h2>Architektur</h2>
 * Der CodeMirror-{@code EditorView} lebt ausschließlich <strong>clientseitig</strong>; serverseitig
 * hält die Komponente nur den (serialisierbaren) Zustand (Text, Sprache, Theme, Optionen,
 * Treffer-Stand). Bei jedem {@code onAttach} wird der Editor aus diesem Zustand neu aufgebaut – damit
 * ist die Komponente serialisierbar (Session-Clustering) und übersteht Re-Attach.
 *
 * <h2>Thread-Safety</h2>
 * <strong>Nicht thread-safe</strong> – wie jede Vaadin-Komponente an genau eine {@code UI}/Session
 * gebunden; Methoden nur aus dem Session-Thread aufrufen, eine Instanz pro UI.
 */
@NpmPackage(value = "@codemirror/state", version = "6.4.1")
@NpmPackage(value = "@codemirror/view", version = "6.26.3")
@NpmPackage(value = "@codemirror/language", version = "6.10.2")
@NpmPackage(value = "@codemirror/commands", version = "6.6.0")
@NpmPackage(value = "@codemirror/search", version = "6.5.6")
@NpmPackage(value = "@codemirror/theme-one-dark", version = "6.1.2")
@NpmPackage(value = "@codemirror/legacy-modes", version = "6.4.0")
@NpmPackage(value = "@codemirror/lang-java", version = "6.0.1")
@NpmPackage(value = "@codemirror/lang-python", version = "6.1.6")
@NpmPackage(value = "@codemirror/lang-json", version = "6.0.1")
@NpmPackage(value = "@codemirror/lang-html", version = "6.4.9")
@NpmPackage(value = "@codemirror/lang-css", version = "6.2.1")
@NpmPackage(value = "@codemirror/lang-javascript", version = "6.2.2")
@NpmPackage(value = "@codemirror/lang-xml", version = "6.1.0")
@NpmPackage(value = "@codemirror/lang-yaml", version = "6.1.1")
@NpmPackage(value = "@codemirror/lang-sql", version = "6.7.0")
@JsModule("./web/common/component/code/code-viewer.js")
@CssImport("./web/common/component/code/styles/code-viewer.css")
public class CodeViewer extends Div implements MatchNavigable {

    private static final long serialVersionUID = 1L;

    private String text = "";
    private CodeLanguage language; // null = Auto-Erkennung aus dem Inhalt
    private boolean dark = false;
    private boolean wrap = false;
    private boolean showLineNumbers = true;
    private boolean searchCaseSensitive = false;

    // Vom Client (CodeMirror) gespiegelter Treffer-Stand – Single Source of Truth für die Getter.
    private int matchCount = 0;
    private int currentMatchIndex = -1;

    public CodeViewer() {
        addClassName(CodeCssClasses.ROOT);
    }

    public CodeViewer(String text) {
        this();
        this.text = text == null ? "" : text;
    }

    public CodeViewer(String text, CodeLanguage language) {
        this(text);
        this.language = language;
    }

    // ------------------------------------------------------------------------
    // Lifecycle: Editor clientseitig auf-/abbauen
    // ------------------------------------------------------------------------

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        callJs("create", text, effectiveLanguageId(), dark, wrap, showLineNumbers);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Best-effort-Cleanup des clientseitigen Editors; die JS-Registry räumt detachte Hosts
        // ohnehin lazy auf (isConnected-Prüfung), falls dieser Aufruf beim Detach nicht mehr abgeht.
        getElement().executeJs("window.MaknoCodeViewer && window.MaknoCodeViewer.destroy(this)");
        super.onDetach(detachEvent);
    }

    // ------------------------------------------------------------------------
    // Inhalt & Sprache
    // ------------------------------------------------------------------------

    /** Setzt den anzuzeigenden Quelltext und baut die Ansicht neu. {@code null} = leerer Text. */
    public void setText(String text) {
        this.text = text == null ? "" : text;
        matchCount = 0;
        currentMatchIndex = -1;
        callJs("setDoc", this.text, effectiveLanguageId());
        fireSearchReset();
    }

    public String getText() {
        return text;
    }

    /** Setzt die Sprache explizit; {@code null} aktiviert wieder die Auto-Erkennung aus dem Inhalt. */
    public void setLanguage(CodeLanguage language) {
        this.language = language;
        callJs("setLanguage", effectiveLanguageId());
    }

    /** Leitet die Sprache aus der Datei-Endung ab (z.&nbsp;B. {@code "Foo.java"} → Java). */
    public void setLanguageFromFileName(String fileName) {
        setLanguage(CodeLanguageDetector.fromFileName(fileName));
    }

    /** Die explizit gesetzte Sprache, oder {@code null} bei Auto-Erkennung. */
    public CodeLanguage getLanguage() {
        return language;
    }

    private String effectiveLanguageId() {
        CodeLanguage resolved = language != null ? language : CodeLanguageDetector.fromContent(text);
        return resolved.cm6Id();
    }

    // ------------------------------------------------------------------------
    // Darstellung
    // ------------------------------------------------------------------------

    /** Schaltet zwischen hellem (Standard) und dunklem Theme um. */
    public void setDark(boolean dark) {
        this.dark = dark;
        callJs("setTheme", dark);
    }

    public boolean isDark() {
        return dark;
    }

    /** Schaltet den Zeilenumbruch um (Standard: aus → lange Zeilen scrollen horizontal). */
    public void setWrap(boolean wrap) {
        this.wrap = wrap;
        callJs("setWrap", wrap);
    }

    public boolean isWrap() {
        return wrap;
    }

    /** Blendet die Zeilennummern-Spalte ein/aus (Standard: ein). */
    public void setShowLineNumbers(boolean showLineNumbers) {
        this.showLineNumbers = showLineNumbers;
        callJs("setLineNumbers", showLineNumbers);
    }

    public boolean isShowLineNumbers() {
        return showLineNumbers;
    }

    /** Klappt alle faltbaren Blöcke zu. */
    public void foldAll() {
        callJs("foldAll");
    }

    /** Klappt alle Blöcke auf. */
    public void unfoldAll() {
        callJs("unfoldAll");
    }

    // ------------------------------------------------------------------------
    // Suche (über MatchNavigable; UI liegt im SearchNavigator)
    // ------------------------------------------------------------------------

    /**
     * Sucht {@code query} im Editor, markiert alle Treffer und springt zum ersten. Die Trefferzählung
     * meldet CodeMirror asynchron zurück (siehe {@link #onMatchChange(int, int)}); leerer Text löscht
     * die Suche.
     */
    @Override
    public void search(String query) {
        callJs("search", query == null ? "" : query, searchCaseSensitive);
    }

    /** Springt zum nächsten Suchtreffer (umlaufend). */
    @Override
    public void nextMatch() {
        callJs("move", 1);
    }

    /** Springt zum vorherigen Suchtreffer (umlaufend). */
    @Override
    public void previousMatch() {
        callJs("move", -1);
    }

    /** Entfernt alle Such-Markierungen. */
    public void clearSearch() {
        callJs("clearSearch");
    }

    /** Anzahl der aktuellen Suchtreffer (vom Client gespiegelt). */
    @Override
    public int getMatchCount() {
        return matchCount;
    }

    /** 0-basierter Index des aktuellen Treffers (vom Client gespiegelt), oder {@code -1}. */
    @Override
    public int getCurrentMatchIndex() {
        return currentMatchIndex;
    }

    /** Schaltet Groß-/Kleinschreibungsabgleich der Suche um (gilt ab der nächsten Suche; Standard: aus). */
    public void setSearchCaseSensitive(boolean caseSensitive) {
        this.searchCaseSensitive = caseSensitive;
    }

    public boolean isSearchCaseSensitive() {
        return searchCaseSensitive;
    }

    @Override
    public Registration addMatchChangeListener(ComponentEventListener<MatchChangeEvent> listener) {
        return addListener(MatchChangeEvent.class, listener);
    }

    /**
     * Vom Frontend-Glue aufgerufener Callback mit dem aktuellen Treffer-Stand. <strong>Interner
     * Framework-Hook</strong> (nicht direkt aufrufen): aktualisiert die gespiegelten Felder und feuert
     * {@link MatchChangeEvent}, sodass z.&nbsp;B. der {@code SearchNavigator} synchron bleibt.
     *
     * @param count Anzahl der Treffer
     * @param index 0-basierter Index des aktuellen Treffers, oder {@code -1}
     */
    @ClientCallable
    public void onMatchChange(int count, int index) {
        this.matchCount = count;
        this.currentMatchIndex = index;
        fireEvent(new MatchChangeEvent(this, count, index));
    }

    private void fireSearchReset() {
        fireEvent(new MatchChangeEvent(this, getMatchCount(), getCurrentMatchIndex(), true));
    }

    /**
     * Ruft eine Funktion des Frontend-Glues {@code window.MaknoCodeViewer} mit diesem Element als
     * erstem Argument auf – nur bei gebundener UI (im Unit-Test ohne Client wirkungslos).
     */
    private void callJs(String function, Serializable... args) {
        if (getUI().isEmpty()) {
            return;
        }
        StringBuilder js =
                new StringBuilder("window.MaknoCodeViewer.").append(function).append("(this");
        for (int i = 0; i < args.length; i++) {
            js.append(", $").append(i);
        }
        js.append(")");
        getElement().executeJs(js.toString(), args);
    }
}
