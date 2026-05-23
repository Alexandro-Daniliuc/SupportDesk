package it.uniroma2.dicii.ispw.supportdesk.bean;

import it.uniroma2.dicii.ispw.supportdesk.utility.observer.EventType;

public class NotificationBean {

    private final EventType eventType;
    private final String message;

    public NotificationBean(EventType eventType, String message) {
        this.eventType = eventType;
        this.message = message;
    }

    public EventType getEventType() {
        return eventType;
    }

    public String getMessage() {
        return message;
    }
}
