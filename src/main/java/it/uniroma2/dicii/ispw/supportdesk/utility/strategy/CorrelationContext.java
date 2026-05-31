package it.uniroma2.dicii.ispw.supportdesk.utility.strategy;

/**
 * DTO con le metriche di correlazione pre-calcolate da CorrelationEngine,
 * passato alla CorrelationStrategy che decide senza ricalcolare nulla.
 *
 * @param textSimilarity  cosine similarity TF-IDF (0.0-1.0)
 * @param sameCategory    true se i ticket condividono la categoria
 * @param keywordOverlap  indice di Jaccard sulle keyword (0.0-1.0)
 */
public record CorrelationContext(
        double  textSimilarity,
        boolean sameCategory,
        double  keywordOverlap
) {}
