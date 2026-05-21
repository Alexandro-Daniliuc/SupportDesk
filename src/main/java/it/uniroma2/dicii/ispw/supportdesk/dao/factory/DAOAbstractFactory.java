package it.uniroma2.dicii.ispw.supportdesk.dao.factory;

import it.uniroma2.dicii.ispw.supportdesk.dao.CommentDAO;
import it.uniroma2.dicii.ispw.supportdesk.dao.KnowledgeBaseDAO;
import it.uniroma2.dicii.ispw.supportdesk.dao.NotificationDAO;
import it.uniroma2.dicii.ispw.supportdesk.dao.TicketDAO;
import it.uniroma2.dicii.ispw.supportdesk.dao.UserDAO;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.ApplicationMode;

public abstract class DAOAbstractFactory {

    public static DAOAbstractFactory getFactory(ApplicationMode mode) {
        return switch (mode) {
            case DEMO      -> new DAOFactoryDemo();
            case FULL_DB   -> new DAOFactoryDB();
            case FULL_FILE -> new DAOFactoryFile();
        };
    }

    public abstract TicketDAO createTicketDAO();

    public abstract UserDAO createUserDAO();

    public abstract CommentDAO createCommentDAO();

    public abstract KnowledgeBaseDAO createKnowledgeBaseDAO();

    public abstract NotificationDAO createNotificationDAO();
}
