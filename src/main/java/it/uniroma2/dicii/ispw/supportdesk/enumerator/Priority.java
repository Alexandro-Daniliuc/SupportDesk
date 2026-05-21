package it.uniroma2.dicii.ispw.supportdesk.enumerator;

/**
 * Priorità di un ticket con SLA massimo incorporato.
 * Le ore SLA sono parte del dominio: appartengono qui, non in un file di config.
 */
public enum Priority {

    LOW(72),
    MEDIUM(24),
    HIGH(8),
    CRITICAL(4);

    private final int maxSlaHours;

    Priority(int maxSlaHours) {
        this.maxSlaHours = maxSlaHours;
    }

    /**
     * Ore massime entro cui il ticket deve essere risolto.
     */
    public int getMaxSlaHours() {
        return maxSlaHours;
    }
}
