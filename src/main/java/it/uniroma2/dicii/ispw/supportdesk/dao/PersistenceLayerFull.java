package it.uniroma2.dicii.ispw.supportdesk.dao;

import it.uniroma2.dicii.ispw.supportdesk.dao.factory.DAOAbstractFactory;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.TicketNotFoundException;
import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;
import it.uniroma2.dicii.ispw.supportdesk.utility.singleton.ApplicationModeManager;

import java.util.List;

public class PersistenceLayerFull extends PersistenceLayer {

    PersistenceLayerFull() {
        DAOAbstractFactory factory = DAOAbstractFactory.getFactory(
                ApplicationModeManager.getInstanceSingleton().getMode());
        this.ticketDAO        = factory.createTicketDAO();
        this.userDAO          = factory.createUserDAO();
        this.commentDAO       = factory.createCommentDAO();
        this.knowledgeBaseDAO = factory.createKnowledgeBaseDAO();
        this.notificationDAO  = factory.createNotificationDAO();
    }

    @Override
    public void saveTicket(Ticket ticket) throws DAOException {
        ticketDAO.insert(ticket);
    }

    @Override
    public Ticket getTicketById(int id) throws DAOException, TicketNotFoundException {
        return ticketDAO.findById(id);
    }

    @Override
    public List<Ticket> getTicketsByUser(String email) throws DAOException {
        return ticketDAO.findByUserEmail(email);
    }
}
