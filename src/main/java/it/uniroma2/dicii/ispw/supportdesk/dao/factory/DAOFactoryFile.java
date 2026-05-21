package it.uniroma2.dicii.ispw.supportdesk.dao.factory;

import it.uniroma2.dicii.ispw.supportdesk.dao.CommentDAO;
import it.uniroma2.dicii.ispw.supportdesk.dao.TicketDAO;
import it.uniroma2.dicii.ispw.supportdesk.dao.UserDAO;
import it.uniroma2.dicii.ispw.supportdesk.dao.file.CommentDAOFile;
import it.uniroma2.dicii.ispw.supportdesk.dao.file.TicketDAOFile;
import it.uniroma2.dicii.ispw.supportdesk.dao.file.UserDAOFile;

public class DAOFactoryFile extends DAOAbstractFactory {

    @Override
    public TicketDAO createTicketDAO() {
        return new TicketDAOFile();
    }

    @Override
    public UserDAO createUserDAO() {
        return new UserDAOFile();
    }

    @Override
    public CommentDAO createCommentDAO() {
        return new CommentDAOFile();
    }

    @Override
    public it.uniroma2.dicii.ispw.supportdesk.dao.KnowledgeBaseDAO createKnowledgeBaseDAO() {
        throw new UnsupportedOperationException("KnowledgeBaseDAO non implementato per questa modalità");
    }

    @Override
    public it.uniroma2.dicii.ispw.supportdesk.dao.NotificationDAO createNotificationDAO() {
        throw new UnsupportedOperationException("NotificationDAO non implementato per questa modalità");
    }
}