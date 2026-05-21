package it.uniroma2.dicii.ispw.supportdesk.dao.factory;

import it.uniroma2.dicii.ispw.supportdesk.dao.CommentDAO;
import it.uniroma2.dicii.ispw.supportdesk.dao.KnowledgeBaseDAO;
import it.uniroma2.dicii.ispw.supportdesk.dao.NotificationDAO;
import it.uniroma2.dicii.ispw.supportdesk.dao.TicketDAO;
import it.uniroma2.dicii.ispw.supportdesk.dao.UserDAO;
import it.uniroma2.dicii.ispw.supportdesk.dao.demo.CommentDAODemo;
import it.uniroma2.dicii.ispw.supportdesk.dao.demo.KnowledgeBaseDAODemo;
import it.uniroma2.dicii.ispw.supportdesk.dao.demo.NotificationDAODemo;
import it.uniroma2.dicii.ispw.supportdesk.dao.demo.TicketDAODemo;
import it.uniroma2.dicii.ispw.supportdesk.dao.demo.UserDAODemo;

public class DAOFactoryDemo extends DAOAbstractFactory {

    @Override
    public TicketDAO createTicketDAO() {
        return new TicketDAODemo();
    }

    @Override
    public UserDAO createUserDAO() {
        return new UserDAODemo();
    }

    @Override
    public CommentDAO createCommentDAO() {
        return new CommentDAODemo();
    }

    @Override
    public KnowledgeBaseDAO createKnowledgeBaseDAO() {
        return new KnowledgeBaseDAODemo();
    }

    @Override
    public NotificationDAO createNotificationDAO() {
        return new NotificationDAODemo();
    }
}
