/*
 * SupportDesk — ISPW Project
 * Copyright (C) 2026  Alexandro Daniliuc
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
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
