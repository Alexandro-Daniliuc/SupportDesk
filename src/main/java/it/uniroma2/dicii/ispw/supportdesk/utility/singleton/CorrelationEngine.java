package it.uniroma2.dicii.ispw.supportdesk.utility.singleton;

import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;
import it.uniroma2.dicii.ispw.supportdesk.utility.strategy.CategoryAwareStrategy;
import it.uniroma2.dicii.ispw.supportdesk.utility.strategy.CorrelationContext;
import it.uniroma2.dicii.ispw.supportdesk.utility.strategy.CorrelationStrategy;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("java:S6548")
public final class CorrelationEngine {

    private CorrelationStrategy strategy;

    private CorrelationEngine() {
        this.strategy = new CategoryAwareStrategy();
    }

    private static final class Holder {
        private static final CorrelationEngine INSTANCE = new CorrelationEngine();
    }

    public static CorrelationEngine getInstanceSingleton() {
        return Holder.INSTANCE;
    }

    public void setStrategy(CorrelationStrategy strategy) {
        this.strategy = strategy;
    }


    // API pubblica


    public List<Ticket> findCorrelatedTickets(Ticket target, List<Ticket> candidates) {
        List<String> docs  = prepareDocuments(target, candidates);
        Set<String>  vocab = buildVocabulary(docs);
        Map<Integer, double[]> vecs = calculateTFIDFVectors(docs, vocab);
        return filterByStrategy(target, candidates, vecs);
    }


    // Preparazione documenti TF-IDF


    private List<String> prepareDocuments(Ticket target, List<Ticket> candidates) {
        List<String> docs = new ArrayList<>();
        docs.add(normalize(target.getTitle() + " " + target.getDescription()));
        for (Ticket c : candidates) {
            docs.add(normalize(c.getTitle() + " " + c.getDescription()));
        }
        return docs;
    }

    private Set<String> buildVocabulary(List<String> docs) {
        Set<String> vocab = new LinkedHashSet<>();
        for (String doc : docs) {
            Collections.addAll(vocab, doc.split("\\s+"));
        }
        return vocab;
    }

    private Map<Integer, double[]> calculateTFIDFVectors(List<String> docs, Set<String> vocab) {
        String[] terms = vocab.toArray(new String[0]);
        Map<Integer, double[]> vecs = new LinkedHashMap<>();
        for (int i = 0; i < docs.size(); i++) {
            vecs.put(i, tfidfVector(docs.get(i), docs, terms));
        }
        return vecs;
    }

    private double[] tfidfVector(String doc, List<String> allDocs, String[] terms) {
        String[] words = doc.split("\\s+");
        double[] vec   = new double[terms.length];
        for (int j = 0; j < terms.length; j++) {
            final int idx = j;
            long tf = Arrays.stream(words).filter(w -> w.equals(terms[idx])).count();
            long df = allDocs.stream().filter(d -> d.contains(terms[idx])).count();
            vec[j]  = (tf > 0 && df > 0)
                    ? ((double) tf / words.length) * Math.log((double) allDocs.size() / df)
                    : 0;
        }
        return vec;
    }


    // Calcolo metriche e delegazione alla strategy


    private List<Ticket> filterByStrategy(Ticket target, List<Ticket> candidates,
                                          Map<Integer, double[]> vecs) {
        double[] targetVec = vecs.get(0);
        List<Ticket> correlated = new ArrayList<>();
        for (int i = 0; i < candidates.size(); i++) {
            Ticket candidate = candidates.get(i);
            CorrelationContext ctx = new CorrelationContext(
                    cosine(targetVec, vecs.get(i + 1)),
                    candidate.getCategory() == target.getCategory(),
                    computeKeywordOverlap(target, candidate)
            );
            if (strategy.isCorrelated(ctx)) {
                correlated.add(candidate);
            }
        }
        return correlated;
    }


    // Keyword overlap (indice di Jaccard su parole significative)


    private double computeKeywordOverlap(Ticket target, Ticket candidate) {
        Set<String> targetWords    = extractKeywords(target);
        Set<String> candidateWords = extractKeywords(candidate);
        if (targetWords.isEmpty() || candidateWords.isEmpty()) return 0.0;
        Set<String> intersection = new HashSet<>(targetWords);
        intersection.retainAll(candidateWords);
        Set<String> union = new HashSet<>(targetWords);
        union.addAll(candidateWords);
        return (double) intersection.size() / union.size();
    }

    /** Estrae le parole significative (lunghezza > 3) dal testo normalizzato del ticket. */
    private Set<String> extractKeywords(Ticket t) {
        return Arrays.stream(normalize(t.getTitle() + " " + t.getDescription()).split("\\s+"))
                .filter(w -> w.length() > 3)
                .collect(Collectors.toSet());
    }


    // Utility


    private double cosine(double[] a, double[] b) {
        double dot = 0;
        double na  = 0;
        double nb  = 0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            na  += a[i] * a[i];
            nb  += b[i] * b[i];
        }
        return (na == 0 || nb == 0) ? 0 : dot / (Math.sqrt(na) * Math.sqrt(nb));
    }

    private String normalize(String text) {
        return text.toLowerCase(Locale.ITALIAN)
                   .replaceAll("[^a-zàèéìòù\\s]", " ")
                   .trim();
    }
}
