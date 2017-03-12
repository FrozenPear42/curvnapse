package com.bugfullabs.curvnapse;

import com.bugfullabs.curvnapse.gui.Board;
import com.bugfullabs.curvnapse.gui.LoginBox;
import com.bugfullabs.curvnapse.gui.MessageBox;
import com.bugfullabs.curvnapse.gui.MessageList;
import com.bugfullabs.curvnapse.network.message.HandshakeMessage;
import com.bugfullabs.curvnapse.network.message.Message;
import com.bugfullabs.curvnapse.network.client.ServerConnector;
import com.bugfullabs.curvnapse.network.message.TextMessage;
import com.bugfullabs.curvnapse.network.server.Server;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;

public class Main extends Application implements LoginBox.LoginListener {
    private static final Logger LOG = Logger.getLogger(Main.class.getName());
    private MessageBox messageList;

    @Override
    public void start(Stage primaryStage) {

        HBox root = new HBox();
        root.setAlignment(Pos.CENTER);
        messageList = new MessageBox();
        LoginBox loginBox = new LoginBox();
        Board b = new Board(1000, 800);

        loginBox.setLoginListener(this);
        root.getChildren().add(b);
        root.getChildren().add(messageList);
        root.getChildren().add(loginBox);

        primaryStage.setTitle("Curvnapse");
        primaryStage.setWidth(1600);
        primaryStage.setHeight(800);
        primaryStage.setScene(new Scene(root));

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void onLogin(String pName, String pIP, String pPort, boolean pHost) {
        if (pHost) {
            try {
                Server server = new Server(Integer.parseInt(pPort), 100);
                server.start();
                //primaryStage.setOnCloseRequest(event -> {
                //   LOG.info("Closing app!");
                //   server.close();
                //});
            } catch (Exception e) {
                LOG.warning("Could not start server");
                return;
            }
        }

        try {
            ServerConnector connector = new ServerConnector(pIP, Integer.parseInt(pPort), pName);
            connector.start();
            connector.registerListener(pMessage -> {
                if (pMessage instanceof TextMessage)
                    messageList.addMessage((TextMessage) pMessage);
                else if (pMessage instanceof HandshakeMessage)
                    messageList.addMessage(new TextMessage("Server", ((HandshakeMessage) (pMessage)).getName() + " joined!"));
            });
            messageList.setSendListener(pMessage -> connector.sendMessage(new TextMessage(pName, pMessage)));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
}
