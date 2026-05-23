package it.uniroma2.dicii.ispw.supportdesk.utility.facade;

import it.uniroma2.dicii.ispw.supportdesk.controller.applicativo.CorrelationController;
import it.uniroma2.dicii.ispw.supportdesk.dao.PersistenceLayer;
import it.uniroma2.dicii.ispw.supportdesk.exception.CorrelationEngineException;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.TicketNotFoundException;
import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;
import it.uniroma2.dicii.ispw.supportdesk.record.TicketRecord;

import java.util.List;

public final class CorrelationFacade {

    private final CorrelationController correlationController;

    private CorrelationFacade() {
        correlationController = new CorrelationController();
    }

    private static final class Holder {
        private static final CorrelationFacade INSTANCE = new CorrelationFacade();
    }

    public static CorrelationFacade getInstanceSingleton() {
        return Holder.INSTANCE;
    }

    public List<TicketRecord> findCorrelations(int ticketId)
            throws DAOException, TicketNotFoundException, CorrelationEngineException {
        Ticket target = PersistenceLayer.getInstanceSingleton().getTicketById(ticketId);
        return correlationController.findCorrelations(target);
    }
}
