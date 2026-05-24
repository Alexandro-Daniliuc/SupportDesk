package it.uniroma2.dicii.ispw.supportdesk.utility.observer;

import it.uniroma2.dicii.ispw.supportdesk.bean.NotificationBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserNotificationObserver implements Observer {

    private static final Logger log = LoggerFactory.getLogger(UserNotificationObserver.class);

    @Override
    public void update(EventType event, Object payload) {
        NotificationBean notification = (NotificationBean) payload;
        switch (event) {
            case TICKET_IN_CARICO     -> log.info("[NOTIFICA UTENTE] {}", notification.getMessage());
            case TICKET_RISOLTO       -> log.info("[NOTIFICA UTENTE] {}", notification.getMessage());
            case ASSEGNAZIONE_MANUALE -> log.info("[NOTIFICA UTENTE] {}", notification.getMessage());
            default -> { /* eventi non di competenza dell'utente */ }
        }
    }
}
