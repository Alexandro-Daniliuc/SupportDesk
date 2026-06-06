package it.uniroma2.dicii.ispw.supportdesk.controller.applicativo;

import it.uniroma2.dicii.ispw.supportdesk.dao.PersistenceLayerFactory;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.TicketStatus;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.TicketNotFoundException;
import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;
import it.uniroma2.dicii.ispw.supportdesk.record.TicketRecord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class SLAController {

    private static final Logger log = LoggerFactory.getLogger(SLAController.class);
    private static final long SLA_WARNING_HOURS = 2;

    public void start() {
        log.info("SLA monitoring avviato");
    }

    public void checkSLA(Ticket ticket) {
        if (LocalDateTime.now(ZoneId.systemDefault()).isAfter(ticket.getScadenzaSla())) {
            log.warn("SLA violato per ticket {}", ticket.getId());
        }
    }

    public boolean isSlaViolated(int ticketId) throws DAOException, TicketNotFoundException {
        Ticket ticket = PersistenceLayerFactory.getInstance().getTicketById(ticketId);
        boolean violated = LocalDateTime.now(ZoneId.systemDefault()).isAfter(ticket.getScadenzaSla());
        if (violated) {
            log.warn("SLA violato per ticket {}", ticketId);
        }
        return violated;
    }

    public List<TicketRecord> getTicketsWithSlaExpiringSoon() throws DAOException {
        LocalDateTime threshold = LocalDateTime.now(ZoneId.systemDefault()).plusHours(SLA_WARNING_HOURS);
        return PersistenceLayerFactory.getInstance().findAllTickets().stream()
                .filter(t -> !isTerminated(t) && !t.getScadenzaSla().isAfter(threshold))
                .map(SubmitTicketController::toRecord)
                .toList();
    }

    private boolean isTerminated(Ticket t) {
        return t.getStatus() == TicketStatus.RESOLVED || t.getStatus() == TicketStatus.CLOSED;
    }
}
