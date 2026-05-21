package it.uniroma2.dicii.ispw.supportdesk.exception;

/**
 * Input non valido fornito dall'utente o da un Bean.
 */
public class ValidationException extends SupportDeskException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
