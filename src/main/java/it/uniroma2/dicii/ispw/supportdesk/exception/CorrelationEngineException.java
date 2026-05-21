package it.uniroma2.dicii.ispw.supportdesk.exception;

/**
 * Errore durante l'analisi di correlazione TF-IDF tra ticket.
 */
public class CorrelationEngineException extends SupportDeskException {

    public CorrelationEngineException(String message) {
        super(message);
    }

    public CorrelationEngineException(String message, Throwable cause) {
        super(message, cause);
    }
}
