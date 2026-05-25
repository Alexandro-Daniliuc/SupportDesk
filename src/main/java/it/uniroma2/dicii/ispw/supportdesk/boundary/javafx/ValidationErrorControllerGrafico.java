package it.uniroma2.dicii.ispw.supportdesk.boundary.javafx;

import it.uniroma2.dicii.ispw.supportdesk.fx.SceneNavigator;
import javafx.fxml.FXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ValidationErrorControllerGrafico {

    private static final Logger log = LoggerFactory.getLogger(ValidationErrorControllerGrafico.class);

    @FXML
    public void onOk() throws IOException {
        SceneNavigator.navigateTo("open-ticket.fxml", "Apri Ticket");
    }
}
