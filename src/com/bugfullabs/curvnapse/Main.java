package com.bugfullabs.curvnapse;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main application class
 */
public class Main extends Application {

    /**
     *  function called after gaining all the resources from system
     * @param primaryStage window handle
     */
    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Curvnapse");
        primaryStage.setWidth(1150);
        primaryStage.setHeight(730);
        primaryStage.setResizable(false);
        primaryStage.show();
        FlowManager.getInstance().initialize(primaryStage);
        FlowManager.getInstance().loginScene();
        //FlowManager.getInstance().testMode();
    }

    /**
     * Just standard main
     * @param args commandline args
     */
    public static void main(String[] args) {
        launch(args);
    }

}
