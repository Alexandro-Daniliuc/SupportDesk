package it.uniroma2.dicii.ispw.supportdesk.utility.observer;

import it.uniroma2.dicii.ispw.supportdesk.bean.NotificationBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManagerNotificationObserver implements Observer {

    private static final Logger log = LoggerFactory.getLogger(ManagerNotificationObserver.class);
    private static final String NOTIFICA_FORMAT = "[NOTIFICA MANAGER] {}";

    @Override
    public void update(EventType event, Object payload) {
        NotificationBean notification = (NotificationBean) payload;
        switch (event) {
            case TICKET_OPEN          -> log.info(NOTIFICA_FORMAT, notification.getMessage());
            case TICKET_IN_CARICO     -> log.info(NOTIFICA_FORMAT, notification.getMessage());
            case TICKET_RISOLTO       -> log.info(NOTIFICA_FORMAT, notification.getMessage());
            case TICKET_CAMBIO_STATO  -> log.info(NOTIFICA_FORMAT, notification.getMessage());
            case SLA_VIOLATO          -> log.warn(NOTIFICA_FORMAT, notification.getMessage());
            case SLA_IN_SCADENZA      -> log.warn(NOTIFICA_FORMAT, notification.getMessage());
            case ASSEGNAZIONE_MANUALE -> log.warn(NOTIFICA_FORMAT, notification.getMessage());
            default -> { /* altri eventi non di competenza del manager */ }
        }
    }
}
