package com.bugfullabs.curvnapse;

import com.bugfullabs.curvnapse.scene.LoginScene;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.util.logging.Logger;

public class Main extends Application {
    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Curvnapse");
        primaryStage.setWidth(1000);
        primaryStage.setHeight(700);
        primaryStage.setScene(new LoginScene(primaryStage).getScene());
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
