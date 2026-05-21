package it.uniroma2.dicii.ispw.supportdesk.exception;

/**
 * Credenziali non valide o utente non autenticato.
 */
public class AuthenticationException extends SupportDeskException {

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
