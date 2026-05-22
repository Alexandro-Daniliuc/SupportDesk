package it.uniroma2.dicii.ispw.supportdesk.boundary.javafx;

import it.uniroma2.dicii.ispw.supportdesk.utility.facade.CorrelationFacade;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.InvalidTransitionException;
import it.uniroma2.dicii.ispw.supportdesk.exception.ReportException;
import it.uniroma2.dicii.ispw.supportdesk.exception.SupportDeskException;
import it.uniroma2.dicii.ispw.supportdesk.exception.TicketNotFoundException;
import it.uniroma2.dicii.ispw.supportdesk.fx.SceneNavigator;
import it.uniroma2.dicii.ispw.supportdesk.record.ReportRecord;
import it.uniroma2.dicii.ispw.supportdesk.record.TicketRecord;
import it.uniroma2.dicii.ispw.supportdesk.record.UserRecord;
import it.uniroma2.dicii.ispw.supportdesk.utility.facade.ReportFacade;
import it.uniroma2.dicii.ispw.supportdesk.utility.facade.SlaFacade;
import it.uniroma2.dicii.ispw.supportdesk.utility.facade.ViewTicketsFacade;
import it.uniroma2.dicii.ispw.supportdesk.utility.singleton.UserSession;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ManagerDashboardControllerGrafico extends AbstractDashboardControllerGrafico {

    private static final String COL_TITLE = "title";
    private static final String COL_STATUS = "status";
    private static final String ERR_TITLE = "Errore";



    @FXML private Label welcomeLabel;

    // Tab 1 — Tutti i ticket
    @FXML private TableView<TicketRecord>       allTicketsTable;
    @FXML private TableColumn<TicketRecord, Integer> colId;
    @FXML private TableColumn<TicketRecord, String>  colTitle;
    @FXML private TableColumn<TicketRecord, String>  colCategory;
    @FXML private TableColumn<TicketRecord, String>  colPriority;
    @FXML private TableColumn<TicketRecord, String>  colStatus;
    @FXML private TableColumn<TicketRecord, String>  colTech;
    @FXML private TableColumn<TicketRecord, String>  colSla;

    // Tab 2 — SLA
    @FXML private TableView<TicketRecord>       slaTable;
    @FXML private TableColumn<TicketRecord, Integer> slaColId;
    @FXML private TableColumn<TicketRecord, String>  slaColTitle;
    @FXML private TableColumn<TicketRecord, String>  slaColPriority;
    @FXML private TableColumn<TicketRecord, String>  slaColStatus;
    @FXML private TableColumn<TicketRecord, String>  slaColSla;

    // Tab 3 — Correlati
    @FXML private TextField                     correlationIdField;
    @FXML private Label                         correlationErrorLabel;
    @FXML private TableView<TicketRecord>       correlatedTable;
    @FXML private TableColumn<TicketRecord, Integer> corrColId;
    @FXML private TableColumn<TicketRecord, String>  corrColTitle;
    @FXML private TableColumn<TicketRecord, String>  corrColCategory;
    @FXML private TableColumn<TicketRecord, String>  corrColStatus;
    @FXML private TableColumn<TicketRecord, String>  corrColSla;

    // Tab 4 - Assegna Ticket
    @FXML private TableView<TicketRecord>       assignTicketTable;
    @FXML private TableColumn<TicketRecord, Integer> assignColId;
    @FXML private TableColumn<TicketRecord, String>  assignColTitle;
    @FXML private TableColumn<TicketRecord, String>  assignColStatus;
    @FXML private TableColumn<TicketRecord, String>  assignColTech;
    @FXML private ComboBox<String>              technicianComboBox;
    @FXML private Label                         assignErrorLabel;

    // Tab 5 - Report
    @FXML private TextField                     reportTitleField;
    @FXML private DatePicker                    reportStartDate;
    @FXML private DatePicker                    reportEndDate;
    @FXML private Label                         reportErrorLabel;
    @FXML private VBox                          reportResultPanel;
    @FXML private Label                         reportSummaryLabel;
    @FXML private TableView<TicketRecord>       reportTicketsTable;
    @FXML private TableColumn<TicketRecord, Integer> repColId;
    @FXML private TableColumn<TicketRecord, String>  repColTitle;
    @FXML private TableColumn<TicketRecord, String>  repColCategory;
    @FXML private TableColumn<TicketRecord, String>  repColStatus;
    @FXML private TableColumn<TicketRecord, String>  repColSla;

    private final List<UserRecord> availableTechnicians = new ArrayList<>();

    @FXML
    public void initialize() {
        welcomeLabel.setText("Benvenuto, " + SessionContext.getCurrentUser().name());
        bindAllTicketsTable();
        bindSlaTable();
        bindCorrelatedTable();
        bindAssignTable();
        bindReportTable();

        allTicketsTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, o, n) -> { if (n != null) populateDetail(n); else hideDetail(); });
        slaTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, o, n) -> { if (n != null) populateDetail(n); else hideDetail(); });
        correlatedTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, o, n) -> { if (n != null) populateDetail(n); else hideDetail(); });

        loadAllTickets();
        loadTechnicians();
    }

    @FXML
    public void onRefreshAll() { loadAllTickets(); }

    @FXML
    public void onCheckSla() {
        try {
            List<TicketRecord> expiring = SlaFacade.getInstanceSingleton().getTicketsWithSlaExpiringSoon();
            slaTable.setItems(FXCollections.observableArrayList(expiring));
        } catch (DAOException e) {
            log.error("Errore controllo SLA", e);
            showError(ERR_TITLE, ERR_INTERNAL);
        }
    }

    @FXML
    public void onFindCorrelated() {
        correlationErrorLabel.setText("");
        String raw = correlationIdField.getText().trim();
        int ticketId;
        try {
            ticketId = Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            correlationErrorLabel.setText("Inserisci un ID numerico valido.");
            return;
        }
        try {
            List<TicketRecord> correlated = CorrelationFacade.getInstanceSingleton().findCorrelations(ticketId);
            correlatedTable.setItems(FXCollections.observableArrayList(correlated));
        } catch (DAOException e) {
            log.error("Errore ricerca ticket correlati", e);
            showError(ERR_TITLE, ERR_INTERNAL);
        } catch (SupportDeskException e) {
            correlationErrorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void onLogout() throws IOException {
        UserSession.getInstanceSingleton().logout();
        SessionContext.clear();
        SceneNavigator.navigateTo("login.fxml", "Login");
    }

    @Override
    protected void refreshAll() {
        loadAllTickets();
    }

    private void loadAllTickets() {
        try {
            allTicketsTable.setItems(FXCollections.observableArrayList(
                    ViewTicketsFacade.getInstanceSingleton().getAllTickets()));
        } catch (DAOException e) {
            log.error("Errore caricamento tutti i ticket", e);
            showError(ERR_TITLE, "Impossibile caricare i ticket.");
        }
    }

    private void bindAllTicketsTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>(COL_TITLE));
        colCategory.setCellValueFactory(new PropertyValueFactory<>(PROP_CATEGORY));
        colPriority.setCellValueFactory(new PropertyValueFactory<>(PROP_PRIORITY));
        colStatus.setCellValueFactory(new PropertyValueFactory<>(COL_STATUS));
        colTech.setCellValueFactory(new PropertyValueFactory<>("assignedTechnicianName"));
        colSla.setCellValueFactory(new PropertyValueFactory<>(PROP_SCADENZA_SLA));
    }

    private void bindSlaTable() {
        slaColId.setCellValueFactory(new PropertyValueFactory<>("id"));
        slaColTitle.setCellValueFactory(new PropertyValueFactory<>(COL_TITLE));
        slaColPriority.setCellValueFactory(new PropertyValueFactory<>(PROP_PRIORITY));
        slaColStatus.setCellValueFactory(new PropertyValueFactory<>(COL_STATUS));
        slaColSla.setCellValueFactory(new PropertyValueFactory<>(PROP_SCADENZA_SLA));
    }

    private void bindCorrelatedTable() {
        corrColId.setCellValueFactory(new PropertyValueFactory<>("id"));
        corrColTitle.setCellValueFactory(new PropertyValueFactory<>(COL_TITLE));
        corrColCategory.setCellValueFactory(new PropertyValueFactory<>(PROP_CATEGORY));
        corrColStatus.setCellValueFactory(new PropertyValueFactory<>(COL_STATUS));
        corrColSla.setCellValueFactory(new PropertyValueFactory<>(PROP_SCADENZA_SLA));
    }

    private void bindAssignTable() {
        assignColId.setCellValueFactory(new PropertyValueFactory<>("id"));
        assignColTitle.setCellValueFactory(new PropertyValueFactory<>(COL_TITLE));
        assignColStatus.setCellValueFactory(new PropertyValueFactory<>(COL_STATUS));
        assignColTech.setCellValueFactory(new PropertyValueFactory<>("assignedTechnicianName"));
    }

    private void bindReportTable() {
        repColId.setCellValueFactory(new PropertyValueFactory<>("id"));
        repColTitle.setCellValueFactory(new PropertyValueFactory<>(COL_TITLE));
        repColCategory.setCellValueFactory(new PropertyValueFactory<>(PROP_CATEGORY));
        repColStatus.setCellValueFactory(new PropertyValueFactory<>(COL_STATUS));
        repColSla.setCellValueFactory(new PropertyValueFactory<>(PROP_SCADENZA_SLA));
    }

    private void loadTechnicians() {
        try {
            availableTechnicians.clear();
            availableTechnicians.addAll(ViewTicketsFacade.getInstanceSingleton().getAvailableTechnicians());
            List<String> names = availableTechnicians.stream()
                    .map(u -> u.name() + " " + u.surname())
                    .toList();
            technicianComboBox.setItems(FXCollections.observableArrayList(names));
        } catch (DAOException e) {
            log.error("Errore caricamento tecnici", e);
        }
    }

    private void loadAssignTickets() {
        try {
            assignTicketTable.setItems(FXCollections.observableArrayList(
                    ViewTicketsFacade.getInstanceSingleton().getAllTickets()));
        } catch (DAOException e) {
            log.error("Errore caricamento ticket assegnazione", e);
            showError(ERR_TITLE, "Impossibile caricare i ticket.");
        }
    }

    @FXML
    public void onRefreshAssign() {
        loadAssignTickets();
        loadTechnicians();
    }

    @FXML
    public void onAssignTicket() {
        assignErrorLabel.setStyle("-fx-text-fill: #C62828; -fx-font-size: 11px;");
        assignErrorLabel.setText("");
        TicketRecord selected = assignTicketTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            assignErrorLabel.setText("Seleziona un ticket dalla lista.");
            return;
        }
        int techIdx = technicianComboBox.getSelectionModel().getSelectedIndex();
        if (techIdx < 0) {
            assignErrorLabel.setText("Seleziona un tecnico.");
            return;
        }
        String techEmail = availableTechnicians.get(techIdx).email();
        try {
            ViewTicketsFacade.getInstanceSingleton().assignTechnician(selected.id(), techEmail);
            loadAllTickets();
            loadAssignTickets();
            assignErrorLabel.setStyle("-fx-text-fill: #2E7D32; -fx-font-size: 11px;");
            assignErrorLabel.setText("Ticket assegnato con successo.");
        } catch (TicketNotFoundException e) {
            assignErrorLabel.setText("Ticket non trovato.");
        } catch (InvalidTransitionException e) {
            assignErrorLabel.setText("Transizione non valida per questo ticket.");
        } catch (DAOException e) {
            log.error("Errore assegnazione ticket {}", selected.id(), e);
            showError(ERR_TITLE, ERR_INTERNAL);
        }
    }

    @FXML
    public void onGenerateReport() {
        reportErrorLabel.setText("");
        reportResultPanel.setVisible(false);
        reportResultPanel.setManaged(false);
        String title = reportTitleField.getText().trim();
        if (title.isBlank()) {
            reportErrorLabel.setText("Il titolo è obbligatorio.");
            return;
        }
        LocalDate startDate = reportStartDate.getValue();
        LocalDate endDate   = reportEndDate.getValue();
        LocalDateTime start = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime end   = endDate   != null ? endDate.atTime(23, 59, 59) : null;
        try {
            ReportRecord report = ReportFacade.getInstanceSingleton().generateReport(title, start, end);
            reportSummaryLabel.setText("\"" + report.title() + "\" — " + report.totalTickets() + " ticket trovati");
            reportTicketsTable.setItems(FXCollections.observableArrayList(report.tickets()));
            reportResultPanel.setVisible(true);
            reportResultPanel.setManaged(true);
        } catch (ReportException e) {
            reportErrorLabel.setText(e.getMessage());
        } catch (DAOException e) {
            log.error("Errore generazione report", e);
            showError(ERR_TITLE, ERR_INTERNAL);
        }
    }

    @FXML
    public void onCloseDetail() {
        hideDetail();
        allTicketsTable.getSelectionModel().clearSelection();
        slaTable.getSelectionModel().clearSelection();
        correlatedTable.getSelectionModel().clearSelection();
    }
}
