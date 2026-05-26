package it.uniroma2.dicii.ispw.supportdesk.dao.factory;

import it.uniroma2.dicii.ispw.supportdesk.dao.CommentDAO;
import it.uniroma2.dicii.ispw.supportdesk.dao.KnowledgeBaseDAO;
import it.uniroma2.dicii.ispw.supportdesk.dao.NotificationDAO;
import it.uniroma2.dicii.ispw.supportdesk.dao.TicketDAO;
import it.uniroma2.dicii.ispw.supportdesk.dao.UserDAO;
import it.uniroma2.dicii.ispw.supportdesk.dao.file.CommentDAOFile;
import it.uniroma2.dicii.ispw.supportdesk.dao.file.KnowledgeBaseDAOFile;
import it.uniroma2.dicii.ispw.supportdesk.dao.file.NotificationDAOFile;
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
    public KnowledgeBaseDAO createKnowledgeBaseDAO() {
        return new KnowledgeBaseDAOFile();
    }

    @Override
    public NotificationDAO createNotificationDAO() {
        return new NotificationDAOFile();
    }
}