package it.uniroma2.dicii.ispw.supportdesk.controller.applicativo;

import it.uniroma2.dicii.ispw.supportdesk.dao.PersistenceLayerFactory;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Role;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.TicketStatus;
import it.uniroma2.dicii.ispw.supportdesk.exception.AssignmentException;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.InvalidTransitionException;
import it.uniroma2.dicii.ispw.supportdesk.exception.TicketNotFoundException;
import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;
import it.uniroma2.dicii.ispw.supportdesk.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssignmentController {

    private static final Logger log = LoggerFactory.getLogger(AssignmentController.class);

    public void assign(Ticket ticket)
            throws DAOException, AssignmentException, TicketNotFoundException, InvalidTransitionException {
        List<User> technicians = PersistenceLayerFactory.getInstance().findUsersByRole(Role.TECHNICIAN);
        if (technicians.isEmpty()) {
            throw new AssignmentException("Nessun tecnico disponibile");
        }
        List<Ticket> allTickets = PersistenceLayerFactory.getInstance().findAllTickets();
        Map<String, Integer> workloadMap = computeWorkloadMap(allTickets);
        User technician = pickLeastLoaded(technicians, workloadMap);
        ticket.setAssignedTechnician(technician);
        ticket.changeStatus(TicketStatus.ASSIGNED);
        PersistenceLayerFactory.getInstance().updateTicket(ticket);
        if (log.isInfoEnabled()) {
            log.info("Ticket {} assegnato a {}", ticket.getId(), technician.getEmail());
        }
    }

    private User pickLeastLoaded(List<User> technicians, Map<String, Integer> workloadMap) {
        return technicians.stream()
                .min((a, b) -> {
                    int wa = workloadMap.getOrDefault(a.getEmail(), 0);
                    int wb = workloadMap.getOrDefault(b.getEmail(), 0);
                    return Integer.compare(wa, wb);
                })
                .orElse(technicians.get(0));
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
