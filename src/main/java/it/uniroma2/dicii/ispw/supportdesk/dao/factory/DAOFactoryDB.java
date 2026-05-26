package it.uniroma2.dicii.ispw.supportdesk.dao.factory;

import it.uniroma2.dicii.ispw.supportdesk.dao.CommentDAO;
import it.uniroma2.dicii.ispw.supportdesk.dao.KnowledgeBaseDAO;
import it.uniroma2.dicii.ispw.supportdesk.dao.NotificationDAO;
import it.uniroma2.dicii.ispw.supportdesk.dao.TicketDAO;
import it.uniroma2.dicii.ispw.supportdesk.dao.UserDAO;
import it.uniroma2.dicii.ispw.supportdesk.dao.db.CommentDAODB;
import it.uniroma2.dicii.ispw.supportdesk.dao.db.KnowledgeBaseDAODB;
import it.uniroma2.dicii.ispw.supportdesk.dao.db.NotificationDAODB;
import it.uniroma2.dicii.ispw.supportdesk.dao.db.TicketDAODB;
import it.uniroma2.dicii.ispw.supportdesk.dao.db.UserDAODB;

public class DAOFactoryDB extends DAOAbstractFactory {

    @Override
    public TicketDAO createTicketDAO() {
        return new TicketDAODB();
    }

    @Override
    public UserDAO createUserDAO() {
        return new UserDAODB();
    }

    @Override
    public CommentDAO createCommentDAO() {
        return new CommentDAODB();
    }

    @Override
    public KnowledgeBaseDAO createKnowledgeBaseDAO() {
        return new KnowledgeBaseDAODB();
    }

    @Override
    public NotificationDAO createNotificationDAO() {
        return new NotificationDAODB();
    }
}