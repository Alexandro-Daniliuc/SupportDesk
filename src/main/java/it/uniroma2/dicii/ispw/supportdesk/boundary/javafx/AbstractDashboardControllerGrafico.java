package it.uniroma2.dicii.ispw.supportdesk.boundary.javafx;

import it.uniroma2.dicii.ispw.supportdesk.enumerator.Priority;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.TicketNotFoundException;
import it.uniroma2.dicii.ispw.supportdesk.record.TicketRecord;
import it.uniroma2.dicii.ispw.supportdesk.utility.facade.ViewTicketsFacade;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDashboardControllerGrafico {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected static final String PROP_CATEGORY     = "category";
    protected static final String PROP_PRIORITY     = "priority";
    protected static final String PROP_SCADENZA_SLA = "scadenzaSla";

    protected static final String ERR_DIALOG_TITLE  = "Errore";
    protected static final String ERR_INTERNAL      = "Errore interno del sistema.";

    @FXML protected VBox  detailPanel;
    @FXML protected Label detailId;
    @FXML protected Label detailTitle;
    @FXML protected Label detailDescription;
    @FXML protected Label detailCategory;
    @FXML protected Label detailStatus;
    @FXML protected Label detailDataApertura;
    @FXML protected Label detailSla;
    @FXML protected Label detailTechnician;
    @FXML protected ComboBox<String> priorityEditBox;
    @FXML protected Label prioritySaveLabel;

    protected int currentTicketId;

    protected void populateDetail(TicketRecord t) {
        currentTicketId = t.id();
        detailId.setText(String.valueOf(t.id()));
        detailTitle.setText(t.title());
        detailDescription.setText(t.description());
        detailCategory.setText(t.getCategory());
        detailStatus.setText(t.getStatus());
        detailDataApertura.setText(t.getDataApertura());
        detailSla.setText(t.getScadenzaSla());
        detailTechnician.setText(t.getAssignedTechnicianName().isBlank()
                ? "Non assegnato" : t.getAssignedTechnicianName());
        if (priorityEditBox != null) {
            if (priorityEditBox.getItems().isEmpty()) {
                for (Priority p : Priority.values()) priorityEditBox.getItems().add(p.name());
            }
            priorityEditBox.setValue(t.getPriority());
        }
        if (prioritySaveLabel != null) prioritySaveLabel.setText("");
        detailPanel.setVisible(true);
        detailPanel.setManaged(true);
    }

    @FXML
    public void onSavePriority() {
        if (priorityEditBox == null || priorityEditBox.getValue() == null) return;
        if (prioritySaveLabel != null) prioritySaveLabel.setText("");
        try {
            ViewTicketsFacade.getInstanceSingleton().changePriority(currentTicketId, priorityEditBox.getValue());
            if (prioritySaveLabel != null) {
                prioritySaveLabel.setStyle("-fx-text-fill: #2E7D32; -fx-font-size: 11px;");
                prioritySaveLabel.setText("Priorità aggiornata.");
            }
            refreshAll();
        } catch (DAOException | TicketNotFoundException e) {
            log.error("Errore aggiornamento priorità ticket {}", currentTicketId, e);
            showError(ERR_DIALOG_TITLE, ERR_INTERNAL);
        }
    }

    protected void refreshAll() {
        // override nelle sottoclassi che gestiscono una tabella da aggiornare
    }

    protected void hideDetail() {
        detailPanel.setVisible(false);
        detailPanel.setManaged(false);
    }

    protected void showError(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
