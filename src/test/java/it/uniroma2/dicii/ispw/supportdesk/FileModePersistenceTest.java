package it.uniroma2.dicii.ispw.supportdesk;

import it.uniroma2.dicii.ispw.supportdesk.dao.file.UserDAOFile;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Role;
import it.uniroma2.dicii.ispw.supportdesk.model.User;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FULL_FILE Persistence Integration Tests")
class FileModePersistenceTest {

    private UserDAOFile userDAO;

    @BeforeEach
    void setUp() {
        userDAO = new UserDAOFile();
    }

    @Test
    @DisplayName("findByEmail: admin esistente → User con ruolo MANAGER")
    void findByEmail_adminEsistente_ritornaUserManager() throws Exception {
        User user = userDAO.findByEmail("admin@supportdesk.it");
        assertNotNull(user);
        assertEquals("Admin",        user.getName());
        assertEquals(Role.MANAGER,   user.getRole());
    }

    @Test
    @DisplayName("findByEmail: user esistente → nome e cognome coerenti col CSV")
    void findByEmail_userEsistente_ritornaUserCorretto() throws Exception {
        User user = userDAO.findByEmail("giovanni@azienda.it");
        assertNotNull(user);
        assertEquals("Giovanni", user.getName());
        assertEquals("Rossi",    user.getSurname());
        assertEquals(Role.USER,  user.getRole());
    }

    @Test
    @DisplayName("findByEmail: email non presente nel CSV → null")
    void findByEmail_emailInesistente_ritornaNull() throws Exception {
        User user = userDAO.findByEmail("nonexistent@example.com");
        assertNull(user);
    }

    @Test
    @DisplayName("findByRole: TECHNICIAN → esattamente 2 tecnici nel CSV")
    void findByRole_technician_ritornaDueTecnici() throws Exception {
        List<User> technicians = userDAO.findByRole(Role.TECHNICIAN);
        assertEquals(2, technicians.size());
        assertTrue(technicians.stream().allMatch(u -> u.getRole() == Role.TECHNICIAN));
    }
}
