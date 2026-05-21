package it.uniroma2.dicii.ispw.supportdesk.exception;

/**
 * Eccezione base del sistema SupportDesk.
 * Tutte le eccezioni checked del progetto estendono questa classe.
 */
public abstract class SupportDeskException extends Exception {

    protected SupportDeskException(String message) {
        super(message);
    }

    protected SupportDeskException(String message, Throwable cause) {
        super(message, cause);
    }
}
