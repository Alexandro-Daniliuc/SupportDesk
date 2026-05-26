package it.uniroma2.dicii.ispw.supportdesk.dao.file;

import it.uniroma2.dicii.ispw.supportdesk.dao.KnowledgeBaseDAO;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.model.KnowledgeEntry;
import it.uniroma2.dicii.ispw.supportdesk.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class KnowledgeBaseDAOFile implements KnowledgeBaseDAO {

    private static final String SEP = "|";
    // format: id|authorEmail|createdAt|title|content  (content last — may contain pipes)
    private static final int FIELDS = 5;
    private static final String DATA_FILE = FilePathResolver.resolve("knowledge_base.csv");

    @Override
    public void insert(KnowledgeEntry entry) throws DAOException {
        CsvFileStore.appendLine(DATA_FILE, buildLine(entry));
    }

    @Override
    public List<KnowledgeEntry> findAll() throws DAOException {
        List<KnowledgeEntry> list = new ArrayList<>();
        for (String line : CsvFileStore.readLines(DATA_FILE)) {
            list.add(parseLine(line));
        }
        return list;
    }

    @Override
    public List<KnowledgeEntry> findByKeyword(String keyword) throws DAOException {
        String kw = keyword.toLowerCase(Locale.ITALIAN);
        List<KnowledgeEntry> result = new ArrayList<>();
        for (KnowledgeEntry e : findAll()) {
            if (e.getTitle().toLowerCase(Locale.ITALIAN).contains(kw)
                    || e.getContent().toLowerCase(Locale.ITALIAN).contains(kw)) {
                result.add(e);
            }
        }
        return result;
    }

    private String buildLine(KnowledgeEntry e) {
        return e.getId() + SEP + e.getAuthor().getEmail() + SEP
            + e.getCreatedAt() + SEP + e.getTitle() + SEP + e.getContent();
    }

    private KnowledgeEntry parseLine(String line) throws DAOException {
        String[] p = line.split("\\|", FIELDS);
        try {
            int id              = Integer.parseInt(p[0]);
            String authorEmail  = p[1];
            LocalDateTime createdAt = LocalDateTime.parse(p[2]);
            String title        = p[3];
            String content      = p[4];
            User author = new UserDAOFile().findByEmail(authorEmail);
            return new KnowledgeEntry(id, title, content, author, createdAt);
        } catch (Exception e) {
            throw new DAOException("Errore parsing riga knowledge base: " + line, e);
        }
    }
}
