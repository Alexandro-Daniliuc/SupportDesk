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
package it.uniroma2.dicii.ispw.supportdesk.utility.facade;

import it.uniroma2.dicii.ispw.supportdesk.controller.applicativo.CorrelationController;
import it.uniroma2.dicii.ispw.supportdesk.controller.applicativo.TicketController;
import it.uniroma2.dicii.ispw.supportdesk.dao.PersistenceLayer;
import it.uniroma2.dicii.ispw.supportdesk.exception.CorrelationEngineException;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.TicketNotFoundException;
import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;
import it.uniroma2.dicii.ispw.supportdesk.record.TicketRecord;

import java.util.List;

@SuppressWarnings("java:S6548")
public final class CorrelationFacade {

    private CorrelationFacade() {}

    private static final class Holder {
        private static final CorrelationFacade INSTANCE = new CorrelationFacade();
    }

    public static CorrelationFacade getInstanceSingleton() {
        return Holder.INSTANCE;
    }

    public List<TicketRecord> findCorrelations(int ticketId)
            throws DAOException, TicketNotFoundException, CorrelationEngineException {
        Ticket target = PersistenceLayer.getInstance().getTicketById(ticketId);
        return new CorrelationController().findCorrelations(target);
    }
}
