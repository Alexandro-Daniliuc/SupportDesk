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
package it.uniroma2.dicii.ispw.supportdesk.utility.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserNotificationObserver implements TicketObserver {

    private static final Logger log = LoggerFactory.getLogger(UserNotificationObserver.class);

    @Override
    public void update(EventType event) {
        if (event == EventType.TICKET_RISOLTO) {
            log.info("[NOTIFICA UTENTE] Ticket risolto — email inviata all'utente richiedente.");
        }
    }
}