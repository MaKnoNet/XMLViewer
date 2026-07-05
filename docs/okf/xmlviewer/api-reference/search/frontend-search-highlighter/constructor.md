---
type: API Reference
title: FrontendSearchHighlighter – Konstruktoren
description: Alle Konstruktoren von FrontendSearchHighlighter.
resource: web-common/src/main/java/de/makno/web/common/component/search/FrontendSearchHighlighter.java
tags: [api-reference, constructor]
timestamp: '2026-07-08T09:00:00+02:00'
---


## `FrontendSearchHighlighter(Component host)`

```java
public FrontendSearchHighlighter(Component host)
```

- `host` (`Component`) — null-erlaubt: **nein**. Verifiziert durch
  `this.host = Objects.requireNonNull(host, "host");` — wirft `NullPointerException` mit
  Nachricht `"host"` bei `null`.
- **Geworfene Exceptions:** `NullPointerException`, wenn `host == null`.
- Die Wurzel-Element von `host` (`this` im JS-Kontext) dient dem Highlighter als
  Geltungsbereich; mehrere Komponenten je Seite werden über dieses Wurzel-Scoping
  unterstützt.

# Citations

[1] [FrontendSearchHighlighter (Übersicht)](./frontend-search-highlighter.md)
