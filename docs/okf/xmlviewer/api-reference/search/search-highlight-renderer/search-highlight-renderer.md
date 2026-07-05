---
type: API Reference
title: SearchHighlightRenderer
description: Schlanke, serialisierbare Abstraktion für das Zeichnen von Suchtreffern, entkoppelt von deren Ermittlung im SearchController.
resource: web-common/src/main/java/de/makno/web/common/component/search/SearchHighlightRenderer.java
tags: [api-reference, search, interface]
timestamp: '2026-07-07T10:00:00+02:00'
---

# Überblick


`SearchHighlightRenderer` ist ein `Serializable`-Interface mit drei Methoden, das das
**Zeichnen** von Suchtreffern von deren **Ermittlung** in
[SearchController](/api-reference/search/search-controller/search-controller.md) trennt (Dependency Inversion). Die
Standardimplementierung [FrontendSearchHighlighter](/api-reference/search/frontend-search-highlighter/frontend-search-highlighter.md)
lagert das Highlighting ins Frontend aus (CSS Custom Highlight API) — ohne zusätzlichen
DOM-Knoten oder Session-Heap pro Treffer. Tests können stattdessen einen aufzeichnenden
Renderer übergeben, ohne einen echten Client zu benötigen. Architekturkontext in
[Geteilte Such-Engine](/architecture/search-engine.md).

**Thread-Safety:** Das Interface selbst ist zustandslos; Thread-Safety-Eigenschaften
hängen von der konkreten Implementierung ab. `FrontendSearchHighlighter` etwa hält nur
eine unveränderliche Komponentenreferenz und ist an genau eine UI/Session gebunden.

# Felder

Keine Felder (Interface). `SearchHighlightRenderer` deklariert ausschließlich drei
abstrakte Instanzmethoden (`render`, `moveCurrent`, `clear`) und keine Konstanten
(verifiziert: keine Feld-Deklaration im Quellcode).

# Thread-Safety

Kein besonderer Vertrag auf Interface-Ebene: `SearchHighlightRenderer` selbst ist
zustandslos und deklariert keine Felder. Die tatsächliche Thread-Safety hängt von der
jeweiligen Implementierung ab — die einzige projektinterne Implementierung,
[FrontendSearchHighlighter](/api-reference/search/frontend-search-highlighter/frontend-search-highlighter.md), hält nur
eine `final` Komponentenreferenz und ist an eine UI/Session gebunden (siehe deren
eigene Thread-Safety-Sektion).

# Serialisierung

`extends Serializable` — die Schnittstelle selbst erweitert `java.io.Serializable`
als Marker (verifiziert: `public interface SearchHighlightRenderer extends
Serializable`), damit **jede** Implementierung (insbesondere Lambda-artige
Test-Recorder) als Vaadin-Session-Zustand serialisierbar bleibt. Ein Interface kann
selbst keine `serialVersionUID` sinnvoll deklarieren (keine Instanzfelder, keine
Objektidentität) — dieser Wert ist Sache jeder konkreten Implementierung.
`FrontendSearchHighlighter` etwa setzt dafür explizit `serialVersionUID = 1L`. Die im
Überblick verwendete Bezeichnung „serialisierbare Abstraktion" ist damit zutreffend
verifiziert: Das Interface ist tatsächlich `Serializable`, aber nur als reiner
Marker/Vertrag — die eigentliche Serialisierungslogik liegt bei den Implementierungen.

# equals/hashCode/toString

Ein Interface deklariert keine eigenen `equals`-/`hashCode`-/`toString`-Implementierungen
und kann dies auch nicht (verifiziert: keine entsprechenden Default- oder
statischen Methoden im Quellcode). Für jede konkrete Implementierung gilt mangels
eigener Überschreibung die **Identitätssemantik von `java.lang.Object`**, sofern die
Implementierung selbst nichts anderes deklariert — siehe dazu
[FrontendSearchHighlighter](/api-reference/search/frontend-search-highlighter/frontend-search-highlighter.md).

# Vererbungshierarchie


**Vorwärts (eigene Deklaration):** `public interface SearchHighlightRenderer extends
Serializable`.

- **Erweitertes Interface:** `java.io.Serializable` — JDK-Standard-Interface (Marker-Interface,
  keine Methoden), kein Projekt-Typ, daher kein Cross-Link.

**Rückwärts (Abhängige):** Verifiziert per Grep auf `implements ... SearchHighlightRenderer`
über den gesamten `web-common/src/main/java/de/makno/web/common/component/`-Baum — **ein
Treffer**:

- [FrontendSearchHighlighter](/api-reference/search/frontend-search-highlighter/frontend-search-highlighter.md) — `public final
  class FrontendSearchHighlighter implements SearchHighlightRenderer`, die einzige
  Standardimplementierung im Projekt (Frontend-basiertes Highlighting über die CSS Custom
  Highlight API, siehe Überblick).

# Konstruktoren


# Methoden

- [``render(List<TokenMatch> matches, int currentIndex)``](./render.md)
- [``moveCurrent(int currentIndex)``](./move-current.md)
- [``clear()``](./clear.md)

# Citations


[1] web-common/src/main/java/de/makno/web/common/component/search/SearchHighlightRenderer.java
