package it.uniroma2.dicii.ispw.supportdesk.utility.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserNotificationObserver implements TicketObserver {

    private static final Logger log = LoggerFactory.getLogger(UserNotificationObserver.class);

    @Override
    public void update(EventType event) {
        if (event == EventType.TICKET_RISOLTO) {
            log.info("[NOTIFICA UTENTE] Ticket risolto — email inviata all'utente richiedente.");
        }
    }
}