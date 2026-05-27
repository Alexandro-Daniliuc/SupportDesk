package it.uniroma2.dicii.ispw.supportdesk.utility.facade;

import it.uniroma2.dicii.ispw.supportdesk.bean.TicketBean;
import it.uniroma2.dicii.ispw.supportdesk.controller.applicativo.SubmitTicketController;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.TicketStatus;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.InvalidTransitionException;
import it.uniroma2.dicii.ispw.supportdesk.exception.TicketNotFoundException;
import it.uniroma2.dicii.ispw.supportdesk.exception.ValidationException;
import it.uniroma2.dicii.ispw.supportdesk.record.TicketRecord;
import it.uniroma2.dicii.ispw.supportdesk.utility.observer.ManagerNotificationObserver;
import it.uniroma2.dicii.ispw.supportdesk.utility.observer.TechnicianNotificationObserver;
import it.uniroma2.dicii.ispw.supportdesk.utility.singleton.UserSession;

public final class TicketFacade {

    private final SubmitTicketController ticketCtrl;

    private TicketFacade() {
        ticketCtrl = new SubmitTicketController();
        ticketCtrl.addObserver(new TechnicianNotificationObserver());
        ticketCtrl.addObserver(new ManagerNotificationObserver());
    }

    private static final class Holder {
        private static final TicketFacade INSTANCE = new TicketFacade();
    }

    public static TicketFacade getInstanceSingleton() {
        return Holder.INSTANCE;
    }

    public TicketBean openTicketWithWorkflow(TicketBean bean) throws ValidationException, DAOException {
        if (bean == null) {
            throw new ValidationException("TicketBean non può essere null");
        }
        String authorEmail = UserSession.getInstanceSingleton().getEmail();
        TicketRecord record = ticketCtrl.openTicket(bean, authorEmail);
        bean.setId(record.id());
        return bean;
    }

    public void closeTicketWithWorkflow(int ticketId)
            throws DAOException, TicketNotFoundException, InvalidTransitionException {
        ticketCtrl.changeStatus(ticketId, TicketStatus.CLOSED);
    }
}
