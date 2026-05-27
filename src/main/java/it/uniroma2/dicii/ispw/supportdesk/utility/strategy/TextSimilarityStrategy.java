package it.uniroma2.dicii.ispw.supportdesk.utility.strategy;

/**
 * Strategia basata esclusivamente sulla similarità testuale TF-IDF.
 * Due ticket sono correlati se la cosine similarity supera {@value #THRESHOLD}.
 */
public class TextSimilarityStrategy implements CorrelationStrategy {

    private static final double THRESHOLD = 0.30;

    @Override
    public boolean isCorrelated(CorrelationContext context) {
        return context.textSimilarity() >= THRESHOLD;
    }
}
