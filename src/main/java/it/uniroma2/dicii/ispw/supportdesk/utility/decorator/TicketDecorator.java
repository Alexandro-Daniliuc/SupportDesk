package it.uniroma2.dicii.ispw.supportdesk.utility.decorator;

import it.uniroma2.dicii.ispw.supportdesk.enumerator.Category;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Priority;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.TicketStatus;
import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;

import java.time.LocalDateTime;

public abstract class TicketDecorator implements TicketComponent {

    protected final Ticket ticket;

    protected TicketDecorator(Ticket ticket) {
        this.ticket = ticket;
    }

    public int getId()                    { return ticket.getId(); }
    public String getTitle()              { return ticket.getTitle(); }
    public String getDescription()        { return ticket.getDescription(); }
    public Category getCategory()         { return ticket.getCategory(); }
    public Priority getPriority()         { return ticket.getPriority(); }
    public TicketStatus getStatus()       { return ticket.getStatus(); }
    public LocalDateTime getDataApertura(){ return ticket.getDataApertura(); }
    public LocalDateTime getScadenzaSla() { return ticket.getScadenzaSla(); }
}
