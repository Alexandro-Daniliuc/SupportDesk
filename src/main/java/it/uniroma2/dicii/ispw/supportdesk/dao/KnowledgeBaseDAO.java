package it.uniroma2.dicii.ispw.supportdesk.dao;

import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.model.KnowledgeEntry;

import java.util.List;

public interface KnowledgeBaseDAO {
    void insert(KnowledgeEntry entry) throws DAOException;
    List<KnowledgeEntry> findAll() throws DAOException;
    List<KnowledgeEntry> findByKeyword(String keyword) throws DAOException;
}
