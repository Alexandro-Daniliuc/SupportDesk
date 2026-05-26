package it.uniroma2.dicii.ispw.supportdesk.dao.db;

import it.uniroma2.dicii.ispw.supportdesk.dao.ConnectionManager;
import it.uniroma2.dicii.ispw.supportdesk.dao.KnowledgeBaseDAO;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Role;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.model.KnowledgeEntry;
import it.uniroma2.dicii.ispw.supportdesk.model.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class KnowledgeBaseDAODB implements KnowledgeBaseDAO {

    private static final String SQL_INSERT =
        "INSERT INTO knowledge_base (id, title, content, author_email, created_at) VALUES (?,?,?,?,?)";

    private static final String COLS =
        "kb.id, kb.title, kb.content, kb.created_at, " +
        "u.id AS u_id, u.name AS u_name, u.surname AS u_surname, u.email AS u_email, " +
        "u.credential_hash AS u_hash, u.role AS u_role, u.specialization AS u_spec ";

    private static final String SQL_FIND_ALL =
        "SELECT " + COLS + "FROM knowledge_base kb JOIN users u ON kb.author_email = u.email";

    private static final String SQL_FIND_BY_KEYWORD =
        "SELECT " + COLS + "FROM knowledge_base kb JOIN users u ON kb.author_email = u.email " +
        "WHERE LOWER(kb.title) LIKE ? OR LOWER(kb.content) LIKE ?";

    @Override
    public void insert(KnowledgeEntry entry) throws DAOException {
        try (Connection conn = ConnectionManager.getInstanceSingleton().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
            ps.setInt(1, entry.getId());
            ps.setString(2, entry.getTitle());
            ps.setString(3, entry.getContent());
            ps.setString(4, entry.getAuthor().getEmail());
            ps.setTimestamp(5, Timestamp.valueOf(entry.getCreatedAt()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Errore insert knowledge entry", e);
        }
    }

    @Override
    public List<KnowledgeEntry> findAll() throws DAOException {
        List<KnowledgeEntry> list = new ArrayList<>();
        try (Connection conn = ConnectionManager.getInstanceSingleton().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new DAOException("Errore findAll knowledge base", e);
        }
        return list;
    }

    @Override
    public List<KnowledgeEntry> findByKeyword(String keyword) throws DAOException {
        List<KnowledgeEntry> list = new ArrayList<>();
        String pattern = "%" + keyword.toLowerCase() + "%";
        try (Connection conn = ConnectionManager.getInstanceSingleton().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_KEYWORD)) {
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Errore findByKeyword knowledge base", e);
        }
        return list;
    }

    private KnowledgeEntry mapRow(ResultSet rs) throws SQLException {
        int id              = rs.getInt("id");
        String title        = rs.getString("title");
        String content      = rs.getString("content");
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        User author = new User(
            rs.getInt("u_id"),
            rs.getString("u_name"),
            rs.getString("u_surname"),
            rs.getString("u_email"),
            rs.getString("u_hash"),
            Role.valueOf(rs.getString("u_role"))
        );
        String spec = rs.getString("u_spec");
        if (spec != null) author.setSpecialization(spec);
        return new KnowledgeEntry(id, title, content, author, createdAt);
    }
}
