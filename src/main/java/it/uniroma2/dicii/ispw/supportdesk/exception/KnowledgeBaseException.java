package it.uniroma2.dicii.ispw.supportdesk.exception;

/**
 * Errore durante l'accesso o la modifica della knowledge base.
 */
public class KnowledgeBaseException extends SupportDeskException {

    public KnowledgeBaseException(String message) {
        super(message);
    }

    public KnowledgeBaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
