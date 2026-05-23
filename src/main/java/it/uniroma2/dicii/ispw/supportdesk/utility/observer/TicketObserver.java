package it.uniroma2.dicii.ispw.supportdesk.utility.observer;

import it.uniroma2.dicii.ispw.supportdesk.bean.NotificationBean;

public interface TicketObserver {
    void update(NotificationBean notification);
}
