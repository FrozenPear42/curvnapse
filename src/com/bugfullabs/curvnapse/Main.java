package com.bugfullabs.curvnapse;

import com.bugfullabs.curvnapse.gui.Board;
import com.bugfullabs.curvnapse.server.Server;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.logging.Logger;

public class Main extends Application {
    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    //FIXME: THROW IS SO FUCKING WRONG
    @Override
    public void start(Stage primaryStage) throws Exception {

        Group root = new Group();
        Board b = new Board(1000, 800);
        Server server = new Server(1337, 10);
        server.start();

        root.getChildren().add(b);
        primaryStage.setTitle("Curvnapse");
        primaryStage.setWidth(1000);
        primaryStage.setHeight(800);
        primaryStage.setScene(new Scene(root));
        primaryStage.setOnCloseRequest(event -> {
            LOG.info("Closing app!");
            server.close();
        });
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
