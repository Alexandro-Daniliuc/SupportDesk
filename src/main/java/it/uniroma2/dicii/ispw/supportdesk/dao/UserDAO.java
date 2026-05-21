package it.uniroma2.dicii.ispw.supportdesk.dao;

import it.uniroma2.dicii.ispw.supportdesk.enumerator.Role;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.model.User;

import java.util.List;

public interface UserDAO {

    User findByEmail(String email) throws DAOException;

    List<User> findByRole(Role role) throws DAOException;

    void insert(User user) throws DAOException;
}
