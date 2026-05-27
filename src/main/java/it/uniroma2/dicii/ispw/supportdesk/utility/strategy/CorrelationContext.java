package it.uniroma2.dicii.ispw.supportdesk.utility.strategy;

/**
 * DTO che trasporta le metriche di correlazione calcolate da {@code CorrelationEngine}
 * verso una {@code CorrelationStrategy}. La strategy riceve valori già pronti
 * e si limita a decidere — non calcola nulla.
 *
 * @param textSimilarity  cosine similarity TF-IDF tra i testi dei due ticket (0.0 – 1.0)
 * @param sameCategory    true se i due ticket appartengono alla stessa categoria
 * @param keywordOverlap  indice di Jaccard sulle keyword significative (0.0 – 1.0)
 */
public record CorrelationContext(
        double  textSimilarity,
        boolean sameCategory,
        double  keywordOverlap
) {}
