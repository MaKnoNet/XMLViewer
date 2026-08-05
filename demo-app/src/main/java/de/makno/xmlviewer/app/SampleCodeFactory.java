package de.makno.xmlviewer.app;

import de.makno.web.common.component.code.CodeLanguage;

/**
 * Liefert kurze Beispiel-Snippets je Sprache für die {@code CodeViewer}-Demo – jeweils mit faltbaren
 * Blöcken (Klassen/Methoden/Objekte) und wiederkehrenden Bezeichnern für die Treffer-Navigation.
 */
final class SampleCodeFactory {

    private SampleCodeFactory() {}

    /** Beispiel-Quelltext zur gegebenen Sprache; {@link CodeLanguage#PLAIN} liefert einen Hinweistext. */
    static String sampleFor(CodeLanguage language) {
        return switch (language) {
            case JAVA -> JAVA;
            case CSHARP -> CSHARP;
            case PYTHON -> PYTHON;
            case JSON -> JSON;
            case YAML -> YAML;
            case HTML -> HTML;
            case CSS -> CSS;
            case JAVASCRIPT -> JAVASCRIPT;
            case XML -> XML;
            case SQL -> SQL;
            case PLAIN -> "Wähle oben eine Sprache, um ein Beispiel mit Syntax-Highlighting zu sehen.";
        };
    }

    private static final String JAVA = """
            package com.example.demo;

            import java.util.List;

            /** Demo-Klasse mit faltbaren Bloecken (Klasse, Methode, if). */
            public class OrderService {

                private final List<String> items;

                public OrderService(List<String> items) {
                    this.items = items;
                }

                public int countActive() {
                    int count = 0;
                    for (String item : items) {
                        if (item != null && !item.isBlank()) {
                            count++;
                        }
                    }
                    return count;
                }
            }
            """;

    private static final String CSHARP = """
            using System;

            namespace Demo
            {
                public class OrderService
                {
                    public int CountActive(string[] items)
                    {
                        int count = 0;
                        foreach (var item in items)
                        {
                            if (!string.IsNullOrWhiteSpace(item))
                            {
                                count++;
                            }
                        }
                        return count;
                    }
                }
            }
            """;

    private static final String PYTHON = """
            from dataclasses import dataclass


            @dataclass
            class Order:
                customer: str
                total: float

                def is_large(self) -> bool:
                    return self.total > 100.0


            def summarize(orders):
                for order in orders:
                    if order.is_large():
                        print(f"Grossauftrag: {order.customer}")
            """;

    private static final String JSON = """
            {
              "name": "Demo-Bibliothek",
              "books": [
                { "id": "b-001", "title": "Erstes Buch", "price": 9.99 },
                { "id": "b-002", "title": "Zweites Buch", "price": 14.5 }
              ],
              "open": true
            }
            """;

    private static final String YAML = """
            name: Demo
            version: 1.0.0
            server:
              host: localhost
              port: 8080
            items:
              - eins
              - zwei
              - drei
            """;

    private static final String HTML = """
            <!DOCTYPE html>
            <html lang="de">
              <head>
                <title>Demo</title>
              </head>
              <body>
                <h1>Hallo &amp; willkommen</h1>
                <ul>
                  <li>Eins</li>
                  <li>Zwei</li>
                </ul>
              </body>
            </html>
            """;

    private static final String CSS = """
            :root {
              --accent: #2563eb;
            }

            .button {
              color: var(--accent);
              padding: 8px 16px;
              border-radius: 6px;
            }

            .button:hover {
              background: var(--accent);
              color: white;
            }
            """;

    private static final String JAVASCRIPT = """
            function fibonacci(n) {
              const seq = [0, 1];
              for (let i = 2; i < n; i++) {
                seq.push(seq[i - 1] + seq[i - 2]);
              }
              return seq;
            }

            console.log(fibonacci(10));
            """;

    private static final String XML = """
            <?xml version="1.0" encoding="UTF-8"?>
            <library name="Demo">
              <book id="b-001">
                <title>Erstes Buch</title>
                <price currency="EUR">9.99</price>
              </book>
            </library>
            """;

    private static final String SQL = """
            SELECT b.id, b.title, b.price
            FROM book b
            JOIN author a ON a.id = b.author_id
            WHERE b.price > 10.0
            ORDER BY b.title;
            """;
}
