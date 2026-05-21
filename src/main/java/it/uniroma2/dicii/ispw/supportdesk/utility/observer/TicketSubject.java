package it.uniroma2.dicii.ispw.supportdesk.utility.observer;

public interface TicketSubject {
    void attach(TicketObserver observer);
    void detach(TicketObserver observer);
    void notifyObservers(EventType eventType);
}
