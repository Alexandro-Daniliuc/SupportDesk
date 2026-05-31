package it.uniroma2.dicii.ispw.supportdesk.utility.strategy;

/**
 * Strategy (GoF) multi-criterio per la correlazione tra ticket.
 * Correla se: alta similarità TF-IDF, oppure stessa categoria
 * con similarità moderata o almeno una keyword in comune.
 */
public class CategoryAwareStrategy implements CorrelationStrategy {

    private static final double TEXT_THRESHOLD          = 0.30;
    private static final double CATEGORY_TEXT_THRESHOLD = 0.10;

    @Override
    public boolean isCorrelated(CorrelationContext context) {
        boolean highTextSimilarity  = context.textSimilarity() >= TEXT_THRESHOLD;
        boolean categoryTextMatch   = context.sameCategory()
                                      && context.textSimilarity() >= CATEGORY_TEXT_THRESHOLD;
        boolean categoryKeywordMatch = context.sameCategory()
                                      && context.keywordOverlap() > 0.0;
        return highTextSimilarity || categoryTextMatch || categoryKeywordMatch;
    }
}
