package com.bugfullabs.curvnapse.scene;

import com.bugfullabs.curvnapse.FlowManager;
import com.bugfullabs.curvnapse.game.Game;
import com.bugfullabs.curvnapse.gui.Board;
import com.bugfullabs.curvnapse.gui.Leaderboard;
import com.bugfullabs.curvnapse.gui.MessageBox;
import com.bugfullabs.curvnapse.network.client.ServerConnector;
import com.bugfullabs.curvnapse.network.message.ControlUpdateMessage;
import com.bugfullabs.curvnapse.network.message.Message;
import com.bugfullabs.curvnapse.network.message.SnakeFragmentsMessage;
import com.bugfullabs.curvnapse.network.message.TextMessage;
import com.bugfullabs.curvnapse.player.Player;
import com.bugfullabs.curvnapse.snake.SnakeFragment;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

import java.util.*;
import java.util.logging.Logger;

public class GameScene implements ServerConnector.MessageListener {
    private static final Logger LOG = Logger.getLogger(GameScene.class.getName());
    private BorderPane mRoot;
    private Scene mScene;
    private Board mBoard;
    private MessageBox mMessageBox;
    private Leaderboard mLeaderboard;

    private Game mGame;

    private ObservableList<Player> mPlayers;
    private ObservableList<TextMessage> mMessages;
    private ArrayList<SnakeFragment> mSnakeFragments;

    private Map<KeyCode, Player> mKeys;
    private List<KeyCode> mActiveKeys;

    private ServerConnector mConnector;

    private Timer mTimer;

    public GameScene(Game pGame) {
        mGame = pGame;

        mConnector = FlowManager.getInstance().getConnector();
        mPlayers = FXCollections.observableArrayList(mGame.getPlayers());
        mMessages = FXCollections.observableArrayList();

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


        mSnakeFragments = new ArrayList<>();

        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                mBoard.update(mSnakeFragments);
            }
        }, 0, 1000 / 60);

        mConnector.registerListener(this);
        mMessageBox.setSendListener(pMessage -> mConnector.sendMessage(new TextMessage(FlowManager.getInstance().getUsername(), pMessage)));
        mMessageBox.setMessages(mMessages);

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

    }

    public Scene getScene() {
        return mScene;
    }

    @Override
    public void onClientMessage(Message pMessage) {
        switch (pMessage.getType()) {
            case TEXT:
                Platform.runLater(() -> mMessages.add((TextMessage) pMessage));
                break;
            case SNAKE_UPDATE:
                SnakeFragmentsMessage msg = (SnakeFragmentsMessage) pMessage;
                msg.getSnakeFragments().forEach(fragment -> {
                    mSnakeFragments.removeIf(frag -> frag.getUID() == fragment.getUID());
                    mSnakeFragments.add(fragment);
                });
                break;
        }
    }

}
