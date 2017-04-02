package com.bugfullabs.curvnapse.scene;

import com.bugfullabs.curvnapse.FlowManager;
import com.bugfullabs.curvnapse.game.Game;
import com.bugfullabs.curvnapse.gui.Board;
import com.bugfullabs.curvnapse.gui.Leaderboard;
import com.bugfullabs.curvnapse.gui.MessageBox;
import com.bugfullabs.curvnapse.network.client.ServerConnector;
import com.bugfullabs.curvnapse.network.message.*;
import com.bugfullabs.curvnapse.player.Player;
import com.bugfullabs.curvnapse.snake.SnakeFragment;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
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

    private Game mGame;

    private ObservableList<Player> mPlayers;
    private ObservableList<TextMessage> mMessages;
    private ArrayList<SnakeFragment> mSnakeFragments;

    private Map<KeyCode, Player> mKeys;
    private List<KeyCode> mActiveKeys;

    private ServerConnector mConnector;

    public GameScene(Game pGame) {
        mGame = pGame;

        mConnector = FlowManager.getInstance().getConnector();
        mPlayers = FXCollections.observableArrayList(mGame.getPlayers());
        mMessages = FXCollections.observableArrayList();

        mKeys = new HashMap<>();
        mActiveKeys = new ArrayList<>();

        mPlayers.stream()
                .filter(player -> player.getOwner() == FlowManager.getInstance().getUserID())
                .forEach(pPlayer -> {
                    mKeys.put(pPlayer.getLeftKey(), pPlayer);
                    mKeys.put(pPlayer.getRightKey(), pPlayer);
                });

        mRoot = new BorderPane();
        mMessageBox = new MessageBox();
        mBoard = new Board(mGame.getBoardWidth(), mGame.getBoardHeight());
        mLeaderboard = new Leaderboard();
        mRoot.setLeft(mMessageBox);
        mRoot.setCenter(mBoard);
        mRoot.setRight(mLeaderboard);
        mScene = new Scene(mRoot);
        mScene.getStylesheets().add("resources/JMetro.css");

        mSnakeFragments = new ArrayList<>();

        mConnector.registerListener(this);
        mMessageBox.setSendListener(pMessage -> mConnector.sendMessage(new TextMessage(FlowManager.getInstance().getUsername(), pMessage)));
        mMessageBox.setMessages(mMessages);

        mLeaderboard.setPlayers(mPlayers);

        mScene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            if (mActiveKeys.contains(code)) {
                event.consume();
                return;
            }
            mActiveKeys.add(code);
            if (mKeys.containsKey(code)) {
                event.consume();
                Player p = mKeys.get(code);
                if (p.getLeftKey() == code) {
                    mActiveKeys.remove(p.getRightKey());
                    mConnector.sendMessage(new ControlUpdateMessage(p.getID(), ControlUpdateMessage.Direction.LEFT, ControlUpdateMessage.Action.DOWN));
                } else {
                    mActiveKeys.remove(p.getLeftKey());
                    mConnector.sendMessage(new ControlUpdateMessage(p.getID(), ControlUpdateMessage.Direction.RIGHT, ControlUpdateMessage.Action.DOWN));
                }
            }
        });

        mScene.setOnKeyReleased(event -> {
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
                mBoard.update(((SnakeFragmentsMessage) pMessage).getSnakeFragments());
                //TODO: convert to be more MVC
//                msg.getSnakeFragments().forEach(fragment -> {
//                    mSnakeFragments.removeIf(frag -> frag.getUID() == fragment.getUID());
//                    mSnakeFragments.add(fragment);
//                    mBoard.update(mSnakeFragments);
//                });
//
                break;
            case BOARD_ERASE:
                mBoard.erase();
                break;

            case SPAWN_POWERUP:
                mBoard.updatePowerUps(((UpdatePowerUpMessage) pMessage).getPowerUp());
                break;
        }
    }

}
