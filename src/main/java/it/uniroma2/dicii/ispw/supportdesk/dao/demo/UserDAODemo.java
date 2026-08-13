package it.uniroma2.dicii.ispw.supportdesk.dao.demo;

import it.uniroma2.dicii.ispw.supportdesk.dao.UserDAO;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Role;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.model.User;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UserDAODemo implements UserDAO {

    private static final String DEMO_CREDENTIAL_HASH = "DEMO_HASH_PLACEHOLDER";

    private static final Map<String, User> STORE = new LinkedHashMap<>();

    static {
        User admin = new User(1, "Admin", "Sistema", "admin@supportdesk.it", DEMO_CREDENTIAL_HASH, Role.MANAGER);
        User tech1 = new User(2, "Marco", "Bianchi", "marco.bianchi@supportdesk.it", DEMO_CREDENTIAL_HASH, Role.TECHNICIAN);
        User tech2 = new User(3, "Laura", "Verdi", "laura.verdi@supportdesk.it", DEMO_CREDENTIAL_HASH, Role.TECHNICIAN);
        tech1.setSpecialization("Email e Posta Elettronica");
        tech2.setSpecialization("Reti e Connettività");
        User user1 = new User(4, "Giovanni", "Rossi", "giovanni.rossi@azienda.it", DEMO_CREDENTIAL_HASH, Role.USER);
        User user2 = new User(5, "Sara", "Neri", "sara.neri@azienda.it", DEMO_CREDENTIAL_HASH, Role.USER);

        STORE.put(admin.getEmail(), admin);
        STORE.put(tech1.getEmail(), tech1);
        STORE.put(tech2.getEmail(), tech2);
        STORE.put(user1.getEmail(), user1);
        STORE.put(user2.getEmail(), user2);
    }

    @Override
    public User findByEmail(String email) throws DAOException {
        return STORE.get(email);
    }

    @Override
    public List<User> findByRole(Role role) throws DAOException {
        List<User> result = new ArrayList<>();
        for (User u : STORE.values()) {
            if (u.getRole() == role) {
                result.add(u);
            }
        }
        return result;
    }

    @Override
    public void insert(User user) throws DAOException {
        STORE.put(user.getEmail(), user);
    }
}
