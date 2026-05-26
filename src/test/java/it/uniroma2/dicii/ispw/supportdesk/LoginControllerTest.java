package it.uniroma2.dicii.ispw.supportdesk;

import it.uniroma2.dicii.ispw.supportdesk.bean.LoginBean;
import it.uniroma2.dicii.ispw.supportdesk.controller.applicativo.LoginController;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.ApplicationMode;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Role;
import it.uniroma2.dicii.ispw.supportdesk.exception.AuthenticationException;
import it.uniroma2.dicii.ispw.supportdesk.exception.ValidationException;
import it.uniroma2.dicii.ispw.supportdesk.record.LoginRecord;
import it.uniroma2.dicii.ispw.supportdesk.utility.singleton.ApplicationModeManager;
import it.uniroma2.dicii.ispw.supportdesk.utility.singleton.UserSession;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("LoginController Tests")
class LoginControllerTest {

    private LoginController loginController;

    @BeforeAll
    static void setDemoMode() {
        ApplicationModeManager.getInstanceSingleton().setMode(ApplicationMode.DEMO);
    }

    @BeforeEach
    void setUp() {
        loginController = new LoginController();
        UserSession.getInstanceSingleton().clear();
    }

    @Test
    @DisplayName("Login valido: ritorna LoginRecord non null e popola UserSession con email e ruolo")
    void loginValido_ritornaLoginRecord_e_settaUserSession() throws Exception {
        LoginBean bean = new LoginBean();
        bean.setEmail("giovanni.rossi@azienda.it");
        bean.setPassword("password");

        LoginRecord record = loginController.authenticate(bean);

        assertNotNull(record);
        assertEquals("giovanni.rossi@azienda.it", UserSession.getInstanceSingleton().getEmail());
        assertEquals(Role.USER, UserSession.getInstanceSingleton().getRole());
    }

    @Test
    @DisplayName("Email formato non valido: lancia ValidationException")
    void emailFormatoNonValido_lancia_ValidationException() {
        LoginBean bean = new LoginBean();
        bean.setEmail("emailsenzachiocciola.com");
        bean.setPassword("password");

        assertThrows(ValidationException.class, () -> loginController.authenticate(bean));
    }

    @Test
    @DisplayName("Email valida ma non registrata nel sistema: lancia AuthenticationException")
    void emailValidaMaNonPresente_lancia_AuthenticationException() {
        LoginBean bean = new LoginBean();
        bean.setEmail("nonexistent@example.com");
        bean.setPassword("password");

        assertThrows(AuthenticationException.class, () -> loginController.authenticate(bean));
    }
}
