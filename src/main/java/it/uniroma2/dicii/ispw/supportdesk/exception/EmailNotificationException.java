package it.uniroma2.dicii.ispw.supportdesk.exception;

/**
 * Errore nell'invio di una notifica email.
 */
public class EmailNotificationException extends SupportDeskException {

    public EmailNotificationException(String message) {
        super(message);
    }

    public EmailNotificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
