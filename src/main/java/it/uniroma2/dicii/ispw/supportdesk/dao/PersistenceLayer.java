package it.uniroma2.dicii.ispw.supportdesk.dao;

import it.uniroma2.dicii.ispw.supportdesk.enumerator.Role;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.TicketNotFoundException;
import it.uniroma2.dicii.ispw.supportdesk.model.Comment;
import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;
import it.uniroma2.dicii.ispw.supportdesk.model.User;

import java.util.List;

public abstract class PersistenceLayer {

    protected TicketDAO        ticketDAO;
    protected UserDAO          userDAO;
    protected CommentDAO       commentDAO;
    protected KnowledgeBaseDAO knowledgeBaseDAO;
    protected NotificationDAO  notificationDAO;

    // -- Abstract factory methods (PNG 10) ------------------------------------

    public abstract void saveTicket(Ticket ticket) throws DAOException;

    public abstract Ticket getTicketById(int id) throws DAOException, TicketNotFoundException;

    public abstract List<Ticket> getTicketsByUser(String email) throws DAOException;

    public abstract User login(String email, String hash) throws DAOException;

    // -- Ticket ---------------------------------------------------------------

    public List<Ticket> findAllTickets() throws DAOException {
        return ticketDAO.findAll();
    }

    public void updateTicket(Ticket ticket) throws DAOException, TicketNotFoundException {
        ticketDAO.update(ticket);
    }

    public void deleteTicket(int id) throws DAOException {
        ticketDAO.delete(id);
    }

    // -- User -----------------------------------------------------------------

    public User findUserByEmail(String email) throws DAOException {
        return userDAO.findByEmail(email);
    }

    public List<User> findUsersByRole(Role role) throws DAOException {
        return userDAO.findByRole(role);
    }

    public void insertUser(User user) throws DAOException {
        userDAO.insert(user);
    }

    // -- Comment --------------------------------------------------------------

    public void saveComment(Comment comment) throws DAOException {
        commentDAO.insert(comment);
    }

    public List<Comment> findCommentsByTicketId(int ticketId) throws DAOException {
        return commentDAO.findByTicketId(ticketId);
    }

    public List<Comment> findAllComments() throws DAOException {
        return commentDAO.findAll();
    }

    // -- KnowledgeBase --------------------------------------------------------

    public void saveKnowledgeEntry(it.uniroma2.dicii.ispw.supportdesk.model.KnowledgeEntry entry) throws DAOException {
        knowledgeBaseDAO.insert(entry);
    }

    public List<it.uniroma2.dicii.ispw.supportdesk.model.KnowledgeEntry> findAllKnowledgeEntries() throws DAOException {
        return knowledgeBaseDAO.findAll();
    }

    public List<it.uniroma2.dicii.ispw.supportdesk.model.KnowledgeEntry> findKnowledgeEntriesByKeyword(String keyword) throws DAOException {
        return knowledgeBaseDAO.findByKeyword(keyword);
    }

    // -- Notification ---------------------------------------------------------

    public void saveNotification(it.uniroma2.dicii.ispw.supportdesk.model.Notification notification) throws DAOException {
        notificationDAO.insert(notification);
    }

    public List<it.uniroma2.dicii.ispw.supportdesk.model.Notification> findNotificationsByRole(it.uniroma2.dicii.ispw.supportdesk.enumerator.Role role) throws DAOException {
        return notificationDAO.findByRole(role);
    }

    public void markNotificationAsRead(int notificationId) throws DAOException {
        notificationDAO.markAsRead(notificationId);
    }
}
