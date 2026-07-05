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
Highlight-Zustand — dieser wird erst von [XmlViewer](/api-reference/xml-viewer.md) auf Basis
des zurückgegebenen [RenderedTree](/api-reference/rendered-tree.md) verwaltet. Texte werden
ausschließlich über `new Span(text)`/Vaadin-`setText`-Mechanik gesetzt, wodurch Vaadin
XML-Sonderzeichen automatisch escaped (kein `innerHTML`, kein XSS-Risiko).

**Thread-Safety:** **Nicht thread-safe und single-use.** Die Instanzfelder
`elementHeaders`, `childContainers`, `endTags`, `toggles` (alle `IdentityHashMap`, nicht
synchronisiert) und `tokens` (`ArrayList`, nicht synchronisiert) werden während eines
einzigen [render(Element)](#render)-Aufrufs befüllt und danach unverändert in ein
[RenderedTree](/api-reference/rendered-tree.md) verpackt zurückgegeben. Die Klasse ist laut
Klassen-Javadoc "auf einen `render(Element)`-Aufruf ausgelegt" — der Code erzwingt das
jedoch **nicht**: Ein zweiter `render(...)`-Aufruf auf derselben Instanz würde stillschweigend
zusätzliche Einträge in dieselben Maps/Listen schreiben (kein Reset, keine Guard-Prüfung),
was zu einer inkonsistenten `RenderedTree`-Rückgabe führen könnte. In der Praxis wird pro
Renderdurchlauf stets eine frische Instanz erzeugt (siehe `XmlViewer.render()`).

# Konstruktoren

```java
XmlTreeRenderer(boolean collapsible)
```

- `collapsible` (`boolean`) — primitiver Typ, kann nicht `null` sein (nicht anwendbar/kein
  null-Fall). Steuert, ob Elemente mit Inhalt ein Aufklapp-Dreieck (`CssClasses.TOGGLE`)
  erhalten oder nur einen Einrückungs-Platzhalter (`newIndent()`).
- Keine Validierung nötig oder vorhanden (boolescher Wert kann nicht ungültig sein); der
  Konstruktor weist das Feld unverändert zu und initialisiert die vier `IdentityHashMap`-
  und die eine `ArrayList`-Instanzfelder über deren Feld-Initialisierer.
- Wirft nichts.

# Methoden

## render

```java
RenderedTree render(Element root)
```
Paket-sichtbar (keine explizite Modifier-Angabe = package-private).

- `root` (`Element`) — null-erlaubt: **nein in der Praxis, aber nicht verifiziert im
  Code selbst.** Es gibt keinen expliziten Null-Check. Wird `null` übergeben, ruft
  `renderElement(null, container, 0)` unmittelbar `meaningfulContent(null)` auf, was
  `element.getContent()` auf einer `null`-Referenz aufruft → **`NullPointerException`**
  (implizit, nicht deklariert, nicht dokumentiert). Der einzige Aufrufer im Projekt
  (`XmlViewer.render()`) ruft `render(root)` nur im `else`-Zweig auf, wenn `root != null`
  bereits geprüft wurde — die Absicherung liegt beim Aufrufer, nicht in dieser Methode.
- Rückgabewert (`RenderedTree`) — nie `null`: Es wird immer ein neues `RenderedTree`-Objekt
  konstruiert und zurückgegeben (kein Pfad, der `null` liefert), vorausgesetzt die Methode
  kehrt überhaupt zurück (siehe NPE-Fall oben).
- Geworfene Exceptions: `NullPointerException` (implizit) bei `root == null`, siehe oben.
  Sonst keine explizit geworfenen Exceptions im Methodenkörper.

## renderElement (private)

```java
private void renderElement(Element element, Div container, int depth)
```

- `element` (`Element`) — null-erlaubt: nein, nicht abgesichert; `meaningfulContent(element)`
  ruft `element.getContent()` auf → NPE bei `null`.
- `container` (`Div`) — null-erlaubt: nein, nicht abgesichert; `container.add(header)` würde
  bei `null` eine NPE werfen. In der Praxis immer ein frisch erzeugter `Div`.
- `depth` (`int`) — primitiver Typ, kein Null-Fall. Negative Werte würden `prependRails`
  einfach 0-mal iterieren lassen (keine Exception, da die `for`-Schleife bei `i < depth`
  mit negativem `depth` sofort abbricht).
- Rückgabewert: `void`.
- Geworfene Exceptions: implizite `NullPointerException` bei `element == null` oder
  `container == null` (kein expliziter Check).
- Rekursiver Aufbau: baut die Start-Tag-Zeile, entscheidet zwischen "kein Inhalt"
  (selbstschließendes Tag), "einzelner Inline-Text" (Text direkt in der Start-Zeile) und
  "hat Kindknoten" (rekursiver Aufruf über `renderChildren` + separate End-Tag-Zeile).

## renderChildren (private)

```java
private Div renderChildren(Element element, List<Content> meaningful, int depth)
```

- `element` (`Element`) — null-erlaubt: nein; wird als Schlüssel in `childContainers.put(element, children)`
  verwendet — `IdentityHashMap` erlaubt zwar `null`-Schlüssel, aber semantisch falsch;
  keine Exception, da `IdentityHashMap.put` `null`-Schlüssel toleriert.
- `meaningful` (`List<Content>`) — null-erlaubt: nein, nicht abgesichert; die `for`-Schleife
  (`for (Content content : meaningful)`) würde bei `null` eine `NullPointerException` werfen.
- `depth` (`int`) — primitiv, kein Null-Fall.
- Rückgabewert (`Div`) — nie `null`: Es wird immer ein neuer `Div` mit `CssClasses.CHILDREN`
  konstruiert und zurückgegeben.
- Geworfene Exceptions: implizite `NullPointerException` bei `meaningful == null`.
- Verwendet ein `switch`-Pattern-Matching über den `sealed`/bekannten `Content`-Typ (`Element`,
  `CDATA`, `Comment`, `Text`); der `default`-Zweig ("andere Knotentypen werden nicht
  dargestellt") behandelt z.&nbsp;B. `ProcessingInstruction`/`EntityRef` stillschweigend,
  ohne Exception oder Log — bewusstes Verschlucken unbekannter Inhaltstypen laut Kommentar.

## renderNamespaces (private)

```java
private void renderNamespaces(Element element, Div header)
```

- `element` (`Element`) — null-erlaubt: nein; `element.getNamespacesIntroduced()` würde
  bei `null` eine NPE werfen.
- `header` (`Div`) — null-erlaubt: nein; `header.add(...)` würde bei `null` eine NPE werfen.
- Rückgabewert: `void`.
- Geworfene Exceptions: implizite NPE bei `element == null` oder `header == null`.
  Kein expliziter `throw`.

## renderAttributes (private)

```java
private void renderAttributes(Element element, Div header)
```

- `element` (`Element`) — null-erlaubt: nein; `element.getAttributes()` würde bei `null`
  eine NPE werfen.
- `header` (`Div`) — null-erlaubt: nein; gleiche Begründung wie oben.
- Rückgabewert: `void`.
- Geworfene Exceptions: implizite NPE, kein expliziter `throw`.

## quotedValue (private)

```java
private Span quotedValue(String value, Element owner)
```

- `value` (`String`) — null-erlaubt: ja im Sinne, dass die Methode selbst nicht direkt
  darauf zugreift (übergibt `value` unverändert an `token(...)`, welches `null` intern zu
  `""` normalisiert — siehe [token](#token-private) unten). Kein NPE-Risiko in dieser
  Methode selbst.
- `owner` (`Element`) — null-erlaubt: ja im Sinne, dass diese Methode `owner` nur
  durchreicht (an `token(...)`, das `owner` nur als Feld in `SearchableToken` speichert,
  ohne es zu dereferenzieren).
- Rückgabewert (`Span`) — nie `null`: Es wird immer ein neuer `Span` (`group`) konstruiert
  und mit vier Kindspans befüllt.
- Geworfene Exceptions: keine.
- Baut `="wert"` als Span-Folge (Anführungszeichen und Gleichheitszeichen als
  Satzzeichen-Spans, Wert als durchsuchbares Token).

## endTagLine (private)

```java
private Div endTagLine(Element element, int depth)
```

- `element` (`Element`) — null-erlaubt: nein; wird an `tag(element)` weitergereicht, das
  `element.getQualifiedName()` aufruft → NPE bei `null`.
- `depth` (`int`) — primitiv, kein Null-Fall.
- Rückgabewert (`Div`) — nie `null`.
- Geworfene Exceptions: implizite NPE bei `element == null` (über `tag(...)`).

## endTagMarker (private static)

```java
private static Span endTagMarker()
```

- Keine Parameter.
- Rückgabewert (`Span`) — nie `null`; erzeugt einen leeren `Span` mit CSS-Klasse
  `ENDTAG_MARKER` (das sichtbare Symbol liefert CSS `::before`, kein Java-Text).
- Geworfene Exceptions: keine.

## textLine (private)

```java
private Div textLine(String text, Element owner, int depth)
```

- `text` (`String`) — null-erlaubt: ja im Sinne, dass diese Methode `text` nur an
  `token(...)` durchreicht, das `null` zu `""` normalisiert.
- `owner` (`Element`) — null-erlaubt: ja (wird nur durchgereicht, nicht dereferenziert
  in dieser Methode).
- `depth` (`int`) — primitiv.
- Rückgabewert (`Div`) — nie `null`.
- Geworfene Exceptions: keine in dieser Methode selbst.

## commentLine (private)

```java
private Div commentLine(String text, Element owner, int depth)
```

- `text` (`String`) — null-erlaubt: ja, wird nur an `token(...)` durchgereicht (Normalisierung
  dort).
- `owner` (`Element`) — null-erlaubt: ja, nur durchgereicht.
- `depth` (`int`) — primitiv.
- Rückgabewert (`Div`) — nie `null`.
- Geworfene Exceptions: keine in dieser Methode selbst.

## cdataLine (private)

```java
private Div cdataLine(String text, Element owner, int depth)
```

- `text` (`String`) — null-erlaubt: ja, nur durchgereicht an `token(...)`.
- `owner` (`Element`) — null-erlaubt: ja, nur durchgereicht.
- `depth` (`int`) — primitiv.
- Rückgabewert (`Div`) — nie `null`.
- Geworfene Exceptions: keine in dieser Methode selbst.

## newToggle (private)

```java
private Span newToggle(Element element)
```

- `element` (`Element`) — null-erlaubt: ja im Sinne, dass diese Methode `element` nur
  als Schlüssel in `toggles.put(element, toggle)` verwendet; `IdentityHashMap` erlaubt
  `null`-Schlüssel, keine Exception.
- Rückgabewert (`Span`) — nie `null`; leerer `Span` mit CSS-Klasse `TOGGLE` (sichtbares
  Zeichen kommt aus CSS `::before`).
- Geworfene Exceptions: keine.

## tag (private)

```java
private Span tag(Element element)
```

- `element` (`Element`) — null-erlaubt: nein; `element.getQualifiedName()` wirft eine
  `NullPointerException`, wenn `element == null`.
- Rückgabewert (`Span`) — nie `null` (sofern keine Exception geworfen wird); liefert das
  Ergebnis von `token(CssClasses.TAG, ..., element)`, das stets einen `Span` konstruiert.
- Geworfene Exceptions: implizite NPE bei `element == null`.

## newLine (private static)

```java
private static Div newLine()
```

- Keine Parameter.
- Rückgabewert (`Div`) — nie `null`; neuer `Div` mit CSS-Klasse `LINE`.
- Geworfene Exceptions: keine.

## newIndent (private static)

```java
private static Span newIndent()
```

- Keine Parameter.
- Rückgabewert (`Span`) — nie `null`; leerer `Span` mit CSS-Klasse `INDENT`.
- Geworfene Exceptions: keine.

## prependRails (private)

```java
private void prependRails(Div line, int depth)
```

- `line` (`Div`) — null-erlaubt: nein, sofern `depth > 0`; `line.add(rail())` würde bei
  `line == null` eine NPE werfen. Bei `depth <= 0` durchläuft die Schleife null Iterationen
  und `line` wird nie dereferenziert — dann wäre `null` folgenlos, aber das ist ein
  Nebeneffekt der Schleifenbedingung, keine bewusste Absicherung.
- `depth` (`int`) — primitiv; negative Werte führen zu null Schleifendurchläufen (kein Fehler).
- Rückgabewert: `void`.
- Geworfene Exceptions: implizite NPE bei `line == null` und `depth > 0`.
- Fügt `depth`-mal eine `RAIL`-Zelle voran (vor Toggle/Marker/Inhalt aufgerufen → linksbündig),
  trägt Einrückung und die senkrechten Führungslinien der Vorfahren-Ebenen.

## rail (private static)

```java
private static Span rail()
```

- Keine Parameter.
- Rückgabewert (`Span`) — nie `null`; leerer `Span` mit CSS-Klasse `RAIL`.
- Geworfene Exceptions: keine.

## punct (private static)

```java
private static Span punct(String text)
```

- `text` (`String`) — null-erlaubt: **nein, nicht abgesichert.** Anders als bei `token(...)`
  gibt es hier **keine** Null-Normalisierung. `new Span(text)` mit `text == null` — Vaadins
  `Span(String)`-Konstruktor akzeptiert laut Vaadin-Verhalten `null` typischerweise ohne
  eigene Prüfung (setzt intern leeren Text oder wirft, je nach Vaadin-Version, aber das ist
  außerhalb dieser Klasse); in diesem Quellcode selbst gibt es keinen Null-Check und keinen
  `throw`. Alle tatsächlichen Aufrufer im Code übergeben ausschließlich Literal-Strings
  (`"<"`, `">"`, `"/>"`, `"="`, `"\""`, `"</"`, `"<!--"`, `"-->"`, `"<![CDATA["`, `"]]>"`),
  nie `null` — das Risiko ist damit im Projekt praktisch nicht erreichbar, aber die Methode
  selbst validiert nichts.
- Rückgabewert (`Span`) — nie `null` (die Span-Konstruktion selbst schlägt für die
  verwendeten Literal-Argumente nie fehl).
- Geworfene Exceptions: keine explizit; theoretisch NPE-Weiterleitung durch Vaadin bei
  `null`-Argument, aber im Projekt nie mit `null` aufgerufen.

## plain (private static)

```java
private static Span plain(String text)
```

- `text` (`String`) — null-erlaubt: nein, nicht abgesichert (keine Prüfung); alle Aufrufer
  im Code übergeben Literal-Strings (`" "`).
- Rückgabewert (`Span`) — nie `null`.
- Geworfene Exceptions: keine explizit.

## token (private)

```java
private Span token(String cssClass, String text, Element owner)
```

- `cssClass` (`String`) — null-erlaubt: **nein, nicht abgesichert**; wird direkt an
  `span.addClassName(cssClass)` übergeben. Vaadins `addClassName` wirft bei `null` typischerweise
  eine eigene `NullPointerException`/`IllegalArgumentException` (außerhalb dieser Klasse) —
  im Projekt wird `cssClass` immer als `CssClasses`-Konstante übergeben, nie `null`.
- `text` (`String`) — null-erlaubt: **ja, explizit abgesichert.** Der Methodenkörper enthält
  `String value = text == null ? "" : text;` — die einzige Stelle in dieser Klasse, die
  `null` aktiv zu einem sicheren Default (leerer String) normalisiert.
- `owner` (`Element`) — null-erlaubt: ja im Sinne, dass diese Methode `owner` nur in ein
  neues `SearchableToken` einbettet, ohne es zu dereferenzieren.
- Rückgabewert (`Span`) — nie `null`; erzeugt immer einen neuen `Span`, registriert ihn in
  `tokens` und gibt ihn zurück.
- Geworfene Exceptions: keine explizit in dieser Methode; potenziell NPE über
  `span.addClassName(cssClass)`, falls `cssClass == null` (im Projekt nicht erreichbar).
- Nebenwirkung: fügt ein neues [SearchableToken](/api-reference/searchable-token.md) der
  Instanzliste `tokens` hinzu (Aufbau der späteren Suchreihenfolge, Dokumentreihenfolge ==
  Einfügereihenfolge).

## isSingleInlineText (private static)

```java
private static boolean isSingleInlineText(List<Content> meaningful)
```

- `meaningful` (`List<Content>`) — null-erlaubt: nein, nicht abgesichert;
  `meaningful.size()` würde bei `null` eine NPE werfen.
- Rückgabewert (`boolean`) — primitiv, kann nicht `null` sein. `true`, wenn genau ein
  Inhaltselement vorhanden ist, das ein `Text`, aber **kein** `CDATA` ist (da `CDATA extends
  Text` in JDOM2, schließt die explizite `!(... instanceof CDATA)`-Prüfung CDATA-Knoten
  bewusst aus dem Inline-Pfad aus).
- Geworfene Exceptions: implizite NPE bei `meaningful == null`.

## meaningfulContent (private static)

```java
private static List<Content> meaningfulContent(Element element)
```

- `element` (`Element`) — null-erlaubt: nein, nicht abgesichert; `element.getContent()`
  würde bei `null` eine NPE werfen.
- Rückgabewert (`List<Content>`) — nie `null`; immer eine neue `ArrayList` (ggf. leer),
  nie das Ergebnis von `element.getContent()` selbst.
- Geworfene Exceptions: implizite NPE bei `element == null`.
- Filtert aus `element.getContent()` alle Knoten heraus, die dargestellt werden: `CDATA`
  immer, `Text` nur wenn nach `trim()` nicht leer, `Element` und `Comment` immer; alle
  anderen JDOM2-Inhaltstypen (`default`-Zweig im `switch`) werden verworfen.

# Citations

[1] web-common/src/main/java/de/makno/web/common/component/xmlviewer/XmlTreeRenderer.java
