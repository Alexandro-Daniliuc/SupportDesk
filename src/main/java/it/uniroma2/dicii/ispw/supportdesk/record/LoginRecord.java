package it.uniroma2.dicii.ispw.supportdesk.record;

import it.uniroma2.dicii.ispw.supportdesk.enumerator.Role;

/**
 * Risposta immutabile restituita dal LoginController alla boundary dopo autenticazione.
 */
public record LoginRecord(
        int userId,
        String name,
        String surname,
        String email,
        Role role
) {}
