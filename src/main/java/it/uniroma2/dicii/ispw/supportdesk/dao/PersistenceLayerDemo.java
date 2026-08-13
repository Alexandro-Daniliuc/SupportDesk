package it.uniroma2.dicii.ispw.supportdesk.dao;

import it.uniroma2.dicii.ispw.supportdesk.dao.demo.CommentDAODemo;
import it.uniroma2.dicii.ispw.supportdesk.dao.demo.KnowledgeBaseDAODemo;
import it.uniroma2.dicii.ispw.supportdesk.dao.demo.NotificationDAODemo;
import it.uniroma2.dicii.ispw.supportdesk.dao.demo.TicketDAODemo;
import it.uniroma2.dicii.ispw.supportdesk.dao.demo.UserDAODemo;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.TicketNotFoundException;
import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;

import java.util.List;

public class PersistenceLayerDemo extends PersistenceLayer {

    public PersistenceLayerDemo() {
        this.ticketDAO        = new TicketDAODemo();
        this.userDAO          = new UserDAODemo();
        this.commentDAO       = new CommentDAODemo();
        this.knowledgeBaseDAO = new KnowledgeBaseDAODemo();
        this.notificationDAO  = new NotificationDAODemo();
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
