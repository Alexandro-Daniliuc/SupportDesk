package it.uniroma2.dicii.ispw.supportdesk.exception;

public class ValidationException extends SupportDeskException {

    private final String field;

    public ValidationException(String message) {
        super(message);
        this.field = null;
    }

    public ValidationException(String field, String message) {
        super(message);
        this.field = field;
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
        this.field = null;
    }

    public String getField() {
        return field;
    }

    public boolean hasField() {
        return field != null;
    }
}
