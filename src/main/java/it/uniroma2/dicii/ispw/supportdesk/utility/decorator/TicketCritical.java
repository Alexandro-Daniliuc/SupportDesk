package it.uniroma2.dicii.ispw.supportdesk.utility.decorator;

import it.uniroma2.dicii.ispw.supportdesk.enumerator.Priority;
import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;

public final class TicketCritical extends TicketDecorator {

    private static final String CRITICAL_TAG = "[CRITICO] ";

    public TicketCritical(Ticket ticket) {
        super(ticket);
    }

    @Override
    public String getDisplaySummary() {
        String tag = Priority.CRITICAL.equals(ticket.getPriority()) ? CRITICAL_TAG : "";
        return String.format("%s[#%d] %s (%s)",
                tag, ticket.getId(), ticket.getTitle(), ticket.getStatus().name());
    }
}
