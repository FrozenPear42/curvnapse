package com.bugfullabs.curvnapse;

import com.bugfullabs.curvnapse.gui.Board;
import com.bugfullabs.curvnapse.gui.MessageBox;
import com.bugfullabs.curvnapse.gui.MessageList;
import com.bugfullabs.curvnapse.network.message.HandshakeMessage;
import com.bugfullabs.curvnapse.network.message.Message;
import com.bugfullabs.curvnapse.network.client.ServerConnector;
import com.bugfullabs.curvnapse.network.message.TextMessage;
import com.bugfullabs.curvnapse.network.server.Server;
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
        MessageBox messageList = new MessageBox();
        Board b = new Board(1000, 800);

        try {
            Server server = new Server(1337, 10);
            server.start();
            primaryStage.setOnCloseRequest(event -> {
                LOG.info("Closing app!");
                server.close();
            });
        } catch (Exception e) {
            LOG.warning("Could not start server");
        }

        ServerConnector connector = new ServerConnector("127.0.0.1", 1337, "To ja test");
        messageList.setSendListener(pMessage -> connector.sendMessage(new TextMessage(pMessage)));
        root.getChildren().add(b);
        root.getChildren().add(messageList);
        primaryStage.setTitle("Curvnapse");
        primaryStage.setWidth(1000);
        primaryStage.setHeight(800);
        primaryStage.setScene(new Scene(root));

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
