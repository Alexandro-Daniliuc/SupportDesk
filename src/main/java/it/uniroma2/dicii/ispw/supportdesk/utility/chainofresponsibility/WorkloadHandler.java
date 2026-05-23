// Lasciato per scopi dimostrativi — pattern Chain of Responsibility non implementato a livello applicativo.
package it.uniroma2.dicii.ispw.supportdesk.utility.chainofresponsibility;

import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;
import it.uniroma2.dicii.ispw.supportdesk.model.User;

import java.util.List;
import java.util.Map;

public class WorkloadHandler extends AssignmentHandler {

    private static final int MAX_WORKLOAD = 10;
    private final Map<String, Integer> workloadByEmail;

    public WorkloadHandler(Map<String, Integer> workloadByEmail) {
        this.workloadByEmail = workloadByEmail;
    }

    @Override
    protected User tryAssign(Ticket ticket, List<User> technicians) {
        User best = null;
        int minLoad = MAX_WORKLOAD + 1;
        for (User tech : technicians) {
            int load = workloadByEmail.getOrDefault(tech.getEmail(), 0);
            if (load < MAX_WORKLOAD && load < minLoad) {
                minLoad = load;
                best = tech;
            }
        }
        return best;
    }
}
