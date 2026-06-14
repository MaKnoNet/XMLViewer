package de.makno.web.common.component.code;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Best-effort-Erkennung der Sprache eines Quelltexts – über Datei-Endung oder Inhalts-Heuristik.
 * CodeMirror 6 erkennt Sprachen nicht selbst; diese Heuristik liefert die Vorauswahl, wenn der
 * Aufrufer keine Sprache explizit setzt. Bewusst einfach gehalten (kein ML, keine vollständige
 * Lexer-Analyse); im Zweifel {@link CodeLanguage#PLAIN}. Reines Java, ohne UI – damit headless testbar.
 */
public final class CodeLanguageDetector {

    private static final Pattern SQL_START =
            Pattern.compile("^\\s*(SELECT|INSERT|UPDATE|DELETE|CREATE|ALTER|DROP|WITH)\\b", Pattern.CASE_INSENSITIVE);
    private static final Pattern PYTHON =
            Pattern.compile("(?m)^\\s*(def\\s+\\w+\\s*\\(|elif\\s|class\\s+\\w+[^{]*:\\s*$|from\\s+\\S+\\s+import\\s)");
    private static final Pattern CSS_RULE = Pattern.compile("[.#]?[\\w-]+\\s*\\{[^{}]*:[^{}]*;[^{}]*}");
    private static final Pattern YAML_KEY = Pattern.compile("(?m)^\\s*[\\w.-]+:\\s+\\S");

    private CodeLanguageDetector() {}

    /** Erkennt die Sprache anhand der Datei-Endung; {@link CodeLanguage#PLAIN}, wenn unbekannt. */
    public static CodeLanguage fromFileName(String fileName) {
        if (fileName == null) {
            return CodeLanguage.PLAIN;
        }
        int dot = fileName.lastIndexOf('.');
        if (dot < 0 || dot == fileName.length() - 1) {
            return CodeLanguage.PLAIN;
        }
        String extension = fileName.substring(dot + 1).toLowerCase(Locale.ROOT);
        for (CodeLanguage language : CodeLanguage.values()) {
            if (language.extensions().contains(extension)) {
                return language;
            }
        }
        return CodeLanguage.PLAIN;
    }

    /**
     * Erkennt die Sprache anhand markanter Inhaltsmuster (geordnete Regeln, erste Übereinstimmung
     * gewinnt); {@link CodeLanguage#PLAIN}, wenn keine Regel greift.
     */
    public static CodeLanguage fromContent(String text) {
        if (text == null || text.isBlank()) {
            return CodeLanguage.PLAIN;
        }
        String head = text.stripLeading();
        String lower = head.toLowerCase(Locale.ROOT);

        if (lower.startsWith("<?xml")) {
            return CodeLanguage.XML;
        }
        if (containsAny(lower, "<!doctype html", "<html", "<head", "<body", "<div")) {
            return CodeLanguage.HTML;
        }
        if (head.startsWith("<")) {
            return CodeLanguage.XML;
        }
        if (head.startsWith("{") || head.startsWith("[")) {
            return CodeLanguage.JSON;
        }
        if (SQL_START.matcher(head).find()) {
            return CodeLanguage.SQL;
        }
        if (PYTHON.matcher(text).find()) {
            return CodeLanguage.PYTHON;
        }
        if (containsAny(text, "using System", "namespace ", "Console.WriteLine")) {
            return CodeLanguage.CSHARP;
        }
        if (containsAny(text, "public class", "package ", "System.out.", "void main")) {
            return CodeLanguage.JAVA;
        }
        if (containsAny(text, "function ", "=>", "const ", "console.log", "document.")) {
            return CodeLanguage.JAVASCRIPT;
        }
        if (CSS_RULE.matcher(text).find()) {
            return CodeLanguage.CSS;
        }
        if (YAML_KEY.matcher(text).find()) {
            return CodeLanguage.YAML;
        }
        return CodeLanguage.PLAIN;
    }

    private static boolean containsAny(String text, String... needles) {
        for (String needle : needles) {
            if (text.contains(needle)) {
                return true;
            }
        }
        return false;
    }
}
