package de.makno.web.common.component.code;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit-Tests der best-effort-Spracherkennung – Endung und markante Inhaltsmuster (eindeutige Fälle).
 */
class CodeLanguageDetectorTest {

    @Test
    void erkenntSpracheAnDerDateiendung() {
        assertEquals(CodeLanguage.JAVA, CodeLanguageDetector.fromFileName("Foo.java"));
        assertEquals(CodeLanguage.CSHARP, CodeLanguageDetector.fromFileName("Program.cs"));
        assertEquals(CodeLanguage.PYTHON, CodeLanguageDetector.fromFileName("script.py"));
        assertEquals(CodeLanguage.JSON, CodeLanguageDetector.fromFileName("data.json"));
        assertEquals(CodeLanguage.YAML, CodeLanguageDetector.fromFileName("config.yml"));
        assertEquals(CodeLanguage.HTML, CodeLanguageDetector.fromFileName("index.html"));
        assertEquals(CodeLanguage.XML, CodeLanguageDetector.fromFileName("pom.xml"));
        assertEquals(CodeLanguage.CSS, CodeLanguageDetector.fromFileName("style.css"));
        assertEquals(CodeLanguage.JAVASCRIPT, CodeLanguageDetector.fromFileName("app.js"));
        assertEquals(CodeLanguage.SQL, CodeLanguageDetector.fromFileName("query.sql"));
    }

    @Test
    void liefertPlainBeiUnbekannterOderFehlenderEndung() {
        assertEquals(CodeLanguage.PLAIN, CodeLanguageDetector.fromFileName("README"));
        assertEquals(CodeLanguage.PLAIN, CodeLanguageDetector.fromFileName("notes.unknownext"));
        assertEquals(CodeLanguage.PLAIN, CodeLanguageDetector.fromFileName(null));
    }

    @Test
    void erkenntJavaAmInhalt() {
        String java = """
                package com.example;

                public class Foo {
                    public static void main(String[] args) {
                        System.out.println("hi");
                    }
                }
                """;
        assertEquals(CodeLanguage.JAVA, CodeLanguageDetector.fromContent(java));
    }

    @Test
    void erkenntCSharpAmInhalt() {
        String csharp = """
                using System;

                namespace Demo {
                    class Program {
                        static void Main() { Console.WriteLine("hi"); }
                    }
                }
                """;
        assertEquals(CodeLanguage.CSHARP, CodeLanguageDetector.fromContent(csharp));
    }

    @Test
    void erkenntPythonAmInhalt() {
        String python = """
                def greet(name):
                    return f"Hallo {name}"
                """;
        assertEquals(CodeLanguage.PYTHON, CodeLanguageDetector.fromContent(python));
    }

    @Test
    void erkenntJsonAmInhalt() {
        assertEquals(CodeLanguage.JSON, CodeLanguageDetector.fromContent("{\n  \"a\": 1,\n  \"b\": [2, 3]\n}"));
    }

    @Test
    void erkenntYamlAmInhalt() {
        String yaml = """
                name: Demo
                version: 1.0.0
                items:
                  - eins
                  - zwei
                """;
        assertEquals(CodeLanguage.YAML, CodeLanguageDetector.fromContent(yaml));
    }

    @Test
    void erkenntHtmlAmInhalt() {
        assertEquals(
                CodeLanguage.HTML,
                CodeLanguageDetector.fromContent("<!DOCTYPE html>\n<html><body><h1>Hi</h1></body></html>"));
    }

    @Test
    void erkenntXmlAmInhalt() {
        assertEquals(CodeLanguage.XML, CodeLanguageDetector.fromContent("<?xml version=\"1.0\"?>\n<root><a/></root>"));
    }

    @Test
    void erkenntSqlAmInhalt() {
        assertEquals(CodeLanguage.SQL, CodeLanguageDetector.fromContent("SELECT id, name FROM users WHERE id = 1;"));
    }

    @Test
    void erkenntCssAmInhalt() {
        assertEquals(CodeLanguage.CSS, CodeLanguageDetector.fromContent(".btn {\n  color: red;\n  padding: 4px;\n}"));
    }

    @Test
    void erkenntJavaScriptAmInhalt() {
        assertEquals(
                CodeLanguage.JAVASCRIPT,
                CodeLanguageDetector.fromContent("function hello() {\n  console.log('hi');\n}"));
    }

    @Test
    void liefertPlainBeiLeeremOderUnbekanntemInhalt() {
        assertEquals(CodeLanguage.PLAIN, CodeLanguageDetector.fromContent(""));
        assertEquals(CodeLanguage.PLAIN, CodeLanguageDetector.fromContent("   \n  "));
        assertEquals(CodeLanguage.PLAIN, CodeLanguageDetector.fromContent("Dies ist einfach nur Fließtext ohne Code."));
    }
}
