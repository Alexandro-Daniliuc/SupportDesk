package it.uniroma2.dicii.ispw.supportdesk.utility.observer;

import it.uniroma2.dicii.ispw.supportdesk.bean.NotificationBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TechnicianNotificationObserver implements TicketObserver {

    private static final Logger log = LoggerFactory.getLogger(TechnicianNotificationObserver.class);

    @Override
    public void update(NotificationBean notification) {
        switch (notification.getEventType()) {
            case TICKET_OPEN         -> log.info("[NOTIFICA TECNICI] {}", notification.getMessage());
            case TICKET_CAMBIO_STATO -> log.info("[NOTIFICA TECNICO] {}", notification.getMessage());
            case ASSEGNAZIONE_MANUALE -> log.info("[NOTIFICA TECNICO] {}", notification.getMessage());
            default -> { /* eventi non di competenza del tecnico */ }
        }
    }
}