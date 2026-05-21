package it.uniroma2.dicii.ispw.supportdesk.exception;

/**
 * Errore durante la generazione di un report.
 */
public class ReportException extends SupportDeskException {

    public ReportException(String message) {
        super(message);
    }

    public ReportException(String message, Throwable cause) {
        super(message, cause);
    }
}
