package it.uniroma2.dicii.ispw.supportdesk.utility.decorator;

import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class TicketWithSLA extends TicketDecorator {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final String LABEL_SCADUTO = " [SLA SCADUTO]";

    public TicketWithSLA(Ticket ticket) {
        super(ticket);
    }

    @Override
    public String getDisplaySummary() {
        String slaFormatted = ticket.getScadenzaSla() != null
                ? FMT.format(ticket.getScadenzaSla())
                : "N/A";
        String scadutoTag = isScaduto() ? LABEL_SCADUTO : "";
        return String.format("[#%d] %s — SLA: %s%s",
                ticket.getId(), ticket.getTitle(), slaFormatted, scadutoTag);
    }

    private boolean isScaduto() {
        return ticket.getScadenzaSla() != null
                && LocalDateTime.now().isAfter(ticket.getScadenzaSla());
    }
}
