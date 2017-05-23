package com.bugfullabs.curvnapse.scene;

import com.bugfullabs.curvnapse.FlowManager;
import com.bugfullabs.curvnapse.game.Game;
import com.bugfullabs.curvnapse.gui.GameListBox;
import com.bugfullabs.curvnapse.gui.MessageBox;
import com.bugfullabs.curvnapse.network.client.ServerConnector;
import com.bugfullabs.curvnapse.network.message.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.util.Collections;
import java.util.logging.Logger;

/**
 * Represents MainLobby screen in application
 */
public class MainLobbyScene implements ServerConnector.MessageListener {
    private static final Logger LOG = Logger.getLogger(MainLobbyScene.class.getName());
    private Scene mScene;

    private ServerConnector mConnector;

    private ObservableList<TextMessage> mMessages;
    private ObservableList<Game> mGames;

    /**
     * Create new MainLobby screen
     */
    public MainLobbyScene() {

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10.0f));

        HBox topBox = new HBox(10.0f);
        topBox.setPadding(new Insets(10.0f));
        topBox.setAlignment(Pos.CENTER);
        Label title = new Label("Lobby");
        title.setStyle("-fx-font-size: 3em; -fx-font-weight: bold");
        topBox.getChildren().add(title);

        MessageBox messageBox = new MessageBox();
        GameListBox gameListBox = new GameListBox();

        root.setTop(topBox);
        root.setLeft(messageBox);
        root.setCenter(gameListBox);

        mScene = new Scene(root);
        mScene.getStylesheets().add("resources/JMetro.css");

        mConnector = FlowManager.getInstance().getConnector();
        mConnector.sendMessage(new UpdateRequest());
        mConnector.registerListener(this);

        mMessages = FXCollections.observableArrayList();
        messageBox.setMessages(mMessages);
        messageBox.setSendListener(pMessage -> mConnector.sendMessage(new TextMessage(FlowManager.getInstance().getUsername(), pMessage)));

        mGames = FXCollections.observableArrayList();
        gameListBox.setGames(mGames);
        gameListBox.setListener(new GameListBox.GameListBoxListener() {
            @Override
            public void onJoin(Game pGame) {
                mConnector.sendMessage(new JoinRequest(pGame.getID()));
            }

            @Override
            public void onCreate() {
                mConnector.sendMessage(new GameCreateRequest(FlowManager.getInstance().getUserID(), "Great Game", "", 10));
            }
        });
    }

    /**
     * Returns JFX scene
     *
     * @return JFX {@link Scene} for this screen
     */
    public Scene getScene() {
        return mScene;
    }

    /**
     * Listener on server messages
     *
     * @param pMessage received message
     */
    @Override
    public void onClientMessage(Message pMessage) {
        if (pMessage.getType() == Message.Type.TEXT)
            Platform.runLater(() -> mMessages.add((TextMessage) pMessage));
        else if (pMessage.getType() == Message.Type.GAME_UPDATE) {
            GameUpdateMessage msg = (GameUpdateMessage) pMessage;
            Platform.runLater(() -> {
                mGames.removeIf(pGame -> msg.getGame().getID() == pGame.getID());
                mGames.add(msg.getGame());
            });
        } else if (pMessage.getType() == Message.Type.GAME_JOIN) {
            Platform.runLater(() -> {
                FlowManager.getInstance().gameLobby(((JoinMessage) pMessage).getGame());
                mConnector.unregisterListener(this);
            });

        }
    }
}
