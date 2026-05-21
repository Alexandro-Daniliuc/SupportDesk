package it.uniroma2.dicii.ispw.supportdesk.fx;

import it.uniroma2.dicii.ispw.supportdesk.enumerator.ApplicationMode;
import it.uniroma2.dicii.ispw.supportdesk.utility.facade.SlaFacade;
import it.uniroma2.dicii.ispw.supportdesk.utility.singleton.ApplicationModeManager;
import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SupportDeskApp extends Application {

    private static final Logger log = LoggerFactory.getLogger(SupportDeskApp.class);

    @Override
    public void start(Stage stage) throws IOException {
        SceneNavigator.init(stage);
        if (ApplicationModeManager.getInstanceSingleton().getMode() != ApplicationMode.DEMO) {
            rescheduleSlaTimers();
        }
        SceneNavigator.navigateTo("login.fxml", "SupportDesk — Login");
    }

    private void rescheduleSlaTimers() {
        try {
            SlaFacade.getInstanceSingleton().rescheduleAllSlaTimers();
        } catch (Exception e) {
            log.warn("Impossibile ricaricare timer SLA al boot: {}", e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
