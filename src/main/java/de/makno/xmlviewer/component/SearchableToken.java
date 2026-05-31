package de.makno.xmlviewer.component;

import com.vaadin.flow.component.html.Span;
import org.jdom2.Element;

/**
 * Ein durchsuchbares Text-Token: der gerenderte {@link Span}, sein ursprünglicher Klartext und das
 * besitzende {@link Element} (für das Aufklappen der Vorfahren bei einem Treffer).
 *
 * <p>Der Klartext wird getrennt vom Span gehalten, weil die Suche den Span-Inhalt temporär in
 * mehrere Treffer-/Nicht-Treffer-Spans zerlegt und danach wieder aus {@code text} herstellt.
 */
record SearchableToken(Span span, String text, Element owner) {}
