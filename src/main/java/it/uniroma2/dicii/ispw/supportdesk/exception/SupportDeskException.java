package it.uniroma2.dicii.ispw.supportdesk.exception;


public abstract class SupportDeskException extends Exception {

    protected SupportDeskException(String message) {
        super(message);
    }

    protected SupportDeskException(String message, Throwable cause) {
        super(message, cause);
    }
}
