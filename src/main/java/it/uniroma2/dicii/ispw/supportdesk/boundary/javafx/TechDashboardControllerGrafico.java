package it.uniroma2.dicii.ispw.supportdesk.boundary.javafx;

import it.uniroma2.dicii.ispw.supportdesk.utility.facade.KnowledgeBaseFacade;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.TicketStatus;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.KnowledgeBaseException;
import it.uniroma2.dicii.ispw.supportdesk.exception.SupportDeskException;
import it.uniroma2.dicii.ispw.supportdesk.fx.SceneNavigator;
import it.uniroma2.dicii.ispw.supportdesk.record.CommentRecord;
import it.uniroma2.dicii.ispw.supportdesk.record.KnowledgeEntryRecord;
import it.uniroma2.dicii.ispw.supportdesk.record.TicketRecord;
import it.uniroma2.dicii.ispw.supportdesk.utility.facade.ViewTicketsFacade;
import it.uniroma2.dicii.ispw.supportdesk.utility.singleton.UserSession;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.List;

public class TechDashboardControllerGrafico extends AbstractDashboardControllerGrafico {



    @FXML private Label welcomeLabel;
    @FXML private Label actionErrorLabel;

    @FXML private TableView<TicketRecord>               ticketTable;
    @FXML private TableColumn<TicketRecord, Integer>    colId;
    @FXML private TableColumn<TicketRecord, String>     colTitle;
    @FXML private TableColumn<TicketRecord, String>     colCategory;
    @FXML private TableColumn<TicketRecord, String>     colPriority;
    @FXML private TableColumn<TicketRecord, String>     colStatus;
    @FXML private TableColumn<TicketRecord, String>     colSla;

    @FXML private ListView<String>  commentsList;
    @FXML private TextField         kbSearchField;
    @FXML private ListView<String>  kbResultsList;
    @FXML private TextField         kbTitleField;
    @FXML private TextArea          kbContentField;
    @FXML private Label             kbAddErrorLabel;

    @FXML
    public void initialize() {
        welcomeLabel.setText("Benvenuto, " + SessionContext.getCurrentUser().name());

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>(PROP_CATEGORY));
        colPriority.setCellValueFactory(new PropertyValueFactory<>(PROP_PRIORITY));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colSla.setCellValueFactory(new PropertyValueFactory<>(PROP_SCADENZA_SLA));

        ticketTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if (newVal != null) populateDetail(newVal);
                    else hideDetail();
                });

        loadAssignedTickets();
    }

    @Override
    protected void populateDetail(TicketRecord t) {
        super.populateDetail(t);
        loadComments(t.id());
    }

    @FXML
    public void onRefresh() {
        loadAssignedTickets();
    }

    @FXML
    public void onTakeCharge() {
        TicketRecord selected = ticketTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            actionErrorLabel.setText("Seleziona un ticket dalla lista.");
            return;
        }
        actionErrorLabel.setText("");
        try {
            if (selected.status() == TicketStatus.OPEN || selected.status() == TicketStatus.REOPENED) {
                String myEmail = SessionContext.getCurrentUser().email();
                ViewTicketsFacade.getInstanceSingleton().assignTechnician(selected.id(), myEmail);
            }
            changeStatus(selected.id(), TicketStatus.IN_PROGRESS);
        } catch (DAOException e) {
            log.error("Errore presa in carico ticket {}", selected.id(), e);
            showError(ERR_DIALOG_TITLE, "Errore interno del sistema.");
        } catch (SupportDeskException e) {
            actionErrorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void onResolve() {
        TicketRecord selected = ticketTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            actionErrorLabel.setText("Seleziona un ticket dalla lista.");
            return;
        }
        changeStatus(selected.id(), TicketStatus.RESOLVED);
    }

    @FXML
    public void onKbSearch() {
        String keyword = kbSearchField.getText().trim();
        if (keyword.isBlank()) {
            kbResultsList.setItems(FXCollections.emptyObservableList());
            return;
        }
        try {
            List<KnowledgeEntryRecord> results = KnowledgeBaseFacade.getInstanceSingleton().searchEntries(keyword);
            List<String> display = results.stream()
                    .map(r -> "[" + r.authorName() + "] " + r.title() + " — " + r.content())
                    .toList();
            kbResultsList.setItems(FXCollections.observableArrayList(display));
        } catch (KnowledgeBaseException e) {
            showError("Knowledge Base", e.getMessage());
        } catch (DAOException e) {
            log.error("Errore ricerca knowledge base", e);
            showError(ERR_DIALOG_TITLE,"Errore interno del sistema.");
        }
    }

    @FXML
    public void onAddKbEntry() {
        kbAddErrorLabel.setText("");
        String title   = kbTitleField.getText().trim();
        String content = kbContentField.getText().trim();
        if (title.isBlank() || content.isBlank()) {
            kbAddErrorLabel.setText("Titolo e contenuto sono obbligatori.");
            return;
        }
        String authorEmail = SessionContext.getCurrentUser().email();
        try {
            KnowledgeBaseFacade.getInstanceSingleton().addEntry(title, content, authorEmail);
            kbTitleField.clear();
            kbContentField.clear();
            kbAddErrorLabel.setStyle("-fx-text-fill: #2E7D32; -fx-font-size: 11px;");
            kbAddErrorLabel.setText("Entry aggiunta con successo.");
        } catch (KnowledgeBaseException e) {
            kbAddErrorLabel.setStyle("-fx-text-fill: #C62828; -fx-font-size: 11px;");
            kbAddErrorLabel.setText(e.getMessage());
        } catch (DAOException e) {
            log.error("Errore aggiunta entry KB", e);
            showError(ERR_DIALOG_TITLE, "Errore interno del sistema.");
        }
    }

    @FXML
    public void onLogout() throws IOException {
        UserSession.getInstanceSingleton().logout();
        SessionContext.clear();
        SceneNavigator.navigateTo("login.fxml", "Login");
    }

    private void loadAssignedTickets() {
        actionErrorLabel.setText("");
        try {
            List<TicketRecord> tickets = ViewTicketsFacade.getInstanceSingleton()
                    .getAllTickets();
            ticketTable.setItems(FXCollections.observableArrayList(tickets));
        } catch (DAOException e) {
            log.error("Errore caricamento ticket tecnico", e);
            showError(ERR_DIALOG_TITLE,"Impossibile caricare i ticket assegnati.");
        }
    }

    private void changeStatus(int ticketId, TicketStatus newStatus) {
        actionErrorLabel.setText("");
        try {
            ViewTicketsFacade.getInstanceSingleton().changeStatus(ticketId, newStatus);
            loadAssignedTickets();
        } catch (DAOException e) {
            log.error("Errore cambio stato ticket {}", ticketId, e);
            showError(ERR_DIALOG_TITLE,"Errore interno del sistema.");
        } catch (SupportDeskException e) {
            actionErrorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void onCloseDetail() {
        hideDetail();
        ticketTable.getSelectionModel().clearSelection();
        if (commentsList != null) commentsList.getItems().clear();
    }

    private void loadComments(int ticketId) {
        if (commentsList == null) return;
        try {
            List<CommentRecord> comments = ViewTicketsFacade.getInstanceSingleton().getCommentsForTicket(ticketId);
            List<String> display = comments.stream()
                    .map(c -> "[" + c.authorEmail() + "] " + c.text())
                    .toList();
            commentsList.setItems(FXCollections.observableArrayList(display));
        } catch (DAOException e) {
            log.error("Errore caricamento commenti ticket {}", ticketId, e);
        }
    }
}
