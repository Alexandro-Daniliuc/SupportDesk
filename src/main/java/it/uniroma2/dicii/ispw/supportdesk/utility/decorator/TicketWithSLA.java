package it.uniroma2.dicii.ispw.supportdesk.utility.decorator;

import it.uniroma2.dicii.ispw.supportdesk.model.TicketComponent;
import it.uniroma2.dicii.ispw.supportdesk.utility.observer.Subject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class TicketWithSLA extends TicketDecorator {

    private static final DateTimeFormatter FMT           = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final String            LABEL_SCADUTO = " [SLA SCADUTO]";

    private final Subject notifier;

    public TicketWithSLA(TicketComponent component, Subject notifier) {
        super(component);
        this.notifier = notifier;
    }

    @Override
    public String getDisplaySummary() {
        String slaFormatted = component.getScadenzaSla() != null
                ? FMT.format(component.getScadenzaSla())
                : "N/A";
        String scadutoTag = isScaduto() ? LABEL_SCADUTO : "";
        return String.format("%s - SLA: %s%s",
                component.getDisplaySummary(), slaFormatted, scadutoTag);
    }

    private boolean isScaduto() {
        return component.getScadenzaSla() != null
                && LocalDateTime.now().isAfter(component.getScadenzaSla());
    }
}
