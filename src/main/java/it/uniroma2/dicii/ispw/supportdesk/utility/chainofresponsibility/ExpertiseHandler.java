// Lasciato per scopi dimostrativi — pattern Chain of Responsibility non implementato a livello applicativo.
package it.uniroma2.dicii.ispw.supportdesk.utility.chainofresponsibility;

import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;
import it.uniroma2.dicii.ispw.supportdesk.model.User;

import java.util.List;

public class ExpertiseHandler extends AssignmentHandler {

    @Override
    protected User tryAssign(Ticket ticket, List<User> technicians) {
        String category = ticket.getCategory().name();
        for (User tech : technicians) {
            if (category.equalsIgnoreCase(tech.obtainSpecialization())) return tech;
        }
        return null;
    }
}
