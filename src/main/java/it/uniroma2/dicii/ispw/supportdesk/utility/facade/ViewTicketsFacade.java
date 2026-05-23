package it.uniroma2.dicii.ispw.supportdesk.utility.facade;

import it.uniroma2.dicii.ispw.supportdesk.bean.CommentBean;
import it.uniroma2.dicii.ispw.supportdesk.controller.applicativo.CommentController;
import it.uniroma2.dicii.ispw.supportdesk.controller.applicativo.TicketController;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.TicketStatus;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.InvalidTransitionException;
import it.uniroma2.dicii.ispw.supportdesk.exception.TicketNotFoundException;
import it.uniroma2.dicii.ispw.supportdesk.exception.ValidationException;
import it.uniroma2.dicii.ispw.supportdesk.record.CommentRecord;
import it.uniroma2.dicii.ispw.supportdesk.record.TicketRecord;
import it.uniroma2.dicii.ispw.supportdesk.record.UserRecord;
import it.uniroma2.dicii.ispw.supportdesk.utility.observer.ManagerNotificationObserver;
import it.uniroma2.dicii.ispw.supportdesk.utility.observer.TechnicianNotificationObserver;
import it.uniroma2.dicii.ispw.supportdesk.utility.observer.UserNotificationObserver;

import java.util.List;

public final class ViewTicketsFacade {

    private final TicketController ticketController;
    private final CommentController commentController;

    private ViewTicketsFacade() {
        ticketController = new TicketController();
        commentController = new CommentController();
        ticketController.attach(new UserNotificationObserver());
        ticketController.attach(new ManagerNotificationObserver());
        ticketController.attach(new TechnicianNotificationObserver());
    }

    private static final class Holder {
        private static final ViewTicketsFacade INSTANCE = new ViewTicketsFacade();
    }

    public static ViewTicketsFacade getInstanceSingleton() {
        return Holder.INSTANCE;
    }

    public List<TicketRecord> getAllTickets() throws DAOException {
        return ticketController.getAllTickets();
    }

    public List<TicketRecord> getTicketsByUser(String email) throws DAOException {
        return ticketController.getTicketsByUser(email);
    }

    public TicketRecord changeStatus(int ticketId, TicketStatus newStatus)
            throws DAOException, TicketNotFoundException, InvalidTransitionException {
        return ticketController.changeStatus(ticketId, newStatus);
    }

    public void changePriority(int ticketId, String priorityName)
            throws it.uniroma2.dicii.ispw.supportdesk.exception.DAOException,
                   it.uniroma2.dicii.ispw.supportdesk.exception.TicketNotFoundException {
        ticketController.changePriority(ticketId,
                it.uniroma2.dicii.ispw.supportdesk.enumerator.Priority.valueOf(priorityName));
    }

    public void addComment(CommentBean bean)
            throws ValidationException, DAOException {
        commentController.addComment(bean.getTicketId(), bean.getAuthorEmail(), bean.getText());
    }

    public List<UserRecord> getAvailableTechnicians() throws DAOException {
        return ticketController.getAvailableTechnicians();
    }

    public TicketRecord assignTechnician(int ticketId, String techEmail)
            throws DAOException, TicketNotFoundException, InvalidTransitionException {
        return ticketController.assignTechnician(ticketId, techEmail);
    }

    public List<CommentRecord> getCommentsForTicket(int ticketId) throws DAOException {
        return commentController.getCommentsForTicket(ticketId).stream()
                .map(c -> new CommentRecord(c.getId(), c.getAuthorEmail(), c.getText(), c.getCreatedAt()))
                .toList();
    }
}
