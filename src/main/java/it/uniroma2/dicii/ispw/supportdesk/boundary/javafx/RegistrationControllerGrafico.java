package it.uniroma2.dicii.ispw.supportdesk.boundary.javafx;

import it.uniroma2.dicii.ispw.supportdesk.bean.RegistrationBean;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.SupportDeskException;
import it.uniroma2.dicii.ispw.supportdesk.fx.SceneNavigator;
import it.uniroma2.dicii.ispw.supportdesk.utility.facade.RegistrationFacade;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class RegistrationControllerGrafico {

    private static final Logger log = LoggerFactory.getLogger(RegistrationControllerGrafico.class);

    @FXML private TextField     nameField;
    @FXML private TextField     surnameField;
    @FXML private TextField     emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label         errorLabel;

    @FXML
    public void onRegister() {
        errorLabel.setText("");
        RegistrationBean bean = new RegistrationBean();
        bean.setName(nameField.getText().trim());
        bean.setSurname(surnameField.getText().trim());
        bean.setEmail(emailField.getText().trim());
        bean.setPassword(passwordField.getText());
        bean.setConfirmPassword(confirmPasswordField.getText());

        try {
            RegistrationFacade.getInstanceSingleton().register(bean);
            SceneNavigator.navigateTo("login.fxml", "Accedi");
        } catch (DAOException e) {
            log.error("Errore DAO durante la registrazione", e);
            showError("Errore", "Errore interno del sistema. Riprovare.");
        } catch (SupportDeskException e) {
            errorLabel.setText(e.getMessage());
        } catch (IOException e) {
            log.error("Errore navigazione login", e);
            showError("Errore", "Impossibile tornare al login.");
        }
    }

    @FXML
    public void onGoToLogin() {
        try {
            SceneNavigator.navigateTo("login.fxml", "Accedi");
        } catch (IOException e) {
            log.error("Errore navigazione login", e);
        }
    }

    private void showError(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
