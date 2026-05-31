package it.uniroma2.dicii.ispw.supportdesk;

import it.uniroma2.dicii.ispw.supportdesk.bean.LoginBean;
import it.uniroma2.dicii.ispw.supportdesk.bean.TicketBean;
import it.uniroma2.dicii.ispw.supportdesk.controller.applicativo.LoginController;
import it.uniroma2.dicii.ispw.supportdesk.controller.applicativo.SubmitTicketController;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.ApplicationMode;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Category;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Priority;
import it.uniroma2.dicii.ispw.supportdesk.exception.ValidationException;
import it.uniroma2.dicii.ispw.supportdesk.record.TicketRecord;
import it.uniroma2.dicii.ispw.supportdesk.utility.facade.TicketFacade;
import it.uniroma2.dicii.ispw.supportdesk.utility.singleton.ApplicationModeManager;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OpenTicketWorkflowTest {

    private static final String USER_EMAIL = "giovanni.rossi@azienda.it";

    @BeforeAll
    static void setupDemoLogin() throws Exception {
        ApplicationModeManager.getInstanceSingleton().setMode(ApplicationMode.DEMO);
        LoginBean lb = new LoginBean();
        lb.setEmail(USER_EMAIL);
        lb.setPassword("password");
        new LoginController().authenticate(lb);
    }

    @Test
    @Order(1)
    void openTicketValido_idAssegnato_e_recuperabile() throws Exception {
        TicketBean bean = new TicketBean();
        bean.setTitle("Problema accesso rete");
        bean.setDescription("Impossibile connettersi alla VPN aziendale");
        bean.setCategory(Category.NETWORK);
        bean.setPriority(Priority.HIGH);

        TicketBean result = TicketFacade.getInstanceSingleton().openTicketWithWorkflow(bean);

        assertTrue(result.getId() > 0, "Il ticket deve avere un ID assegnato");

        List<TicketRecord> tickets = new SubmitTicketController().getTicketsByUser(USER_EMAIL);
        assertTrue(tickets.stream().anyMatch(t -> t.id() == result.getId()),
                "Il ticket creato deve essere recuperabile per l'utente");
    }

    @Test
    @Order(2)
    void openTicketSenzaTitolo_lancia_ValidationException() {
        TicketBean bean = new TicketBean();
        bean.setDescription("Descrizione presente");
        bean.setCategory(Category.SOFTWARE);
        bean.setPriority(Priority.MEDIUM);

        assertThrows(ValidationException.class,
                () -> TicketFacade.getInstanceSingleton().openTicketWithWorkflow(bean));
    }

    @Test
    @Order(3)
    void openTicketSenzaCategoria_lancia_ValidationException() {
        TicketBean bean = new TicketBean();
        bean.setTitle("Titolo presente");
        bean.setDescription("Descrizione presente");
        bean.setPriority(Priority.LOW);

        assertThrows(ValidationException.class,
                () -> TicketFacade.getInstanceSingleton().openTicketWithWorkflow(bean));
    }
}
