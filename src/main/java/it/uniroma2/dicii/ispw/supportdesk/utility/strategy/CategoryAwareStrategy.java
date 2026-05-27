package it.uniroma2.dicii.ispw.supportdesk.utility.strategy;

/**
 * Strategia di correlazione multi-criterio. Un ticket candidato è correlato
 * al target se si verifica almeno una delle seguenti condizioni:
 * <ol>
 *   <li>Similarità testuale molto alta (≥ {@value #TEXT_THRESHOLD}),
 *       indipendentemente dalla categoria.</li>
 *   <li>Stessa categoria <em>e</em> similarità testuale moderata
 *       (≥ {@value #CATEGORY_TEXT_THRESHOLD}).</li>
 *   <li>Stessa categoria <em>e</em> almeno una keyword significativa
 *       in comune (keywordOverlap &gt; 0).</li>
 * </ol>
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
