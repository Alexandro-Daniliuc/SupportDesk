package it.uniroma2.dicii.ispw.supportdesk.utility.strategy;

/**
 * Interfaccia Strategy (GoF) per la correlazione tra ticket.
 * Ogni implementazione valuta le metriche del CorrelationContext
 * senza accedere ai dati grezzi del ticket.
 */
public interface CorrelationStrategy {
    boolean isCorrelated(CorrelationContext context);
}
