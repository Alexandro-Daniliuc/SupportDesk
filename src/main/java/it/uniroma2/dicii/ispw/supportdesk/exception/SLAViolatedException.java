package it.uniroma2.dicii.ispw.supportdesk.exception;

/**
 * SLA scaduto senza che il ticket sia stato risolto.
 */
public class SLAViolatedException extends SupportDeskException {

    public SLAViolatedException(String message) {
        super(message);
    }

    public SLAViolatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
