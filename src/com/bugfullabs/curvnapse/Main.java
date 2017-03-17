package com.bugfullabs.curvnapse;

import com.bugfullabs.curvnapse.gui.*;
import com.bugfullabs.curvnapse.network.message.*;
import com.bugfullabs.curvnapse.network.client.ServerConnector;
import com.bugfullabs.curvnapse.network.server.Server;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.logging.Logger;

public class Main extends Application implements LoginBox.LoginListener {
    private static final Logger LOG = Logger.getLogger(Main.class.getName());
    private MessageBox mMessageList;
    private GameList mGameList;
    private Board mBoard;

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
        mMessageList = new MessageBox();
        mGameList = new GameList();
        lobbyRoot.getChildren().addAll(mMessageList, mGameList);
        mLobbyScene = new Scene(lobbyRoot);

        HBox gameLobbyRoot = new HBox();
        mGameLobbyScene = new Scene(gameLobbyRoot);

        FlowPane gameRoot = new FlowPane();
        mBoard = new Board(1000, 800);
        gameRoot.getChildren().add(mBoard);
        mGameScene = new Scene(gameRoot);

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
                    mMessageList.addMessage((TextMessage) pMessage);
                else if (pMessage instanceof HandshakeMessage)
                    mMessageList.addMessage(new TextMessage("Server", ((HandshakeMessage) (pMessage)).getName() + " joined!"));
                else if (pMessage instanceof GameUpdateMessage) {
                    GameUpdateMessage msg = (GameUpdateMessage) pMessage;
                    mGameList.updateGame(msg.getGame());
                } else if (pMessage.getType() == Message.Type.GAME_JOIN) {
                    Platform.runLater(() -> mMainStage.setScene(mGameLobbyScene));
                }
            });
            mMessageList.setSendListener(pMessage -> connector.sendMessage(new TextMessage(pName, pMessage)));
            mGameList.setListener(pGame -> connector.sendMessage(new JoinRequestMessage(pGame.getID())));
           // connector.sendMessage(new GameCreateRequestMessage("Wichrowski ciota", "", 8));

        } catch (Exception e) {
            LOG.warning("Could not connect to server");
            return;
        }
        mMainStage.setScene(mLobbyScene);
    }

}
