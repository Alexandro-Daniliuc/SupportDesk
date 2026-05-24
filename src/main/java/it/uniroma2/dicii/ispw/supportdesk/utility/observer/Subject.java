package it.uniroma2.dicii.ispw.supportdesk.utility.observer;

import java.util.ArrayList;
import java.util.List;

public abstract class Subject {

    private final List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer o) {
        observers.add(o);
    }

    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    public void notifyObservers(EventType event, Object payload) {
        for (Observer o : observers) {
            o.update(event, payload);
        }
    }
}
