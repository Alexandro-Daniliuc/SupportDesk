package it.uniroma2.dicii.ispw.supportdesk.dao.demo;

import it.uniroma2.dicii.ispw.supportdesk.dao.CommentDAO;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.model.Comment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CommentDAODemo implements CommentDAO {

    private static final Map<Integer, Comment> STORE = new LinkedHashMap<>();

    @Override
    public void insert(Comment comment) throws DAOException {
        STORE.put(comment.getId(), comment);
    }

    @Override
    public List<Comment> findByTicketId(int ticketId) {
        List<Comment> result = new ArrayList<>();
        for (Comment c : STORE.values()) {
            if (c.getTicketId() == ticketId) result.add(c);
        }
        return result;
    }

    @Override
    public List<Comment> findAll() {
        return new ArrayList<>(STORE.values());
    }
}
