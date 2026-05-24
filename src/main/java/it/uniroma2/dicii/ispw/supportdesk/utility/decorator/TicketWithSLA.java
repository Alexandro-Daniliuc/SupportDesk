package it.uniroma2.dicii.ispw.supportdesk.utility.decorator;

import it.uniroma2.dicii.ispw.supportdesk.enumerator.TicketStatus;
import it.uniroma2.dicii.ispw.supportdesk.exception.InvalidTransitionException;
import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;
import it.uniroma2.dicii.ispw.supportdesk.utility.observer.Subject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class TicketWithSLA extends TicketDecorator {

    private static final DateTimeFormatter FMT          = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final String            LABEL_SCADUTO = " [SLA SCADUTO]";

    private final Subject notifier;

    public TicketWithSLA(Ticket ticket, Subject notifier) {
        super(ticket);
        this.notifier = notifier;
    }

    @Override
    public void changeStatus(TicketStatus newStatus) throws InvalidTransitionException {
        ticket.changeStatus(newStatus);
    }

    @Override
    public String getDisplaySummary() {
        String slaFormatted = ticket.getScadenzaSla() != null
                ? FMT.format(ticket.getScadenzaSla())
                : "N/A";
        String scadutoTag = isScaduto() ? LABEL_SCADUTO : "";
        return String.format("[#%d] %s - SLA: %s%s",
                ticket.getId(), ticket.getTitle(), slaFormatted, scadutoTag);
    }

    private boolean isScaduto() {
        return ticket.getScadenzaSla() != null
                && LocalDateTime.now().isAfter(ticket.getScadenzaSla());
    }
}
