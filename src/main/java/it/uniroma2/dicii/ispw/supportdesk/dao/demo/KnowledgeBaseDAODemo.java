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

import it.uniroma2.dicii.ispw.supportdesk.dao.KnowledgeBaseDAO;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.model.KnowledgeEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
public class KnowledgeBaseDAODemo implements KnowledgeBaseDAO {

    private static final List<KnowledgeEntry> STORE = new CopyOnWriteArrayList<>();

    @Override
    public void insert(KnowledgeEntry entry) throws DAOException {
        STORE.add(entry);
    }

    @Override
    public List<KnowledgeEntry> findAll() throws DAOException {
        return new ArrayList<>(STORE);
    }

    @Override
    public List<KnowledgeEntry> findByKeyword(String keyword) throws DAOException {
        String kw = keyword.toLowerCase(Locale.ITALIAN);
        return STORE.stream()
                .filter(e -> e.getTitle().toLowerCase(Locale.ITALIAN).contains(kw)
                          || e.getContent().toLowerCase(Locale.ITALIAN).contains(kw))
                .toList();
    }
}
