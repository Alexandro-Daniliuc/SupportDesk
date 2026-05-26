package it.uniroma2.dicii.ispw.supportdesk.dao.file;

import it.uniroma2.dicii.ispw.supportdesk.dao.NotificationDAO;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Role;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.model.Notification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAOFile implements NotificationDAO {

    private static final String SEP = "|";
    // format: id|targetRole|ticketId|createdAt|isRead|message  (message last — may contain pipes)
    private static final int FIELDS = 6;
    private static final String DATA_FILE = FilePathResolver.resolve("notifications.csv");

    @Override
    public void insert(Notification notification) throws DAOException {
        CsvFileStore.appendLine(DATA_FILE, buildLine(notification));
    }

    @Override
    public List<Notification> findAll() throws DAOException {
        List<Notification> list = new ArrayList<>();
        for (String line : CsvFileStore.readLines(DATA_FILE)) {
            list.add(parseLine(line));
        }
        return list;
    }

    @Override
    public List<Notification> findByRole(Role role) throws DAOException {
        List<Notification> result = new ArrayList<>();
        for (Notification n : findAll()) {
            if (n.getTargetRole() == role) result.add(n);
        }
        return result;
    }

    @Override
    public void markAsRead(int notificationId) throws DAOException {
        List<String> lines = CsvFileStore.readLines(DATA_FILE);
        List<String> updated = new ArrayList<>();
        for (String line : lines) {
            String[] p = line.split("\\|", FIELDS);
            if (p.length >= 1 && Integer.parseInt(p[0]) == notificationId) {
                p[4] = "true";
                updated.add(String.join(SEP, p));
            } else {
                updated.add(line);
            }
        }
        CsvFileStore.writeLines(DATA_FILE, updated);
    }

    private String buildLine(Notification n) {
        return n.getId() + SEP + n.getTargetRole().name() + SEP + n.getTicketId()
            + SEP + n.getCreatedAt() + SEP + n.isRead() + SEP + n.getMessage();
    }

    private Notification parseLine(String line) throws DAOException {
        String[] p = line.split("\\|", FIELDS);
        try {
            int id              = Integer.parseInt(p[0]);
            Role targetRole     = Role.valueOf(p[1]);
            int ticketId        = Integer.parseInt(p[2]);
            LocalDateTime createdAt = LocalDateTime.parse(p[3]);
            boolean read        = Boolean.parseBoolean(p[4]);
            String message      = p[5];
            return new Notification(id, message, targetRole, ticketId, createdAt, read);
        } catch (Exception e) {
            throw new DAOException("Errore parsing riga notifica: " + line, e);
        }
    }
}
