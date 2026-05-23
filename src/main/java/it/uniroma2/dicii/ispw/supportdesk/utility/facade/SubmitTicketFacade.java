package it.uniroma2.dicii.ispw.supportdesk.utility.facade;

import it.uniroma2.dicii.ispw.supportdesk.bean.TicketBean;
import it.uniroma2.dicii.ispw.supportdesk.controller.applicativo.TicketController;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.ValidationException;
import it.uniroma2.dicii.ispw.supportdesk.record.TicketRecord;
import it.uniroma2.dicii.ispw.supportdesk.utility.observer.ManagerNotificationObserver;
import it.uniroma2.dicii.ispw.supportdesk.utility.observer.TechnicianNotificationObserver;
import it.uniroma2.dicii.ispw.supportdesk.utility.singleton.UserSession;

public final class SubmitTicketFacade {

    private final TicketController ticketController;

    private SubmitTicketFacade() {
        ticketController = new TicketController();
        ticketController.attach(new TechnicianNotificationObserver());
        ticketController.attach(new ManagerNotificationObserver());
    }

    private static final class Holder {
        private static final SubmitTicketFacade INSTANCE = new SubmitTicketFacade();
    }

    public static SubmitTicketFacade getInstanceSingleton() {
        return Holder.INSTANCE;
    }

    public TicketRecord openTicketWithWorkflow(TicketBean bean) throws ValidationException, DAOException {
        if (bean == null || !bean.isValid()) {
            throw new ValidationException("TicketBean non valido o incompleto");
        }
        String authorEmail = UserSession.getInstanceSingleton().getCurrentUser() != null
                ? UserSession.getInstanceSingleton().getCurrentUser().getEmail()
                : "unknown";
        return ticketController.openTicket(bean, authorEmail);
    }
}
