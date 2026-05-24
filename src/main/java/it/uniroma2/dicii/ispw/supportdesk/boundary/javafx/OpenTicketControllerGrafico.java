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

    @FXML
    public void initialize() {
        ticketFacade = TicketFacade.getInstanceSingleton();
        categoryBox.setItems(FXCollections.observableArrayList(Category.values()));
        priorityBox.setItems(FXCollections.observableArrayList(Priority.values()));
        showForm();
    }

    public void showForm() {
        errorLabel.setText("");
        titleField.clear();
        descriptionArea.clear();
        categoryBox.setValue(null);
        priorityBox.setValue(null);
    }

    @FXML
    public void onSubmitTicket() {
        errorLabel.setText("");
        String title       = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        Category category  = categoryBox.getValue();
        Priority priority  = priorityBox.getValue();
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
            showConfirmation(result.getId());
        } catch (DAOException e) {
            log.error("Errore DAO apertura ticket", e);
            showError("Errore interno del sistema. Riprovare.");
        } catch (SupportDeskException e) {
            showError(e.getMessage());
        }
    }

    public void showConfirmation(int ticketId) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ticket aperto");
        alert.setContentText("Il tuo ticket #" + ticketId + " e stato aperto con successo.");
        alert.showAndWait();
        try {
            SceneNavigator.navigateTo("user-dashboard.fxml", "Dashboard Utente");
        } catch (IOException e) {
            log.error("Errore navigazione dopo conferma ticket", e);
        }
    }

    public void showError(String message) {
        errorLabel.setText(message);
    }

    @FXML
    public void onBack() throws IOException {
        SceneNavigator.navigateTo("user-dashboard.fxml", "Dashboard Utente");
    }
}
