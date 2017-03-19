package com.bugfullabs.curvnapse.scene;

import com.bugfullabs.curvnapse.gui.Board;
import com.bugfullabs.curvnapse.gui.Leaderboard;
import com.bugfullabs.curvnapse.gui.MessageBox;
import com.bugfullabs.curvnapse.gui.MessageList;
import com.bugfullabs.curvnapse.network.client.ServerConnector;
import com.bugfullabs.curvnapse.network.message.Message;
import com.bugfullabs.curvnapse.network.message.TextMessage;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

import java.util.logging.Logger;

public class GameScene implements ServerConnector.MessageListener {
    private static final Logger LOG = Logger.getLogger(GameScene.class.getName());
    private BorderPane mRoot;
    private Scene mScene;
    private Board mBoard;
    private MessageBox mMessageBox;
    private Leaderboard mLeaderboard;

    private ServerConnector mConnector;

    public GameScene(ServerConnector pConnector) {
        mConnector = pConnector;

        mRoot = new BorderPane();
        mMessageBox = new MessageBox();
        mBoard = new Board(400, 500);
        mLeaderboard = new Leaderboard();
        mRoot.setLeft(mMessageBox);
        mRoot.setCenter(mBoard);
        mRoot.setRight(mLeaderboard);
        mScene = new Scene(mRoot);

        mConnector.registerListener(this);
        mMessageBox.setSendListener(pMessage -> mConnector.sendMessage(new TextMessage("MWSG", pMessage)));

    }

    public Scene getScene() {
        return mScene;
    }

    @Override
    public void onClientMessage(Message pMessage) {
        switch (pMessage.getType()) {
            case TEXT:
                mMessageBox.addMessage((TextMessage) pMessage);
                break;
        }
    }

}
