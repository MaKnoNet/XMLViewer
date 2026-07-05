---
type: API Reference
title: SearchableToken
description: Package-private Record, der einen durchsuchbaren Text-Span mit seinem Klartext und dem besitzenden Element bündelt.
resource: web-common/src/main/java/de/makno/web/common/component/xmlviewer/SearchableToken.java
tags: [api-reference, xmlviewer, record, search]
timestamp: '2026-07-07T10:00:00+02:00'
---

# Überblick

`SearchableToken` ist ein package-private `record`, der einen gerenderten `Span`
zusammen mit dessen ursprünglichem Klartext und dem besitzenden `org.jdom2.Element`
bündelt. Er implementiert `Serializable` (explizite `serialVersionUID = 1L`). Der
Klartext wird bewusst getrennt vom `Span` gehalten, weil die Suchlogik den Span-Inhalt
temporär in mehrere Treffer-/Nicht-Treffer-Teilspans zerlegt und danach aus `text` wieder
herstellt — `text` ist damit die "Quelle der Wahrheit", der `Span`-Inhalt ist ein
veränderlicher Rendering-Zustand.

**Thread-Safety:** Der Record ist unveränderlich (keine Setter, alle Komponenten `final`).
Der referenzierte `Span` ist eine Vaadin-Komponente und damit an die Session/den
UI-Thread des [XmlViewer](/api-reference/xml-viewer.md) gebunden, der ihn erzeugt hat —
kein geteilter Zustand zwischen Sessions.

# Vererbungshierarchie

**Vorwärts (eigene Deklaration):** `record SearchableToken(Span span, String text, Element
owner) implements Serializable` (package-private).

- **Superklasse:** implizit `java.lang.Record` — JDK-Typ, kein Cross-Link.
- **Interfaces:**
  - `java.io.Serializable` — JDK-Standard-Interface (Marker-Interface, keine Methoden), kein
    Projekt-Typ, daher kein Cross-Link.
- Records sind implizit `final` — es kann keine Subklasse geben.

**Rückwärts (Abhängige):** Verifiziert per Grep auf `extends SearchableToken` über den
gesamten `web-common/src/main/java/de/makno/web/common/component/`-Baum — **kein Treffer**
(erwartungsgemäß, Records können nicht erweitert werden). `SearchableToken` wird von
[XmlTreeRenderer](/api-reference/xml-tree-renderer.md) erzeugt und ist Teil der Token-Liste
in [RenderedTree](/api-reference/rendered-tree.md) (Assoziation, nicht Vererbung).

# Konstruktoren

Kanonischer (impliziter) Record-Konstruktor:

```java
SearchableToken(Span span, String text, Element owner)
```

Kein kompakter Konstruktor im Quellcode — der kanonische Konstruktor übernimmt die Werte
unverändert, **keine Validierung**:

- `span` (`Span`) — null-erlaubt: ja, verifiziert durch Fehlen jeglicher Prüfung. In der
  Praxis erzeugt `XmlTreeRenderer.token(...)` immer einen echten `new Span(value)`, nie
  `null`, aber der Record-Konstruktor selbst würde `null` nicht ablehnen.
- `text` (`String`) — null-erlaubt: ja laut Konstruktor (keine Prüfung). Praktisch wird
  beim einzigen Aufrufer (`XmlTreeRenderer.token`) vorher `text == null ? "" : text`
  angewendet, sodass `text` im gerenderten Baum nie `null` ist — diese Absicherung liegt
  aber im Aufrufer, nicht im Record selbst.
- `owner` (`Element`) — null-erlaubt: ja laut Konstruktor (keine Prüfung); im Projekt
  stets das jeweilige `org.jdom2.Element`, dem der Token-Text zugeordnet ist (nie `null`
  beobachtet, aber nicht erzwungen).

**Was bei ungültiger Eingabe passiert:** Nichts im Konstruktor — kein `throw`. Wird z.&nbsp;B.
`owner = null` übergeben, wirkt sich das erst später aus: `XmlViewer.toSearchTokens(...)`
ruft `token.owner()` als Schlüssel eines `IdentityHashMap` auf — `IdentityHashMap` erlaubt
`null`-Schlüssel, sodass dies nicht sofort scheitert, aber `expandTo(null)` würde in der
`for`-Schleife (`ancestor = element; ancestor != null; ...`) einfach null Iterationen
durchlaufen (kein Effekt, keine Exception).

# Methoden

`SearchableToken` deklariert selbst keine eigenen Methoden. Automatisch generiert:

- `Span span()` — Rückgabewert kann `null` sein, falls beim Aufbau `null` übergeben wurde
  (siehe oben); praktisch immer ein echter `Span`.
- `String text()` — Rückgabewert kann theoretisch `null` sein laut Konstruktorvertrag,
  praktisch nie (siehe Absicherung im Aufrufer oben). Wird von
  `XmlViewer.toSearchTokens(...)` verwendet, um den `SearchToken`-Text an die
  Such-Engine zu übergeben, und von `XmlViewer.searchableTexts()` (paketsichtbarer
  Test-Helfer) direkt zurückgegeben.
- `Element owner()` — Rückgabewert kann theoretisch `null` sein (keine Prüfung); wird
  von `XmlViewer.toSearchTokens(...)` als Schlüssel für den identitätsbasierten
  Reveal-Cache verwendet, damit alle Tokens desselben Elements dieselbe
  Aufklapp-Aktion (`expandTo(owner)`) teilen.
- `equals(Object)`, `hashCode()`, `toString()` — Standard-Record-Implementierungen,
  feldbasiert über `span`, `text`, `owner` (bei `Span` faktisch Identitätsvergleich, da
  Vaadin-Komponenten kein `equals` überschreiben).

Keine dieser Methoden wirft eine Exception.

# Citations

[1] web-common/src/main/java/de/makno/web/common/component/xmlviewer/SearchableToken.java
