package it.uniroma2.dicii.ispw.supportdesk.exception;

import it.uniroma2.dicii.ispw.supportdesk.enumerator.TicketStatus;

/**
 * Transizione di stato non consentita dalla state machine del ticket.
 */
public class InvalidTransitionException extends SupportDeskException {

    public InvalidTransitionException(TicketStatus from, TicketStatus to) {
        super("Transizione non valida: " + from + " -> " + to);
    }

    public InvalidTransitionException(String message) {
        super(message);
    }
}
