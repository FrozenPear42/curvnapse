package com.bugfullabs.curvnapse.scene;

import com.bugfullabs.curvnapse.FlowManager;
import com.bugfullabs.curvnapse.gui.GameListBox;
import com.bugfullabs.curvnapse.gui.MessageBox;
import com.bugfullabs.curvnapse.network.client.Game;
import com.bugfullabs.curvnapse.network.client.ServerConnector;
import com.bugfullabs.curvnapse.network.message.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

import java.util.logging.Logger;

public class MainLobbyScene implements ServerConnector.MessageListener {
    private static final Logger LOG = Logger.getLogger(MainLobbyScene.class.getName());
    private BorderPane mRoot;
    private Scene mScene;

    private MessageBox mMessageBox;
    private GameListBox mGameListBox;

    private ServerConnector mConnector;

    private ObservableList<TextMessage> mMessages;
    private ObservableList<Game> mGames;

    public MainLobbyScene() {
        mRoot = new BorderPane();
        mMessageBox = new MessageBox();
        mGameListBox = new GameListBox();
        mRoot.setLeft(mMessageBox);
        mRoot.setCenter(mGameListBox);
        mScene = new Scene(mRoot);

        mConnector = FlowManager.getInstance().getConnector();
        mConnector.sendMessage(new UpdateRequest());
        mConnector.registerListener(this);

        mMessages = FXCollections.observableArrayList();
        mMessageBox.setMessages(mMessages);
        mMessageBox.setSendListener(pMessage -> mConnector.sendMessage(new TextMessage(FlowManager.getInstance().getUsername(), pMessage)));

        mGames = FXCollections.observableArrayList();
        mGameListBox.setGames(mGames);
        mGameListBox.setListener(new GameListBox.GameListBoxListener() {
            @Override
            public void onJoin(Game pGame) {
                mConnector.sendMessage(new JoinRequest(pGame.getID()));
            }

            @Override
            public void onCreate() {
                mConnector.sendMessage(new GameCreateRequest("Wichrowski ciota", "", 10));
            }
        });
    }

    public Scene getScene() {
        return mScene;
    }

    @Override
    public void onClientMessage(Message pMessage) {
        if (pMessage.getType() == Message.Type.TEXT)
            Platform.runLater(() -> mMessages.add((TextMessage) pMessage));
        else if (pMessage.getType() == Message.Type.HANDSHAKE)
            Platform.runLater(() -> mMessages.add(new TextMessage("Server", ((HandshakeMessage) (pMessage)).getName() + " joined!")));
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
