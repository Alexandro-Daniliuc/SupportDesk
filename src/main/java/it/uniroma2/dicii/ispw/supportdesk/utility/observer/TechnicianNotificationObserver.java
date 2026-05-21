package it.uniroma2.dicii.ispw.supportdesk.utility.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TechnicianNotificationObserver implements TicketObserver {

    private static final Logger log = LoggerFactory.getLogger(TechnicianNotificationObserver.class);

    @Override
    public void update(EventType event) {
        if (event == EventType.TICKET_OPEN) {
            log.info("[NOTIFICA TECNICI] Nuovo ticket aperto — in attesa di assegnazione.");
        }
        if (event == EventType.TICKET_CAMBIO_STATO) {
            log.info("[NOTIFICA TECNICO] Cambio stato ticket rilevato.");
        }
    }
}