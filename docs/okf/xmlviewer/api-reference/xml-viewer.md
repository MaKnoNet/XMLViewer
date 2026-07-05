---
type: API Reference
title: XmlViewer
description: Vollständige, verifizierte Methodenreferenz der Vaadin-Komponente XmlViewer — Konstruktoren, alle öffentlichen und package-private Methoden mit Null-Verhalten, Rückgabewerten und Exceptions.
resource: web-common/src/main/java/de/makno/web/common/component/xmlviewer/XmlViewer.java
tags: [api-reference, xmlviewer, vaadin, search]
timestamp: '2026-07-07T10:00:00+02:00'
---

# Überblick

`XmlViewer` ist eine `public class XmlViewer extends Composite<Div> implements HasSize,
HasStyle, MatchNavigable`. Die narrative Beschreibung (Zweck, Rendering-Prinzip, Sicherheit)
steht bereits in [XmlViewer (Komponenten-Doku)](/components/xmlviewer.md) — diese Datei
konzentriert sich ausschließlich auf die erschöpfende, verifizierte Methodenreferenz.

**Thread-Safety (aus Javadoc übernommen und gegen den Code plausibilisiert):** Nicht
thread-safe. Eine Instanz gehört zu genau einer UI/Session; alle Felder (`root`,
`collapsible`, `tree`, `searchController`, `highlightedElements`, …) sind einfache
Instanzfelder ohne Synchronisation — die Klasse verlässt sich vollständig auf das
Vaadin-Session-Lock-Modell. `highlightedElements` ist ein `Collections.newSetFromMap(new
IdentityHashMap<>())`, also identitätsbasiert (kein `equals`-Vergleich der JDOM2-Elemente).

Implementiert [MatchNavigable](/api-reference/match-navigable.md); nutzbar mit
[SearchNavigator](/api-reference/search-navigator.md).

# Konstruktoren

## `public XmlViewer()`

Keine Parameter. Setzt `getContent().addClassName(CssClasses.ROOT)` und ruft `render()` auf
(rendert den leeren Platzhalter, da `root == null`). Wirft nichts.

## `public XmlViewer(Element root)`

- `root` (`org.jdom2.Element`) — null-erlaubt: ja, verifiziert: Der Konstruktor ruft
  `this()` und dann `setRoot(root)` auf; `setRoot` prüft nicht auf `null` und setzt das Feld
  direkt (`this.root = root; render();`). Wird `null` übergeben, verhält sich die Instanz wie
  nach dem parameterlosen Konstruktor (leerer Platzhalter „Kein XML-Element gesetzt.").

**Was bei ungültiger Eingabe passiert:** Nichts — `null` ist ein gültiger, dokumentierter
Wert für „keine Anzeige".

# Methoden

## `public void setRoot(Element root)`

- `root` — null-erlaubt: ja, verifiziert (`this.root = root;` ohne Prüfung). `null` leert die
  Ansicht (zeigt den Platzhalter „Kein XML-Element gesetzt.").
- Rückgabewert: keiner (`void`).
- Exceptions: keine im Methodenkörper; ruft intern `render()` auf, das seinerseits keine
  geprüften Exceptions deklariert.

## `public Element getRoot()`

- Keine Parameter.
- Rückgabewert: `Element`, kann `null` sein — genau dann, wenn zuletzt `setRoot(null)`
  aufgerufen wurde oder der parameterlose Konstruktor verwendet und `setRoot` nie aufgerufen
  wurde. Bedeutung von `null`: kein Wurzelelement gesetzt.
- Wirft nichts.

## `public void highlight(Element element)`

- `element` — null-erlaubt: ja, verifiziert durch expliziten Guard: `if (element == null) {
  return; }` als erste Zeile — `null` wird still ignoriert, kein Fehler.
- Zusätzliches Verhalten: Ist `element` nicht `null`, aber im aktuell gerenderten Baum
  unbekannt (`tree.elementHeaders().get(element)` liefert `null`, z.&nbsp;B. weil `element`
  aus einem anderen JDOM2-Dokument stammt oder kein Element des aktuellen `root`-Baums ist),
  wird ebenfalls still zurückgekehrt (`if (header == null) { return; }`) — **kein** Fehler,
  auch wenn das Element „falsch" ist. Andernfalls: Vorfahren aufklappen (`expandTo`), CSS-Klasse
  `HIGHLIGHT` setzen, zur Menge `highlightedElements` hinzufügen, hinscrollen.
- Rückgabewert: keiner (`void`).
- Exceptions: keine.

## `public void clearHighlight(Element element)`

- `element` — null-erlaubt: ja, verifiziert durch `if (element == null) { return; }` als
  erste Zeile.
- Unbekannte (nicht im Baum enthaltene) Elemente: `header` ist dann `null`, die
  `removeClassName`-Anweisung wird übersprungen (`if (header != null) {...}`), aber
  `highlightedElements.remove(element)` wird trotzdem ausgeführt (No-Op, falls nicht enthalten).
- Rückgabewert: keiner.
- Exceptions: keine.

## `public void clearHighlight()`

- Keine Parameter.
- Entfernt die Hervorhebung aller aktuell hervorgehobenen Elemente und leert
  `highlightedElements`.
- Rückgabewert: keiner.
- Exceptions: keine.

## `public void expandAll()`

- Keine Parameter. Klappt alle Elemente auf, deren Kinder-Container im aktuellen Baum bekannt
  sind (`tree.childContainers().keySet()`).
- Rückgabewert: keiner.
- Exceptions: keine.

## `public void collapseAll()`

- Analog zu `expandAll()`, klappt stattdessen alle zu.
- Exceptions: keine.

## `public void setCollapsible(boolean collapsible)`

- `collapsible` (`boolean`, kein Null-Fall möglich, primitiver Typ).
- Setzt das Feld und rendert **komplett neu** (`render()` — der gesamte Baum wird
  weggeworfen und neu aufgebaut; alle Highlight-/Klapp-Zustände gehen verloren, da `render()`
  am Anfang `clearHighlight()` aufruft und den Content leert).
- Rückgabewert: keiner.
- Exceptions: keine.

## `public boolean isCollapsible()`

- Keine Parameter. Rückgabewert: `boolean`, nie `null` (primitiver Typ). Reiner Getter.
- Exceptions: keine.

## `@Override public void search(String query)`

- `query` — null-erlaubt: ja. Der Javadoc-Kommentar behauptet direkt: „Leerer/`null`-Text
  löscht die Suche." Verifiziert über die Delegation an
  [`SearchController.search(String)`](/api-reference/search-controller.md): Dort wird
  `query` an `splitTerms` → `termSplitter.split(query)` weitergereicht. Der Standard-Splitter
  (`SearchController.DEFAULT_TERM_SPLITTER`) prüft selbst `if (query == null || query.isBlank())
  return List.of();` — bei leerer Trefferliste wird `matches = List.of()` gesetzt, was
  effektiv einer gelöschten Suche entspricht. **Die Javadoc-Behauptung ist zutreffend, aber nur
  weil der Standard-Splitter das selbst behandelt** — ein per `setSearchTermSplitter` gesetzter
  eigener Splitter, der bei `null` z.&nbsp;B. eine `NullPointerException` wirft, würde hier
  durchschlagen (siehe Diskrepanz-Hinweis unten).
- Rückgabewert: keiner.
- Exceptions: keine im `XmlViewer`-Methodenkörper selbst; abhängig vom aktiven
  `SearchTermSplitter` könnte ein defensiv nicht geschriebener eigener Splitter bei `null`
  scheitern (siehe [SearchTermSplitter](/api-reference/search-term-splitter.md)).

## `@Override public void nextMatch()`

- Keine Parameter. Delegiert an `searchController.nextMatch()`. Rückgabewert: keiner.
- Exceptions: keine (No-Op, wenn keine Treffer vorhanden — Guard `if (!matches.isEmpty())`
  im `SearchController`).

## `@Override public void previousMatch()`

- Analog zu `nextMatch()`, umgekehrte Richtung.

## `public void clearSearch()`

- Keine Parameter. Delegiert an `searchController.clearSearch()`. Rückgabewert: keiner.
- Exceptions: keine.

## `@Override public int getMatchCount()`

- Keine Parameter. Rückgabewert: `int`, Anzahl der aktuellen Treffer (`>= 0`), nie
  „ungültig", da `SearchController.getMatchCount()` schlicht `matches.size()` zurückgibt.
- Exceptions: keine.

## `@Override public int getCurrentMatchIndex()`

- Keine Parameter. Rückgabewert: `int`, 0-basierter Index des aktuellen Treffers, oder `-1`,
  wenn keiner aktiv ist (verifiziert: `SearchController` initialisiert `currentMatchIndex = -1`
  und setzt ihn nur bei vorhandenen Treffern auf `>= 0`).
- Exceptions: keine.

## `public void setSearchCaseSensitive(boolean caseSensitive)`

- `caseSensitive` (`boolean`). Setzt das Feld und delegiert an
  `searchController.setCaseSensitive(caseSensitive)`, was bei geänderter Einstellung und
  aktiver Suche (`hasActiveQuery()`) die Suche automatisch neu ausführt.
- Rückgabewert: keiner. Exceptions: keine.

## `public boolean isSearchCaseSensitive()`

- Keine Parameter. Reiner Getter, `boolean`, nie `null`. Exceptions: keine.

## `public void setSearchTermSplitter(SearchTermSplitter splitter)`

- `splitter` — **null-erlaubt: nein**, verifiziert durch
  `this.searchTermSplitter = Objects.requireNonNull(splitter, "splitter");` — dies ist ein
  expliziter Fail-Fast-Check.
- **Exceptions: wirft `NullPointerException` mit Nachricht `"splitter"`, wenn `splitter ==
  null`.** Dies ist im Javadoc **nicht** dokumentiert (der Javadoc-Kommentar der Methode
  erwähnt nur das Verhalten bei aktiver Suche, keine Null-Behandlung) — siehe Diskrepanz
  unten.
- Rückgabewert: keiner.

## `@Override public Registration addMatchChangeListener(ComponentEventListener<MatchChangeEvent> listener)`

- `listener` — null-erlaubt: nicht direkt geprüft in `XmlViewer` selbst; delegiert an
  Vaadins `Composite.addListener(MatchChangeEvent.class, listener)`. Das Verhalten bei
  `null` liegt außerhalb dieser Klasse (Vaadin-Flow-internes `ComponentEventBus`, i.d.R.
  wirft es dort eine `NullPointerException`, aber das ist nicht durch `XmlViewer`
  verifizierbar/dokumentierbar, da der Aufruf reine Weiterleitung ist).
- Rückgabewert: `Registration`, laut Vaadin-Framework-Vertrag nie `null` — dient dem
  späteren Lösen der Registrierung (`registration.remove()`).
- Exceptions: keine explizit in `XmlViewer`; siehe oben zur Weiterleitung.

# Package-private Methoden (nur für Tests, siehe Kommentar „Paket-sichtbare Helfer")

## `Div headerOf(Element element)`

- `element` — null-erlaubt: ja (keine Prüfung); `tree.elementHeaders().get(element)` liefert
  bei `null`-Key regulär `null` zurück (kein Fehler, da `IdentityHashMap.get(null)` gültig ist).
- Rückgabewert: `Div`, kann `null` sein, wenn `element` nicht im aktuellen Baum bekannt ist.
- Exceptions: keine.

## `boolean isExpanded(Element element)`

- `element` — null-erlaubt: ja (keine Prüfung); `tree.childContainers().get(element)` liefert
  bei unbekanntem/`null`-Element `null`.
- Rückgabewert: `boolean`. **Bedeutung:** liefert `true`, wenn der Container `null` ist
  (unbekanntes Element) ODER wenn der Container sichtbar ist (`container == null ||
  container.isVisible()`) — ein unbekanntes Element gilt damit als „expandiert", was beim
  Lesen der Methode zu beachten ist (kein Fehlerzustand, sondern harmloser Default-Fall für
  Leaf-Elemente ohne eigenen Kinder-Container).
- Exceptions: keine.

## `boolean endTagVisible(Element element)`

- `element` — null-erlaubt: ja (keine Prüfung).
- Rückgabewert: `boolean`, `true` nur wenn ein End-Tag-`Div` für das Element existiert UND
  sichtbar ist (`endTag != null && endTag.isVisible()`). Unbekannte Elemente liefern `false`.
- Exceptions: keine.

## `List<String> searchableTexts()`

- Keine Parameter. Rückgabewert: `List<String>`, nie `null` (`.stream().map(...).toList()`
  liefert immer eine — ggf. leere — Liste), die Klartexte aller durchsuchbaren Tokens in
  Dokumentreihenfolge.
- Exceptions: keine.

## `Set<String> cssClassesOfTokenText(String text)`

- `text` — **null-erlaubt: nein für sinnvolles Ergebnis**, aber es gibt **keinen expliziten
  Null-Check**: `text.equals(token.text())` würde bei `text == null` **keine
  `NullPointerException` werfen**, weil `text` der Empfänger von `.equals(...)` in
  `token.text().equals(...)`? — **Verifikation der genauen Reihenfolge:** Der Code lautet
  `text.equals(token.text())`, d.h. `text` ist der Empfänger. Ist `text == null`, wirft dieser
  Aufruf **eine `NullPointerException`**, da man `equals` nicht auf `null` aufrufen kann. Es
  gibt keinen Guard davor.
- **Exceptions: wirft `NullPointerException`, wenn `text == null`** (nicht dokumentiert, da
  die Methode überhaupt keinen Javadoc-Kommentar hat — reine interne Testhilfe).
- Rückgabewert: `Set<String>`, nie `null` — entweder die CSS-Klassen des ersten Tokens mit
  exakt passendem Text, oder `Set.of()` (leer), wenn kein Token passt
  (`.orElseGet(Set::of)`).

# Diskrepanzen zwischen Javadoc und Code (Zusammenfassung)

- **`setSearchTermSplitter(SearchTermSplitter)`:** Javadoc dokumentiert nicht, dass die
  Methode bei `splitter == null` eine `NullPointerException` wirft — der Code tut dies aber
  explizit über `Objects.requireNonNull(splitter, "splitter")`. Diese Datei dokumentiert die
  verifizierte Wahrheit (siehe Methodenabschnitt oben).
- **`cssClassesOfTokenText(String)`:** Keine Javadoc-Dokumentation vorhanden (interne
  Testhilfe ohne Kommentar), aber implizites Risiko einer `NullPointerException` bei
  `text == null`, das für künftige Aufrufer wichtig zu wissen ist.

# Citations

[1] web-common/src/main/java/de/makno/web/common/component/xmlviewer/XmlViewer.java
