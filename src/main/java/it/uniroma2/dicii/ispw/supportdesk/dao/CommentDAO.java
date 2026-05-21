package it.uniroma2.dicii.ispw.supportdesk.dao;

import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.model.Comment;

import java.util.List;

public interface CommentDAO {

    void insert(Comment comment) throws DAOException;

    List<Comment> findByTicketId(int ticketId) throws DAOException;

    List<Comment> findAll() throws DAOException;
}
