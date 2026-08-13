package it.uniroma2.dicii.ispw.supportdesk.record;

import it.uniroma2.dicii.ispw.supportdesk.enumerator.Role;

/**
 * Snapshot immutabile di un User restituito dai controller alla boundary.
 */
public record UserRecord(
        int id,
        String name,
        String surname,
        String email,
        Role role,
        String specialization
) {}
