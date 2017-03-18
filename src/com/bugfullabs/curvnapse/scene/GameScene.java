package com.bugfullabs.curvnapse.scene;

import com.bugfullabs.curvnapse.gui.Board;
import com.bugfullabs.curvnapse.gui.Leaderboard;
import com.bugfullabs.curvnapse.gui.MessageList;
import com.bugfullabs.curvnapse.network.client.ServerConnector;
import com.bugfullabs.curvnapse.network.message.Message;
import com.bugfullabs.curvnapse.network.message.TextMessage;
import com.bugfullabs.curvnapse.network.server.Server;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

import java.util.logging.Logger;

public class GameScene implements ServerConnector.MessageListener {
    private static final Logger LOG = Logger.getLogger(GameScene.class.getName());
    private BorderPane mRoot;
    private Scene mScene;
    private Board mBoard;
    private MessageList mMessageList;
    private Leaderboard mLeaderboard;

    private ServerConnector mConnector;

    public GameScene(ServerConnector pConnector) {
        mConnector = pConnector;

        mRoot = new BorderPane();
        mMessageList = new MessageList();
        mBoard = new Board(300, 300);
        mLeaderboard = new Leaderboard();
        mRoot.setLeft(mMessageList);
        mRoot.setCenter(mBoard);
        mRoot.setRight(mLeaderboard);
        mScene = new Scene(mRoot);

        mConnector.registerListener(this);
    }

    public Scene getScene() {
        return mScene;
    }

    @Override
    public void onClientMessage(Message pMessage) {
        switch (pMessage.getType()) {
            case TEXT:
                mMessageList.addMessage((TextMessage) pMessage);
                break;
        }
    }
}
