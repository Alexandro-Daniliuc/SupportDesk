package it.uniroma2.dicii.ispw.supportdesk.record;

import it.uniroma2.dicii.ispw.supportdesk.enumerator.Category;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Priority;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.TicketStatus;

import java.time.LocalDateTime;

/**
 * Snapshot immutabile di un Ticket restituito dal SubmitTicketController alla boundary.
 * Non espone mai l'entità model Ticket.
 */
public record TicketRecord(
        int id,
        String title,
        String description,
        Category category,
        Priority priority,
        TicketStatus status,
        LocalDateTime dataApertura,
        LocalDateTime scadenzaSla,
        String assignedTechnicianName
) {
    public int getId()                        { return id; }
    public String getTitle()                  { return title; }
    public String getDescription()            { return description; }
    public String getCategory()               { return category != null ? category.name() : ""; }
    public String getPriority()               { return priority != null ? priority.name() : ""; }
    public String getStatus()                 { return status != null ? status.name() : ""; }
    public String getDataApertura()           { return dataApertura != null ? dataApertura.toString() : ""; }
    public String getScadenzaSla()            { return scadenzaSla != null ? scadenzaSla.toString() : "N/A"; }
    public String getAssignedTechnicianName() { return assignedTechnicianName != null ? assignedTechnicianName : ""; }
}
