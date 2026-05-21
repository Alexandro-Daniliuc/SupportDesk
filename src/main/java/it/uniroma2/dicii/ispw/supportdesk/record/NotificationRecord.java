package it.uniroma2.dicii.ispw.supportdesk.record;

import it.uniroma2.dicii.ispw.supportdesk.enumerator.Role;

import java.time.LocalDateTime;

/**
 * Snapshot immutabile di una Notification restituito alla boundary.
 */
public record NotificationRecord(
        int id,
        String message,
        Role targetRole,
        int ticketId,
        LocalDateTime createdAt,
        boolean read
) {}
