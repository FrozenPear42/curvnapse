package com.bugfullabs.curvnapse.scene;

import com.bugfullabs.curvnapse.Main;
import com.bugfullabs.curvnapse.gui.GameListBox;
import com.bugfullabs.curvnapse.gui.MessageBox;
import com.bugfullabs.curvnapse.network.client.Game;
import com.bugfullabs.curvnapse.network.client.ServerConnector;
import com.bugfullabs.curvnapse.network.message.*;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.logging.Logger;

public class MainLobbyScene implements ServerConnector.MessageListener {
    private static final Logger LOG = Logger.getLogger(MainLobbyScene.class.getName());
    private BorderPane mRoot;
    private Scene mScene;

    private MessageBox mMessageBox;
    private GameListBox mGameListBox;

    private Stage mMainStage;
    private ServerConnector mConnector;

    public MainLobbyScene(Stage pMainSatage, ServerConnector pConnector) {
        mRoot = new BorderPane();
        mMessageBox = new MessageBox();
        mGameListBox = new GameListBox();
        mRoot.setLeft(mMessageBox);
        mRoot.setCenter(mGameListBox);
        mScene = new Scene(mRoot);

        mMainStage = pMainSatage;
        mConnector = pConnector;
        mConnector.registerListener(this);

        mMessageBox.setSendListener(pMessage -> mConnector.sendMessage(new TextMessage("asd", pMessage)));
        mGameListBox.setListener(new GameListBox.GameListBoxListener() {
            @Override
            public void onJoin(Game pGame) {
                mConnector.sendMessage(new JoinRequestMessage(pGame.getID()));
            }

            @Override
            public void onCreate() {
                mConnector.sendMessage(new GameCreateRequestMessage("Wichrowski ciota", "", 10));
            }
        });


    }

    public Scene getScene() {
        return mScene;
    }

    @Override
    public void onClientMessage(Message pMessage) {
        if (pMessage.getType() == Message.Type.TEXT)
            mMessageBox.addMessage((TextMessage) pMessage);
        else if (pMessage.getType() == Message.Type.HANDSHAKE)
            mMessageBox.addMessage(new TextMessage("Server", ((HandshakeMessage) (pMessage)).getName() + " joined!"));
        else if (pMessage.getType() == Message.Type.GAME_UPDATE) {
            GameUpdateMessage msg = (GameUpdateMessage) pMessage;
            mGameListBox.updateGame(msg.getGame());
        } else if (pMessage.getType() == Message.Type.GAME_JOIN) {
            Platform.runLater(() -> {
                mMainStage.setScene(new GameLobbyScene(mMainStage, mConnector).getScene());
                mConnector.unregisterListener(this);
            });

        }
    }
}
