---
type: API Reference
title: MatchNavigable.addMatchChangeListener(...)
description: Methode addMatchChangeListener von MatchNavigable - siehe Signatur(en) unten.
resource: web-common/src/main/java/de/makno/web/common/component/navigation/MatchNavigable.java
tags: [api-reference, method]
timestamp: '2026-07-08T09:00:00+02:00'
---

## `Registration addMatchChangeListener(ComponentEventListener<MatchChangeEvent> listener)`


- **Parameter:** `listener` (`ComponentEventListener<MatchChangeEvent>`) — null-erlaubt: nicht durch das
  Interface spezifiziert (kein Javadoc-Hinweis, kein `@Nullable`/`@NonNull`). Da es sich um ein Interface
  handelt, ist das tatsächliche Verhalten bei `null` implementierungsabhängig und hier nicht verifizierbar.
- **Rückgabewert:** `Registration` (Vaadin-Typ). Der Javadoc macht keine Aussage über Nullability des
  Rückgabewerts; üblich für Vaadin-`Registration`-APIs ist ein nicht-`null`-Rückgabewert, mit dem der
  Aufrufer die Registrierung später lösen kann (siehe `SearchNavigator.onDetach`, das den Rückgabewert
  ungeprüft in einem Feld hält und `.remove()` darauf aufruft).
- **Exceptions:** keine deklariert.
- **Semantik:** Registriert einen Listener, der bei jeder Änderung der Suchtreffer oder der
  Treffer-Navigation gefeuert wird (siehe [MatchChangeEvent](/api-reference/navigation/match-change-event/match-change-event.md)).

# Citations

[1] [MatchNavigable (Übersicht)](./match-navigable.md)
