package it.uniroma2.dicii.ispw.supportdesk.boundary.javafx;

import it.uniroma2.dicii.ispw.supportdesk.bean.LoginBean;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Role;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.SupportDeskException;
import it.uniroma2.dicii.ispw.supportdesk.fx.SceneNavigator;
import it.uniroma2.dicii.ispw.supportdesk.record.LoginRecord;
import it.uniroma2.dicii.ispw.supportdesk.utility.facade.LoginFacade;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class LoginControllerGrafico {

    private static final Logger log = LoggerFactory.getLogger(LoginControllerGrafico.class);


    @FXML private TextField     emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label         errorLabel;

    @FXML
    public void onLogin() {
        errorLabel.setText("");
        LoginBean bean = new LoginBean();
        bean.setEmail(emailField.getText().trim());
        bean.setPassword(passwordField.getText());

        try {
            LoginRecord loginRecord = LoginFacade.getInstanceSingleton().login(bean);
            navigateToDashboard(loginRecord);
        } catch (DAOException e) {
            log.error("Errore DAO durante il login", e);
            showError("Errore", "Errore interno del sistema. Riprovare.");
        } catch (SupportDeskException e) {
            errorLabel.setText(e.getMessage());
        } catch (IOException e) {
            log.error("Errore navigazione dashboard", e);
            showError("Errore", "Impossibile aprire la dashboard.");
        }
    }

    private void navigateToDashboard(LoginRecord loginRecord) throws IOException {
        SessionContext.setCurrentUser(loginRecord);
        if (loginRecord.role() == Role.USER) {
            SceneNavigator.navigateTo("user-dashboard.fxml", "Dashboard Utente");
        } else if (loginRecord.role() == Role.TECHNICIAN) {
            SceneNavigator.navigateTo("tech-dashboard.fxml", "Dashboard Tecnico");
        } else {
            SceneNavigator.navigateTo("manager-dashboard.fxml", "Dashboard Manager");
        }
    }

    @FXML
    public void onGoToRegister() {
        try {
            SceneNavigator.navigateTo("registration.fxml", "Registrazione");
        } catch (IOException e) {
            log.error("Errore navigazione registrazione", e);
        }
    }

    private void showError(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
