package it.uniroma2.dicii.ispw.supportdesk.utility.decorator;

import it.uniroma2.dicii.ispw.supportdesk.enumerator.Priority;
import it.uniroma2.dicii.ispw.supportdesk.model.TicketComponent;

public final class TicketCritical extends TicketDecorator {

    private static final String CRITICAL_TAG = "[CRITICO] ";

    public TicketCritical(TicketComponent component) {
        super(component);
    }

    @Override
    public String getDisplaySummary() {
        String tag = Priority.CRITICAL.equals(component.getPriority()) ? CRITICAL_TAG : "";
        return tag + component.getDisplaySummary();
    }
}
