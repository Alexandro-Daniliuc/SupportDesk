// Lasciato per scopi dimostrativi — pattern Chain of Responsibility non implementato a livello applicativo.
package it.uniroma2.dicii.ispw.supportdesk.utility.chainofresponsibility;

import it.uniroma2.dicii.ispw.supportdesk.exception.AssignmentException;
import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;
import it.uniroma2.dicii.ispw.supportdesk.model.User;

import java.util.List;

public abstract class AssignmentHandler {

    private AssignmentHandler next;

    public AssignmentHandler setNext(AssignmentHandler next) {
        this.next = next;
        return next;
    }

    public final User handle(Ticket ticket, List<User> technicians) throws AssignmentException {
        User result = tryAssign(ticket, technicians);
        if (result != null) return result;
        if (next != null) return next.handle(ticket, technicians);
        throw new AssignmentException("Nessun handler in grado di assegnare il ticket: " + ticket.getId());
    }

    protected abstract User tryAssign(Ticket ticket, List<User> technicians);
}
