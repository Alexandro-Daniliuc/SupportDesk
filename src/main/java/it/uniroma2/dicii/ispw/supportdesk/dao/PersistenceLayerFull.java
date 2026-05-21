package it.uniroma2.dicii.ispw.supportdesk.dao;

import it.uniroma2.dicii.ispw.supportdesk.dao.factory.DAOAbstractFactory;
import it.uniroma2.dicii.ispw.supportdesk.utility.singleton.ApplicationModeManager;

public class PersistenceLayerFull extends PersistenceLayer {

    PersistenceLayerFull() {
        DAOAbstractFactory factory = DAOAbstractFactory.getFactory(
            ApplicationModeManager.getInstanceSingleton().getMode()
        );
        this.ticketDAO  = factory.createTicketDAO();
        this.userDAO    = factory.createUserDAO();
        this.commentDAO = factory.createCommentDAO();
    }
}
