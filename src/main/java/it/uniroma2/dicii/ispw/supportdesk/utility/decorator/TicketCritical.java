package it.uniroma2.dicii.ispw.supportdesk.utility.decorator;

import it.uniroma2.dicii.ispw.supportdesk.enumerator.Priority;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.TicketStatus;
import it.uniroma2.dicii.ispw.supportdesk.exception.InvalidTransitionException;
import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;
import it.uniroma2.dicii.ispw.supportdesk.utility.observer.EventType;
import it.uniroma2.dicii.ispw.supportdesk.utility.observer.Subject;

public final class TicketCritical extends TicketDecorator {

    private static final String CRITICAL_TAG = "[CRITICO] ";

    private final Subject notifier;

    public TicketCritical(Ticket ticket, Subject notifier) {
        super(ticket);
        this.notifier = notifier;
    }

    @Override
    public void changeStatus(TicketStatus newStatus) throws InvalidTransitionException {
        ticket.changeStatus(newStatus);
        notifier.notifyObservers(EventType.TICKET_CAMBIO_STATO, ticket);
    }

    @Override
    public String getDisplaySummary() {
        String tag = Priority.CRITICAL.equals(ticket.getPriority()) ? CRITICAL_TAG : "";
        return String.format("%s[#%d] %s (%s)",
                tag, ticket.getId(), ticket.getTitle(), ticket.getStatus().name());
    }
}
