package it.uniroma2.dicii.ispw.supportdesk.fx;

import it.uniroma2.dicii.ispw.supportdesk.utility.configloader.ConfigLoader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public final class SceneNavigator {

    private static final int SCENE_WIDTH  = Integer.parseInt(
            ConfigLoader.getInstanceSingleton().get("scene.width", "1100"));
    private static final int SCENE_HEIGHT = Integer.parseInt(
            ConfigLoader.getInstanceSingleton().get("scene.height", "700"));

    private static Stage stage;

    private SceneNavigator() {}

    public static void init(Stage s) {
        stage = s;
        stage.setResizable(true);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
    }

    public static void navigateTo(String fxml, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                SceneNavigator.class.getResource(fxml));
        Scene scene = new Scene(loader.load());
        stage.setTitle("SupportDesk — " + title);
        stage.setScene(scene);
        if (!stage.isShowing()) {
            stage.setWidth(SCENE_WIDTH);
            stage.setHeight(SCENE_HEIGHT);
        }
        stage.show();
    }
}
