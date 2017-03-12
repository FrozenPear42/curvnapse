package com.bugfullabs.curvnapse;

import com.bugfullabs.curvnapse.gui.*;
import com.bugfullabs.curvnapse.network.client.Game;
import com.bugfullabs.curvnapse.network.message.GameCreateRequestMessage;
import com.bugfullabs.curvnapse.network.message.HandshakeMessage;
import com.bugfullabs.curvnapse.network.client.ServerConnector;
import com.bugfullabs.curvnapse.network.message.TextMessage;
import com.bugfullabs.curvnapse.network.server.Server;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.logging.Logger;

public class Main extends Application implements LoginBox.LoginListener {
    private static final Logger LOG = Logger.getLogger(Main.class.getName());
    private MessageBox messageList;

    private Scene mLoginScene;
    private Scene mLobbyScene;
    private Scene mGameLobbyScene;
    private Scene mGameScene;
    private Stage mMainStage;

    @Override
    public void start(Stage primaryStage) {

        mMainStage = primaryStage;

        HBox loginRoot = new HBox();
        loginRoot.setAlignment(Pos.CENTER);
        LoginBox loginBox = new LoginBox();
        loginBox.setLoginListener(this);
        loginRoot.getChildren().add(loginBox);
        mLoginScene = new Scene(loginRoot);

        FlowPane lobbyRoot = new FlowPane();
        messageList = new MessageBox();
        GameList gameList = new GameList();
        gameList.addGame(new Game());
        lobbyRoot.getChildren().addAll(messageList, gameList);
        mLobbyScene = new Scene(lobbyRoot);

        Board b = new Board(1000, 800);

        primaryStage.setTitle("Curvnapse");
        primaryStage.setWidth(1800);
        primaryStage.setHeight(800);
        primaryStage.setScene(mLoginScene);

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void onLogin(String pName, String pIP, String pPort, boolean pHost) {
        if (pName.isEmpty()) {
            return;
        }

        if (pHost) {
            try {
                Server server = new Server(Integer.parseInt(pPort), 100);
                server.start();
                mMainStage.setOnCloseRequest(event -> {
                    LOG.info("Closing app!");
                    server.close();
                });
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
            connector.sendMessage(new GameCreateRequestMessage("Wichrowski ciota", "", 8));
        } catch (Exception e) {
            LOG.warning("Could not connect to server");
            return;
        }
        mMainStage.setScene(mLobbyScene);

    }
}
