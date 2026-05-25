package it.uniroma2.dicii.ispw.supportdesk.utility.decorator;

import it.uniroma2.dicii.ispw.supportdesk.enumerator.Category;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Priority;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.TicketStatus;
import it.uniroma2.dicii.ispw.supportdesk.model.TicketComponent;

import java.time.LocalDateTime;

public abstract class TicketDecorator implements TicketComponent {

    protected final TicketComponent component;

    protected TicketDecorator(TicketComponent component) {
        this.component = component;
    }

    @Override public int getId()                     { return component.getId(); }
    @Override public String getTitle()               { return component.getTitle(); }
    @Override public String getDescription()         { return component.getDescription(); }
    @Override public Category getCategory()          { return component.getCategory(); }
    @Override public Priority getPriority()          { return component.getPriority(); }
    @Override public TicketStatus getStatus()        { return component.getStatus(); }
    @Override public LocalDateTime getDataApertura() { return component.getDataApertura(); }
    @Override public LocalDateTime getScadenzaSla()  { return component.getScadenzaSla(); }
    @Override public String getDisplaySummary()      { return component.getDisplaySummary(); }
}
