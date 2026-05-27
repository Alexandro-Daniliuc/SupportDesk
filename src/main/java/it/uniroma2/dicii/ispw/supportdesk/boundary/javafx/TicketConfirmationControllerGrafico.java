package it.uniroma2.dicii.ispw.supportdesk.boundary.javafx;

import it.uniroma2.dicii.ispw.supportdesk.fx.SceneNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class TicketConfirmationControllerGrafico {

    @FXML private Label ticketIdLabel;

    private static final Logger log = LoggerFactory.getLogger(TicketConfirmationControllerGrafico.class);

    private static int pendingTicketId = 0;

    public static void setPendingTicketId(int id) {
        pendingTicketId = id;
    }

    @FXML
    public void initialize() {
        ticketIdLabel.setText("Il tuo ticket #" + pendingTicketId + " è stato registrato nel sistema.");
        pendingTicketId = 0;
    }

    @FXML
    public void onBack() {
        try {
            SceneNavigator.navigateTo("open-ticket.fxml", "Apri Ticket");
        } catch (IOException e) {
            log.error("Errore durante la navigazione alla schermata Apri Ticket", e);
        }
    }
}
