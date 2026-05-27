package it.uniroma2.dicii.ispw.supportdesk.utility.strategy;

/**
 * Strategy per la correlazione tra ticket. Ogni implementazione definisce
 * un criterio diverso basandosi esclusivamente sulle metriche pre-calcolate
 * contenute nel {@link CorrelationContext} — non accede ai dati grezzi dei ticket.
 */
public interface CorrelationStrategy {
    boolean isCorrelated(CorrelationContext context);
}
