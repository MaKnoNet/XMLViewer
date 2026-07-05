---
type: API Reference
title: XmlTreeRenderer
description: Package-private, single-use Renderer, der einen org.jdom2.Element-Baum in eine Vaadin-Div/Span-Struktur überführt und dabei Highlight-/Klapp-Nachschlage-Strukturen sowie die durchsuchbare Token-Liste aufbaut.
resource: web-common/src/main/java/de/makno/web/common/component/xmlviewer/XmlTreeRenderer.java
tags: [api-reference, xmlviewer, rendering, jdom2]
timestamp: '2026-07-07T10:00:00+02:00'
---

# Überblick


`XmlTreeRenderer` ist eine package-private, finale Klasse, die einen `org.jdom2.Element`-Baum
rekursiv in Vaadin-`Div`/`Span`-Komponenten übersetzt: pro Element eine Start-Tag-Zeile, ein
eingerückter Kinder-Container und eine End-Tag-Zeile. Reines Rendering ohne Such- oder
Highlight-Zustand — dieser wird erst von [XmlViewer](/api-reference/xmlviewer/xml-viewer/xml-viewer.md) auf Basis
des zurückgegebenen [RenderedTree](/api-reference/xmlviewer/rendered-tree/rendered-tree.md) verwaltet. Texte werden
ausschließlich über `new Span(text)`/Vaadin-`setText`-Mechanik gesetzt, wodurch Vaadin
XML-Sonderzeichen automatisch escaped (kein `innerHTML`, kein XSS-Risiko).

**Thread-Safety:** **Nicht thread-safe und single-use.** Die Instanzfelder
`elementHeaders`, `childContainers`, `endTags`, `toggles` (alle `IdentityHashMap`, nicht
synchronisiert) und `tokens` (`ArrayList`, nicht synchronisiert) werden während eines
einzigen [render(Element)](#render)-Aufrufs befüllt und danach unverändert in ein
[RenderedTree](/api-reference/xmlviewer/rendered-tree/rendered-tree.md) verpackt zurückgegeben. Die Klasse ist laut
Klassen-Javadoc "auf einen `render(Element)`-Aufruf ausgelegt" — der Code erzwingt das
jedoch **nicht**: Ein zweiter `render(...)`-Aufruf auf derselben Instanz würde stillschweigend
zusätzliche Einträge in dieselben Maps/Listen schreiben (kein Reset, keine Guard-Prüfung),
was zu einer inkonsistenten `RenderedTree`-Rückgabe führen könnte. In der Praxis wird pro
Renderdurchlauf stets eine frische Instanz erzeugt (siehe `XmlViewer.render()`).

# Felder

| Feld | Typ | Bedeutung | null-erlaubt |
|---|---|---|---|
| `collapsible` | `final boolean` | Schaltet Ein-/Ausklapp-Dreiecke pro Element ein/aus, gesetzt im Konstruktor. | entfällt (primitiv) |
| `elementHeaders` | `final Map<Element, Div>` | Identitätsbasierte Abbildung Element → Start-Tag-Zeile, mit `new IdentityHashMap<>()` initialisiert, während `render` befüllt. | Feld selbst nie `null` (final, sofort initialisiert); wird unverändert an `RenderedTree` übergeben |
| `childContainers` | `final Map<Element, Div>` | Identitätsbasierte Abbildung Element → Kinder-Container, `IdentityHashMap`, während `render` befüllt. | wie `elementHeaders` |
| `endTags` | `final Map<Element, Div>` | Identitätsbasierte Abbildung Element → End-Tag-Zeile, `IdentityHashMap`, während `render` befüllt. | wie `elementHeaders` |
| `toggles` | `final Map<Element, Span>` | Identitätsbasierte Abbildung Element → Aufklapp-Dreieck, `IdentityHashMap`, während `render` befüllt. | wie `elementHeaders` |
| `tokens` | `final List<SearchableToken>` | Alle durchsuchbaren Tokens in Dokumentreihenfolge, mit `new ArrayList<>()` initialisiert, über `token(...)` befüllt. | Feld selbst nie `null`; Elemente nie `null` (nur über `token(...)` mit garantiert nicht-`null` `SearchableToken` befüllt) |

Alle sechs Felder sind `final` (Konstruktorparameter bzw. sofortige Feldinitialisierung) —
keines wird nach der Konstruktion neu zugewiesen, nur die vier `Map`s und die `List` werden
während eines `render(Element)`-Durchlaufs mutiert (Einträge hinzugefügt, nie ersetzt/entfernt).

# Thread-Safety

**Nicht thread-safe und single-use** (verifiziert): Die Instanzfelder `elementHeaders`,
`childContainers`, `endTags`, `toggles` (alle `IdentityHashMap`, nicht synchronisiert) und
`tokens` (`ArrayList`, nicht synchronisiert) werden während eines einzigen
[render(Element)](./render.md)-Aufrufs befüllt und danach unverändert in ein
[RenderedTree](/api-reference/xmlviewer/rendered-tree/rendered-tree.md) verpackt zurückgegeben. Die Klasse ist laut
Klassen-Javadoc "auf einen `render(Element)`-Aufruf ausgelegt" — der Code erzwingt das jedoch
**nicht**: ein zweiter `render(...)`-Aufruf auf derselben Instanz würde stillschweigend
zusätzliche Einträge in dieselben Maps/Listen schreiben (kein Reset, keine Guard-Prüfung), was
zu einer inkonsistenten `RenderedTree`-Rückgabe führen könnte. In der Praxis wird pro
Renderdurchlauf stets eine frische Instanz erzeugt (`XmlViewer.render()`: `new
XmlTreeRenderer(collapsible).render(root)`). Instanzen dürfen nicht zwischen Threads geteilt
werden.

# Serialisierung

Nicht `Serializable` — `XmlTreeRenderer` implementiert kein Serialisierungs-Interface
(verifiziert gegen die Klassendeklaration `final class XmlTreeRenderer`, kein
`extends`/`implements`). Da die Klasse ohnehin single-use und nur ein temporäres Hilfsobjekt
für einen Render-Durchlauf ist (nicht Teil des dauerhaften `XmlViewer`-Zustands), besteht kein
Bedarf, sie zu serialisieren.

# equals/hashCode/toString

Keine dieser Methoden ist überschrieben (verifiziert: keine `equals`/`hashCode`/`toString`-
Deklaration im Quellcode) — es gilt die **Identitätssemantik von `java.lang.Object`**
(`==`-Vergleich, identitätsbasierter Hashcode, `toString()` liefert Klassenname+Hashcode). Da
die Klasse Single-Use ist und nie in Sammlungen mit Werte-Gleichheit verwendet wird, hat das in
der Praxis geringe Relevanz.

# Vererbungshierarchie


**Vorwärts (eigene Deklaration):** `final class XmlTreeRenderer` (package-private, kein
`extends`/`implements`).

- **Superklasse:** keine explizite (impliziter `Object`).
- **Interfaces:** keine.
- Die Klasse ist `final` und package-private — es kann ohnehin keine Subklasse außerhalb
  (und aufgrund von `final` auch keine innerhalb) des Pakets geben.

**Rückwärts (Abhängige):** Verifiziert per Grep auf `extends XmlTreeRenderer` über den
gesamten `web-common/src/main/java/de/makno/web/common/component/`-Baum — **kein Treffer**
(erwartungsgemäß, die Klasse ist `final`). `XmlTreeRenderer` hat keine projektinterne
Vererbungsbeziehung; [XmlViewer](/api-reference/xmlviewer/xml-viewer/xml-viewer.md) hält eine Instanz nur als
lokales Hilfsobjekt für einen einzelnen Render-Durchlauf (Assoziation, nicht Vererbung).

# Konstruktoren

- [siehe constructor.md](./constructor.md)

# Methoden

- [`render`](./render.md)
- [`renderElement (private)`](./render-element.md)
- [`renderChildren (private)`](./render-children.md)
- [`renderNamespaces (private)`](./render-namespaces.md)
- [`renderAttributes (private)`](./render-attributes.md)
- [`quotedValue (private)`](./quoted-value.md)
- [`endTagLine (private)`](./end-tag-line.md)
- [`endTagMarker (private static)`](./end-tag-marker.md)
- [`textLine (private)`](./text-line.md)
- [`commentLine (private)`](./comment-line.md)
- [`cdataLine (private)`](./cdata-line.md)
- [`newToggle (private)`](./new-toggle.md)
- [`tag (private)`](./tag.md)
- [`newLine (private static)`](./new-line.md)
- [`newIndent (private static)`](./new-indent.md)
- [`prependRails (private)`](./prepend-rails.md)
- [`rail (private static)`](./rail.md)
- [`punct (private static)`](./punct.md)
- [`plain (private static)`](./plain.md)
- [`token (private)`](./token.md)
- [`isSingleInlineText (private static)`](./is-single-inline-text.md)
- [`meaningfulContent (private static)`](./meaningful-content.md)

# Citations


[1] web-common/src/main/java/de/makno/web/common/component/xmlviewer/XmlTreeRenderer.java
