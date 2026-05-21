package it.uniroma2.dicii.ispw.supportdesk.dao;

import it.uniroma2.dicii.ispw.supportdesk.enumerator.Role;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.model.Notification;

import java.util.List;

public interface NotificationDAO {
    void insert(Notification notification) throws DAOException;
    List<Notification> findAll() throws DAOException;
    List<Notification> findByRole(Role role) throws DAOException;
    void markAsRead(int notificationId) throws DAOException;
}
