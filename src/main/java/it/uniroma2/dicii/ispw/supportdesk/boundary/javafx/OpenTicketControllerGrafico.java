package it.uniroma2.dicii.ispw.supportdesk.boundary.javafx;

import it.uniroma2.dicii.ispw.supportdesk.bean.TicketBean;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Category;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Priority;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.SupportDeskException;
import it.uniroma2.dicii.ispw.supportdesk.fx.SceneNavigator;
import it.uniroma2.dicii.ispw.supportdesk.utility.facade.TicketFacade;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class OpenTicketControllerGrafico {

    @FXML private TextField           titleField;
    @FXML private TextArea            descriptionArea;
    @FXML private ComboBox<Category>  categoryBox;
    @FXML private ComboBox<Priority>  priorityBox;
    @FXML private Label               errorLabel;

    private static final Logger log = LoggerFactory.getLogger(OpenTicketControllerGrafico.class);

    private TicketFacade ticketFacade;

    private record PendingFormData(String title, String description, Category category, Priority priority) {}
    private static PendingFormData pendingData = null;

    @FXML
    public void initialize() {
        ticketFacade = TicketFacade.getInstanceSingleton();
        categoryBox.setItems(FXCollections.observableArrayList(Category.values()));
        priorityBox.setItems(FXCollections.observableArrayList(Priority.values()));
        errorLabel.setText("");
        if (pendingData != null) {
            titleField.setText(pendingData.title());
            descriptionArea.setText(pendingData.description());
            categoryBox.setValue(pendingData.category());
            priorityBox.setValue(pendingData.priority());
            pendingData = null;
        }
    }

    @FXML
    public void onSubmitTicket() {
        errorLabel.setText("");
        String title       = titleField.getText() != null ? titleField.getText().trim() : "";
        String description = descriptionArea.getText() != null ? descriptionArea.getText().trim() : "";
        Category category  = categoryBox.getValue();
        Priority priority  = priorityBox.getValue();

        if (title.isBlank() || description.isBlank() || category == null || priority == null) {
            pendingData = new PendingFormData(title, description, category, priority);
            try {
                SceneNavigator.navigateTo("validation-error.fxml", "Errore di Validazione");
            } catch (IOException e) {
                log.error("Errore navigazione validation-error", e);
                errorLabel.setText("Compila tutti i campi obbligatori.");
            }
            return;
        }

        submitTicket(title, description, category, priority);
    }

    public void submitTicket(String title, String description, Category category, Priority priority) {
        TicketBean bean = new TicketBean();
        bean.setTitle(title);
        bean.setDescription(description);
        bean.setCategory(category);
        bean.setPriority(priority);

        try {
            TicketBean result = ticketFacade.openTicketWithWorkflow(bean);
            TicketConfirmationControllerGrafico.setPendingTicketId(result.getId());
            SceneNavigator.navigateTo("ticket-confirmation.fxml", "Conferma Ticket");
        } catch (DAOException e) {
            log.error("Errore DAO apertura ticket", e);
            errorLabel.setText("Errore interno del sistema. Riprovare.");
        } catch (SupportDeskException e) {
            errorLabel.setText(e.getMessage());
        } catch (IOException e) {
            log.error("Errore navigazione conferma ticket", e);
        }
    }

    @FXML
    public void onBack() throws IOException {
        SceneNavigator.navigateTo("user-dashboard.fxml", "Dashboard Utente");
    }
}
