package it.uniroma2.dicii.ispw.supportdesk.model;

import it.uniroma2.dicii.ispw.supportdesk.enumerator.Category;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Priority;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.TicketStatus;
import it.uniroma2.dicii.ispw.supportdesk.exception.InvalidTransitionException;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Entità centrale del sistema. Contiene la logica della state machine
 * e il calcolo automatico della scadenza SLA.
 */
public class Ticket implements TicketComponent {

    private final int id;
    private final String title;
    private final String description;
    private final Category category;
    private final Priority priority;
    private final LocalDateTime dataApertura;
    private final LocalDateTime scadenzaSla;
    private final String authorEmail;
    private TicketStatus status;
    private User assignedTechnician;

    private Ticket(Builder b) {
        this.id = b.id;
        this.title = b.title;
        this.description = b.description;
        this.category = b.category;
        this.priority = b.priority;
        this.authorEmail = b.authorEmail;
        this.dataApertura = b.dataApertura;
        this.scadenzaSla = b.dataApertura.plusHours(b.priority.getMaxSlaHours());
        this.status = b.status;
    }

    public static class Builder {
        private final int id;
        private final String title;
        private final String description;
        private final Category category;
        private final Priority priority;
        private String authorEmail;
        private LocalDateTime dataApertura = LocalDateTime.now(ZoneId.systemDefault());
        private TicketStatus status = TicketStatus.OPEN;

        public Builder(int id, String title, String description, Category category, Priority priority) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.category = category;
            this.priority = priority;
        }

        public Builder authorEmail(String authorEmail) {
            this.authorEmail = authorEmail;
            return this;
        }

        public Builder dataApertura(LocalDateTime dataApertura) {
            this.dataApertura = dataApertura;
            return this;
        }

        public Builder status(TicketStatus status) {
            this.status = status;
            return this;
        }

        public Ticket build() {
            return new Ticket(this);
        }
    }

    /**
     * Unico metodo business del model: cambia stato rispettando la state machine.
     *
     * @throws InvalidTransitionException se la transizione non è consentita
     */
    public void changeStatus(TicketStatus nuovoStato) throws InvalidTransitionException {
        if (!this.status.canTransitionTo(nuovoStato)) {
            throw new InvalidTransitionException(this.status, nuovoStato);
        }
        this.status = nuovoStato;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public Priority getPriority() {
        return priority;
    }

    public LocalDateTime getDataApertura() {
        return dataApertura;
    }

    public LocalDateTime getScadenzaSla() {
        return scadenzaSla;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public User getAssignedTechnician() {
        return assignedTechnician;
    }

    public void setAssignedTechnician(User technician) {
        this.assignedTechnician = technician;
    }

    @Override
    public String getDisplaySummary() {
        return String.format("[#%d] %s (%s)", id, title, status.name());
    }
}

