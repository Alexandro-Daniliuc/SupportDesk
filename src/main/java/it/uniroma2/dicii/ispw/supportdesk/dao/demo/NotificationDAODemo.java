package it.uniroma2.dicii.ispw.supportdesk.dao.demo;

import it.uniroma2.dicii.ispw.supportdesk.dao.NotificationDAO;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Role;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.model.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class NotificationDAODemo implements NotificationDAO {

    private static final List<Notification> STORE = new CopyOnWriteArrayList<>();

    @Override
    public void insert(Notification notification) throws DAOException {
        STORE.add(notification);
    }

    @Override
    public List<Notification> findAll() throws DAOException {
        return new ArrayList<>(STORE);
    }

    @Override
    public List<Notification> findByRole(Role role) throws DAOException {
        return STORE.stream()
                .filter(n -> n.getTargetRole() == role)
                .toList();
    }

    @Override
    public void markAsRead(int notificationId) throws DAOException {
        STORE.stream()
                .filter(n -> n.getId() == notificationId)
                .findFirst()
                .ifPresent(Notification::markAsRead);
    }
}
