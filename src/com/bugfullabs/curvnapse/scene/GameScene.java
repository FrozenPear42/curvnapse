package com.bugfullabs.curvnapse.scene;

import com.bugfullabs.curvnapse.gui.Board;
import com.bugfullabs.curvnapse.gui.Leaderboard;
import com.bugfullabs.curvnapse.gui.MessageBox;
import com.bugfullabs.curvnapse.network.client.ServerConnector;
import com.bugfullabs.curvnapse.network.message.ControlUpdateMessage;
import com.bugfullabs.curvnapse.network.message.Message;
import com.bugfullabs.curvnapse.network.message.TextMessage;
import com.bugfullabs.curvnapse.player.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class GameScene implements ServerConnector.MessageListener {
    private static final Logger LOG = Logger.getLogger(GameScene.class.getName());
    private BorderPane mRoot;
    private Scene mScene;
    private Board mBoard;
    private MessageBox mMessageBox;
    private Leaderboard mLeaderboard;

    private ObservableList<Player> mPlayers;
    private Map<KeyCode, Player> mKeys;
    private List<KeyCode> mActiveKeys;

    private ServerConnector mConnector;

    public GameScene(ServerConnector pConnector, List<Player> pPlayers) {
        mConnector = pConnector;
        mPlayers = FXCollections.observableArrayList(pPlayers);

        mKeys = new HashMap<>();
        mActiveKeys = new ArrayList<>();

        mPlayers.forEach(pPlayer -> {
            mKeys.put(pPlayer.getLeftKey(), pPlayer);
            mKeys.put(pPlayer.getRightKey(), pPlayer);
        });

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

        mLeaderboard.setPlayers(mPlayers);

        mRoot.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            if (mActiveKeys.contains(code)) {
                event.consume();
                return;
            }
            mActiveKeys.add(code);
            if (mKeys.containsKey(code)) {
                event.consume();
                Player p = mKeys.get(code);
                if (p.getLeftKey() == code)
                    mConnector.sendMessage(new ControlUpdateMessage(p.getID(), ControlUpdateMessage.Direction.LEFT, ControlUpdateMessage.Action.DOWN));
                else
                    mConnector.sendMessage(new ControlUpdateMessage(p.getID(), ControlUpdateMessage.Direction.RIGHT, ControlUpdateMessage.Action.DOWN));
            }
        });

        mRoot.setOnKeyReleased(event -> {
            KeyCode code = event.getCode();
            mActiveKeys.remove(code);
            if (mKeys.containsKey(code)) {
                event.consume();
                Player p = mKeys.get(code);
                if (p.getLeftKey() == code)
                    mConnector.sendMessage(new ControlUpdateMessage(p.getID(), ControlUpdateMessage.Direction.LEFT, ControlUpdateMessage.Action.UP));
                else
                    mConnector.sendMessage(new ControlUpdateMessage(p.getID(), ControlUpdateMessage.Direction.RIGHT, ControlUpdateMessage.Action.UP));
            }
        });

        mBoard.update();
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
