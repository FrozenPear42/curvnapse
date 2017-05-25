package com.bugfullabs.curvnapse.scene;

import com.bugfullabs.curvnapse.FlowManager;
import com.bugfullabs.curvnapse.game.Game;
import com.bugfullabs.curvnapse.gui.Board;
import com.bugfullabs.curvnapse.gui.Leaderboard;
import com.bugfullabs.curvnapse.gui.MessageBox;
import com.bugfullabs.curvnapse.network.client.ServerConnector;
import com.bugfullabs.curvnapse.network.message.*;
import com.bugfullabs.curvnapse.game.Player;
import com.bugfullabs.curvnapse.network.message.control.TextMessage;
import com.bugfullabs.curvnapse.network.message.game.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Represents game screen in application
 */
public class GameScene implements ServerConnector.MessageListener {
    private static final Logger LOG = Logger.getLogger(GameScene.class.getName());
    private Scene mScene;
    private Board mBoard;

    private Label mRoundLabel;

    private Game mGame;

    private ObservableList<Player> mPlayers;
    private ObservableList<TextMessage> mMessages;

    private Map<KeyCode, Player> mKeys;
    private List<KeyCode> mActiveKeys;

    private ServerConnector mConnector;

    /**
     * Create new screen for given game
     *
     * @param pGame game
     */
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

        BorderPane root = new BorderPane();
        MessageBox messageBox = new MessageBox();
        mBoard = new Board(mGame.getBoardWidth(), mGame.getBoardHeight());
        Leaderboard leaderboard = new Leaderboard();
        root.setLeft(messageBox);
        root.setCenter(mBoard);
        root.setRight(leaderboard);
        mScene = new Scene(root);
        mScene.getStylesheets().add("resources/JMetro.css");

        mConnector.registerListener(this);
        messageBox.setSendListener(pMessage -> mConnector.sendMessage(new TextMessage(FlowManager.getInstance().getUsername(), pMessage)));
        messageBox.setMessages(mMessages);

        leaderboard.setPlayers(mPlayers);

        VBox gameInfoBox = new VBox();
        gameInfoBox.setAlignment(Pos.CENTER);
        gameInfoBox.setPadding(new Insets(10.0f));

        Label gameNameLabel = new Label(mGame.getName());
        gameNameLabel.setStyle("-fx-font-size: 3em; -fx-font-weight: bold");
        mRoundLabel = new Label(String.format("Round %d/%d", 1, mGame.getRounds()));
        mRoundLabel.setStyle("-fx-font-size: 2em");

        gameInfoBox.getChildren().add(gameNameLabel);
        gameInfoBox.getChildren().add(mRoundLabel);
        root.setTop(gameInfoBox);

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

    /**
     * Returns JFX scene for this screen
     *
     * @return JFX {@link Scene}
     */
    public Scene getScene() {
        return mScene;
    }

    /**
     * Listens on server messages
     *
     * @param pMessage received message
     */
    @Override
    public void onClientMessage(Message pMessage) {
        switch (pMessage.getType()) {
            case TEXT:
                Platform.runLater(() -> mMessages.add((TextMessage) pMessage));
                break;
            case SNAKE_UPDATE:
                Platform.runLater(() -> mBoard.update(((SnakeFragmentsMessage) pMessage).getSnakeFragments()));
                break;
            case BOARD_ERASE:
                mBoard.erase();
                break;

            case SNAKE_KILLED:
                SnakeKilledMessage msg = ((SnakeKilledMessage) pMessage);
                Platform.runLater(() -> mBoard.drawCollision(msg.getPoint(), msg.getKillerColor()));
                break;

            case GAME_UPDATE:
                Platform.runLater(() -> {
                    mGame = ((GameUpdateMessage) pMessage).getGame();
                    mPlayers.clear();
                    mPlayers.addAll(mGame.getPlayers());
                });
                break;

            case ROUND_UPDATE:
                Platform.runLater(() -> {
                    mBoard.clear();
                    mRoundLabel.setText((String.format("Round %d/%d", ((NextRoundMessage) pMessage).getRoundNumber(), mGame.getRounds())));
                });
                break;

            case GAMEOVER:
                Platform.runLater(() -> {
                    mConnector.unregisterListener(this);
                    FlowManager.getInstance().gameOverScene(mGame);
                });
                break;

            case SPAWN_POWERUP:
                Platform.runLater(() -> mBoard.updatePowerUps(((UpdatePowerUpMessage) pMessage).getPowerUp()));
                break;
        }
    }

}
