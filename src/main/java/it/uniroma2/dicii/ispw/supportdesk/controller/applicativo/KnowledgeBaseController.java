package it.uniroma2.dicii.ispw.supportdesk.controller.applicativo;

import it.uniroma2.dicii.ispw.supportdesk.dao.PersistenceLayer;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.KnowledgeBaseException;
import it.uniroma2.dicii.ispw.supportdesk.model.KnowledgeEntry;
import it.uniroma2.dicii.ispw.supportdesk.model.User;
import it.uniroma2.dicii.ispw.supportdesk.record.KnowledgeEntryRecord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class KnowledgeBaseController {

    private static final Logger log = LoggerFactory.getLogger(KnowledgeBaseController.class);
    private static final AtomicInteger idGen = new AtomicInteger(1);

    public KnowledgeEntryRecord addEntry(String title, String content, String authorEmail)
            throws KnowledgeBaseException, DAOException {
        if (title == null || title.isBlank() || content == null || content.isBlank()) {
            throw new KnowledgeBaseException("Titolo e contenuto sono obbligatori");
        }
        User author = PersistenceLayer.getInstanceSingleton().findUserByEmail(authorEmail);
        if (author == null) {
            throw new KnowledgeBaseException("Autore non trovato: " + authorEmail);
        }
        KnowledgeEntry entry = new KnowledgeEntry(idGen.getAndIncrement(), title, content, author);
        PersistenceLayer.getInstanceSingleton().saveKnowledgeEntry(entry);
        log.info("Voce knowledge base aggiunta: {}", title);
        return toRecord(entry);
    }

    public List<KnowledgeEntryRecord> searchEntries(String keyword)
            throws KnowledgeBaseException, DAOException {
        if (keyword == null || keyword.isBlank()) {
            throw new KnowledgeBaseException("Keyword di ricerca non valida");
        }
        return PersistenceLayer.getInstanceSingleton().findKnowledgeEntriesByKeyword(keyword)
                .stream().map(this::toRecord).toList();
    }

    public List<KnowledgeEntryRecord> getAllEntries() throws DAOException {
        return PersistenceLayer.getInstanceSingleton().findAllKnowledgeEntries()
                .stream().map(this::toRecord).toList();
    }

    private KnowledgeEntryRecord toRecord(KnowledgeEntry e) {
        String authorName = e.getAuthor().getName() + " " + e.getAuthor().getSurname();
        return new KnowledgeEntryRecord(e.getId(), e.getTitle(), e.getContent(), authorName, e.getCreatedAt());
    }
}