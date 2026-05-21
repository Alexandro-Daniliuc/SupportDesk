package it.uniroma2.dicii.ispw.supportdesk.utility.factory;

import it.uniroma2.dicii.ispw.supportdesk.enumerator.Role;
import it.uniroma2.dicii.ispw.supportdesk.model.Notification;

public final class NotificationFactory {

    private static final String MSG_TICKET_OPEN      = "Nuovo ticket #%d aperto";
    private static final String MSG_SLA_VIOLATION    = "SLA violato per ticket #%d";
    private static final String MSG_TICKET_ASSIGNED  = "Ticket #%d assegnato al tecnico";
    private static final String MSG_STATE_CHANGED    = "Ticket #%d: cambio stato";

    private NotificationFactory() {}

    public static Notification forTicketOpen(int ticketId) {
        return new Notification(0, String.format(MSG_TICKET_OPEN, ticketId), Role.MANAGER, ticketId);
    }

    public static Notification forSlaViolation(int ticketId) {
        return new Notification(0, String.format(MSG_SLA_VIOLATION, ticketId), Role.MANAGER, ticketId);
    }

    public static Notification forTicketAssigned(int ticketId, Role targetRole) {
        return new Notification(0, String.format(MSG_TICKET_ASSIGNED, ticketId), targetRole, ticketId);
    }

    public static Notification forStateChanged(int ticketId, Role targetRole) {
        return new Notification(0, String.format(MSG_STATE_CHANGED, ticketId), targetRole, ticketId);
    }
}
