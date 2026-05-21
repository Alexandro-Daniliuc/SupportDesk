// Lasciato per scopi dimostrativi — pattern Chain of Responsibility non implementato a livello applicativo.
package it.uniroma2.dicii.ispw.supportdesk.utility.chainofresponsibility;

import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;
import it.uniroma2.dicii.ispw.supportdesk.model.User;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultHandler extends AssignmentHandler {

    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    @Override
    protected User tryAssign(Ticket ticket, List<User> technicians) {
        if (technicians.isEmpty()) return null;
        return technicians.get(COUNTER.getAndIncrement() % technicians.size());
    }
}
