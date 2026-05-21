package it.uniroma2.dicii.ispw.supportdesk.exception;

/**
 * Ticket non trovato nel sistema (per id o criterio di ricerca).
 */
public class TicketNotFoundException extends SupportDeskException {

    public TicketNotFoundException(String message) {
        super(message);
    }

    public TicketNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
