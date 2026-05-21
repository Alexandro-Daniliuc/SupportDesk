package it.uniroma2.dicii.ispw.supportdesk.dao;

import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.TicketNotFoundException;
import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;

import java.util.List;

public interface TicketDAO {

    void insert(Ticket ticket) throws DAOException;

    Ticket findById(int id) throws DAOException, TicketNotFoundException;

    List<Ticket> findAll() throws DAOException;

    List<Ticket> findByUserEmail(String email) throws DAOException;

    void update(Ticket ticket) throws DAOException, TicketNotFoundException;

    void delete(int id) throws DAOException;
}
