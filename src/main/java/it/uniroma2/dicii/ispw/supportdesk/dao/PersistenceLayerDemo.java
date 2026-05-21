package it.uniroma2.dicii.ispw.supportdesk.dao;

import it.uniroma2.dicii.ispw.supportdesk.dao.demo.CommentDAODemo;
import it.uniroma2.dicii.ispw.supportdesk.dao.demo.KnowledgeBaseDAODemo;
import it.uniroma2.dicii.ispw.supportdesk.dao.demo.NotificationDAODemo;
import it.uniroma2.dicii.ispw.supportdesk.dao.demo.TicketDAODemo;
import it.uniroma2.dicii.ispw.supportdesk.dao.demo.UserDAODemo;

public class PersistenceLayerDemo extends PersistenceLayer {

    public PersistenceLayerDemo() {
        this.ticketDAO         = new TicketDAODemo();
        this.userDAO           = new UserDAODemo();
        this.commentDAO        = new CommentDAODemo();
        this.knowledgeBaseDAO  = new KnowledgeBaseDAODemo();
        this.notificationDAO   = new NotificationDAODemo();
    }
}
