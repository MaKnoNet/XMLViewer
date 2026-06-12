# Projekt-Konventionen – XMLViewer

## Code-Qualität

- **Es wird IMMER nach Clean-Code-Richtlinien entwickelt.** Das umfasst insbesondere:
  - **Single Responsibility:** Jede Klasse/Methode hat genau eine klar abgegrenzte Aufgabe.
  - **Sprechende Namen:** Klassen, Methoden und Variablen benennen ihre Absicht, nicht ihre Implementierung.
  - **Kleine Einheiten:** Methoden sind kurz und auf einer Abstraktionsebene; Klassen bleiben überschaubar.
  - **Keine Magic-Strings/-Numbers:** Konstanten werden in dedizierten Klassen (z. B. `CssClasses`) gehalten.
  - **Keine Duplikate (DRY):** Wiederholte Logik wird extrahiert und wiederverwendet.
  - **Saubere Fehlerbehandlung:** Null-/Edge-Cases explizit behandeln, kein stilles Verschlucken.
  - **Serializable-Klassen** erhalten immer eine explizite `serialVersionUID`.

## Code-Formatierung

- **Java-Quellcode wird IMMER mit `palantir-java-format` formatiert.**
  Vor dem Commit/Abschluss einer Änderung muss der geänderte Java-Code in diesem Format vorliegen.
