package com.bugfullabs.curvnapse;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Curvnapse");
        primaryStage.setWidth(1100);
        primaryStage.setHeight(700);
        primaryStage.show();

        FlowManager.getInstance().initialize(primaryStage);
        FlowManager.getInstance().loginScene();
        //FlowManager.getInstance().testMode();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
