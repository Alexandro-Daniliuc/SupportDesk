package it.uniroma2.dicii.ispw.supportdesk.model;

import it.uniroma2.dicii.ispw.supportdesk.enumerator.Category;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Priority;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.TicketStatus;

import java.time.LocalDateTime;

public interface TicketComponent {
    int getId();
    String getTitle();
    String getDescription();
    Category getCategory();
    Priority getPriority();
    TicketStatus getStatus();
    LocalDateTime getDataApertura();
    LocalDateTime getScadenzaSla();
    String getDisplaySummary();
}
