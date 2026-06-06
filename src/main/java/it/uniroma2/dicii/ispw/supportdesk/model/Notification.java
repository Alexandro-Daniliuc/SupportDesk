package it.uniroma2.dicii.ispw.supportdesk.model;

import it.uniroma2.dicii.ispw.supportdesk.enumerator.Role;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Notifica generata dal sistema verso un utente. Dumb data holder.
 */
public class Notification {

    private final int id;
    private final String message;
    private final Role targetRole;
    private final int ticketId;
    private final LocalDateTime createdAt;
    private boolean read;

    public Notification(int id, String message, Role targetRole, int ticketId) {
        this.id = id;
        this.message = message;
        this.targetRole = targetRole;
        this.ticketId = ticketId;
        this.createdAt = LocalDateTime.now(ZoneId.systemDefault());
        this.read = false;
    }

    public Notification(int id, String message, Role targetRole, int ticketId, LocalDateTime createdAt, boolean read) {
        this.id = id;
        this.message = message;
        this.targetRole = targetRole;
        this.ticketId = ticketId;
        this.createdAt = createdAt;
        this.read = read;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public Role getTargetRole() {
        return targetRole;
    }

    public int getTicketId() {
        return ticketId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isRead() {
        return read;
    }

    public void markAsRead() {
        this.read = true;
    }
}
