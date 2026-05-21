package it.uniroma2.dicii.ispw.supportdesk.exception;

/**
 * Errore nel layer di persistenza (DB o file system).
 */
public class DAOException extends SupportDeskException {

    public DAOException(String message) {
        super(message);
    }

    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }
}
