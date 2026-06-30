package it.uniroma2.dicii.ispw.supportdesk.utility.facade;

import it.uniroma2.dicii.ispw.supportdesk.controller.applicativo.SLAController;
import it.uniroma2.dicii.ispw.supportdesk.controller.applicativo.SubmitTicketController;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.record.TicketRecord;
import it.uniroma2.dicii.ispw.supportdesk.utility.observer.ManagerNotificationObserver;

import java.util.List;

@SuppressWarnings("java:S6548")
public final class SlaFacade {

    private final SLAController    slaController;
    private final SubmitTicketController ticketController;

    private SlaFacade() {
        slaController    = new SLAController();
        ticketController = new SubmitTicketController();
        ticketController.addObserver(new ManagerNotificationObserver());
    }

    private static final class Holder {
        private static final SlaFacade INSTANCE = new SlaFacade();
    }

    public static SlaFacade getInstanceSingleton() {
        return Holder.INSTANCE;
    }

    public List<TicketRecord> getTicketsWithSlaExpiringSoon() throws DAOException {
        return slaController.getTicketsWithSlaExpiringSoon();
    }

    public void rescheduleAllSlaTimers() throws DAOException {
        ticketController.rescheduleAllSlaTimers();
    }
}
