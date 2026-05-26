package it.uniroma2.dicii.ispw.supportdesk.dao.db;

import it.uniroma2.dicii.ispw.supportdesk.dao.ConnectionManager;
import it.uniroma2.dicii.ispw.supportdesk.dao.NotificationDAO;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Role;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.model.Notification;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAODB implements NotificationDAO {

    private static final String SQL_INSERT =
        "INSERT INTO notifications (id, message, target_role, ticket_id, created_at, is_read) VALUES (?,?,?,?,?,?)";
    private static final String SQL_FIND_ALL =
        "SELECT id, message, target_role, ticket_id, created_at, is_read FROM notifications";
    private static final String SQL_FIND_BY_ROLE =
        "SELECT id, message, target_role, ticket_id, created_at, is_read FROM notifications WHERE target_role = ?";
    private static final String SQL_MARK_AS_READ =
        "UPDATE notifications SET is_read = TRUE WHERE id = ?";

    @Override
    public void insert(Notification notification) throws DAOException {
        try (Connection conn = ConnectionManager.getInstanceSingleton().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
            ps.setInt(1, notification.getId());
            ps.setString(2, notification.getMessage());
            ps.setString(3, notification.getTargetRole().name());
            ps.setInt(4, notification.getTicketId());
            ps.setTimestamp(5, Timestamp.valueOf(notification.getCreatedAt()));
            ps.setBoolean(6, notification.isRead());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Errore insert notifica", e);
        }
    }

    @Override
    public List<Notification> findAll() throws DAOException {
        List<Notification> list = new ArrayList<>();
        try (Connection conn = ConnectionManager.getInstanceSingleton().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new DAOException("Errore findAll notifiche", e);
        }
        return list;
    }

    @Override
    public List<Notification> findByRole(Role role) throws DAOException {
        List<Notification> list = new ArrayList<>();
        try (Connection conn = ConnectionManager.getInstanceSingleton().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ROLE)) {
            ps.setString(1, role.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Errore findByRole notifiche", e);
        }
        return list;
    }

    @Override
    public void markAsRead(int notificationId) throws DAOException {
        try (Connection conn = ConnectionManager.getInstanceSingleton().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_MARK_AS_READ)) {
            ps.setInt(1, notificationId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Errore markAsRead notifica id=" + notificationId, e);
        }
    }

    private Notification mapRow(ResultSet rs) throws SQLException {
        int id              = rs.getInt("id");
        String message      = rs.getString("message");
        Role targetRole     = Role.valueOf(rs.getString("target_role"));
        int ticketId        = rs.getInt("ticket_id");
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        boolean read        = rs.getBoolean("is_read");
        return new Notification(id, message, targetRole, ticketId, createdAt, read);
    }
}
