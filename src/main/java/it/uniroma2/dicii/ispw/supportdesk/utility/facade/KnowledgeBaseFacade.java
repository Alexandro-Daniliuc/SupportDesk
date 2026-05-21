package it.uniroma2.dicii.ispw.supportdesk.utility.facade;

import it.uniroma2.dicii.ispw.supportdesk.controller.applicativo.KnowledgeBaseController;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.KnowledgeBaseException;
import it.uniroma2.dicii.ispw.supportdesk.record.KnowledgeEntryRecord;

import java.util.List;

@SuppressWarnings("java:S6548")
public final class KnowledgeBaseFacade {

    private final KnowledgeBaseController knowledgeBaseController;

    private KnowledgeBaseFacade() {
        knowledgeBaseController = new KnowledgeBaseController();
    }

    private static final class Holder {
        private static final KnowledgeBaseFacade INSTANCE = new KnowledgeBaseFacade();
    }

    public static KnowledgeBaseFacade getInstanceSingleton() {
        return Holder.INSTANCE;
    }

    public List<KnowledgeEntryRecord> searchEntries(String keyword)
            throws KnowledgeBaseException, DAOException {
        return knowledgeBaseController.searchEntries(keyword);
    }

    public List<KnowledgeEntryRecord> getAllEntries() throws DAOException {
        return knowledgeBaseController.getAllEntries();
    }

    public KnowledgeEntryRecord addEntry(String title, String content, String authorEmail)
            throws KnowledgeBaseException, DAOException {
        return knowledgeBaseController.addEntry(title, content, authorEmail);
    }
}
