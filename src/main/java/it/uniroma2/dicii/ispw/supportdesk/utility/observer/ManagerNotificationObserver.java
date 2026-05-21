package it.uniroma2.dicii.ispw.supportdesk.utility.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManagerNotificationObserver implements TicketObserver {

    private static final Logger log = LoggerFactory.getLogger(ManagerNotificationObserver.class);

    @Override
    public void update(EventType event) {
        switch (event) {
            case TICKET_OPEN        -> log.info("[NOTIFICA MANAGER] Nuovo ticket aperto.");
            case TICKET_CAMBIO_STATO -> log.info("[NOTIFICA MANAGER] Cambio stato ticket rilevato.");
            case SLA_VIOLATO        -> log.warn("[NOTIFICA MANAGER] SLA VIOLATO rilevato.");
            case SLA_IN_SCADENZA    -> log.warn("[NOTIFICA MANAGER] SLA in scadenza rilevato.");
            case ASSEGNAZIONE_MANUALE -> log.warn("[NOTIFICA MANAGER] Ticket richiede assegnazione manuale.");
            default -> { /* altri eventi non di competenza del manager */ }
        }
    }
}