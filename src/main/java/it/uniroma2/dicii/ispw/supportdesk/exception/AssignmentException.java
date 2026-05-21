package it.uniroma2.dicii.ispw.supportdesk.exception;

/**
 * Errore durante l'assegnazione di un ticket a un tecnico.
 */
public class AssignmentException extends SupportDeskException {

    public AssignmentException(String message) {
        super(message);
    }

    public AssignmentException(String message, Throwable cause) {
        super(message, cause);
    }
}
