---
type: API Reference
title: SearchNavigator.setLabelFormatter(...)
description: Methode setLabelFormatter von SearchNavigator - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/navigation/SearchNavigator.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `public void setLabelFormatter(MatchLabelFormatter labelFormatter)`


- `labelFormatter` (`de.makno.web.common.component.navigation.MatchLabelFormatter`) —
  **null-erlaubt: nein**, verifiziert durch `this.labelFormatter =
  Objects.requireNonNull(labelFormatter, "labelFormatter");` als erste Anweisung.

**Was bei ungültiger Eingabe passiert:** Wirft `NullPointerException` mit Nachricht
`"labelFormatter"`, wenn `labelFormatter == null`. Bei gültiger Eingabe wird die Anzeige
sofort mit dem aktuellen Treffer-Stand aktualisiert (`update(navigable.getMatchCount(),
navigable.getCurrentMatchIndex())`).

- Rückgabewert: keiner.

# Citations

[1] [SearchNavigator (Übersicht)](./search-navigator.md)
