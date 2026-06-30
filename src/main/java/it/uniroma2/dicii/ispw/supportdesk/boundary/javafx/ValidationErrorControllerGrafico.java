package it.uniroma2.dicii.ispw.supportdesk.boundary.javafx;

import it.uniroma2.dicii.ispw.supportdesk.fx.SceneNavigator;
import javafx.fxml.FXML;

import java.io.IOException;

public class ValidationErrorControllerGrafico {

    @FXML
    public void onOk() throws IOException {
        SceneNavigator.navigateTo("open-ticket.fxml", "Apri Ticket");
    }
}
