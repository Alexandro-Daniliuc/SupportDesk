// Lasciato per scopi dimostrativi — pattern Chain of Responsibility non implementato a livello applicativo.
package it.uniroma2.dicii.ispw.supportdesk.controller.applicativo;

import it.uniroma2.dicii.ispw.supportdesk.dao.PersistenceLayer;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Role;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.TicketStatus;
import it.uniroma2.dicii.ispw.supportdesk.exception.AssignmentException;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.InvalidTransitionException;
import it.uniroma2.dicii.ispw.supportdesk.exception.TicketNotFoundException;
import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;
import it.uniroma2.dicii.ispw.supportdesk.model.User;
import it.uniroma2.dicii.ispw.supportdesk.utility.chainofresponsibility.AssignmentHandler;
import it.uniroma2.dicii.ispw.supportdesk.utility.chainofresponsibility.DefaultHandler;
import it.uniroma2.dicii.ispw.supportdesk.utility.chainofresponsibility.ExpertiseHandler;
import it.uniroma2.dicii.ispw.supportdesk.utility.chainofresponsibility.WorkloadHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssignmentController {

    private static final Logger log = LoggerFactory.getLogger(AssignmentController.class);


    public void assign(Ticket ticket)
            throws DAOException, AssignmentException, TicketNotFoundException, InvalidTransitionException {
        List<User> technicians = PersistenceLayer.getInstanceSingleton().findUsersByRole(Role.TECHNICIAN);
        List<Ticket> allTickets = PersistenceLayer.getInstanceSingleton().findAllTickets();
        Map<String, Integer> workloadMap = computeWorkloadMap(allTickets);
        AssignmentHandler chain = buildChain(workloadMap);
        User technician = chain.handle(ticket, technicians);
        ticket.setAssignedTechnician(technician);
        ticket.cambiaStato(TicketStatus.ASSIGNED);
        PersistenceLayer.getInstanceSingleton().updateTicket(ticket);
        if (log.isInfoEnabled()) {
            log.info("Ticket {} assegnato a {}", ticket.getId(), technician.getEmail());
        }
    }

    private AssignmentHandler buildChain(Map<String, Integer> workloadMap) {
        AssignmentHandler expertise = new ExpertiseHandler();
        AssignmentHandler workload  = new WorkloadHandler(workloadMap);
        AssignmentHandler fallback  = new DefaultHandler();
        expertise.setNext(workload);
        workload.setNext(fallback);
        return expertise;
    }

    private Map<String, Integer> computeWorkloadMap(List<Ticket> allTickets) {
        Map<String, Integer> map = new HashMap<>();
        for (Ticket t : allTickets) {
            if (t.getAssignedTechnician() != null && t.getStatus() != TicketStatus.RESOLVED
                    && t.getStatus() != TicketStatus.CLOSED) {
                String email = t.getAssignedTechnician().getEmail();
                map.merge(email, 1, Integer::sum);
            }
        }
        return map;
    }
}
