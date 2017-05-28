package com.bugfullabs.curvnapse.scene;

import com.bugfullabs.curvnapse.FlowManager;
import com.bugfullabs.curvnapse.game.Game;
import com.bugfullabs.curvnapse.gui.GameOptionsBox;
import com.bugfullabs.curvnapse.gui.MessageBox;
import com.bugfullabs.curvnapse.gui.PlayersBox;
import com.bugfullabs.curvnapse.network.client.ServerConnector;
import com.bugfullabs.curvnapse.network.message.*;
import com.bugfullabs.curvnapse.game.Player;
import com.bugfullabs.curvnapse.network.message.control.LeaveGameRequest;
import com.bugfullabs.curvnapse.network.message.control.TextMessage;
import com.bugfullabs.curvnapse.network.message.lobby.*;
import com.bugfullabs.curvnapse.powerup.PowerUp;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.util.logging.Logger;

/**
 * Represents Lobby scene for given {@link Game}
 */
public class GameLobbyScene implements ServerConnector.MessageListener {
    private static final Logger LOG = Logger.getLogger(GameLobbyScene.class.getName());
    private Scene mScene;

    private GameOptionsBox mGameOptionsBox;
    private Button mStartButton;

    private ServerConnector mConnector;
    private Game mGame;
    private ObservableList<Player> mPlayers;
    private ObservableList<TextMessage> mMessages;


    /**
     * Create new screen for given {@link Game}
     *
     * @param pGame game for screen to be created with
     */
    public GameLobbyScene(Game pGame) {
        mConnector = FlowManager.getInstance().getConnector();
        mGame = pGame;

        BorderPane root = new BorderPane();
        mScene = new Scene(root);
        mScene.getStylesheets().add("resources/JMetro.css");

        HBox topBox = new HBox(10.0f);
        topBox.setPadding(new Insets(10.0f));
        topBox.setAlignment(Pos.CENTER);
        Label title = new Label("Game Lobby");
        title.setStyle("-fx-font-size: 3em; -fx-font-weight: bold");
        topBox.getChildren().add(title);

        MessageBox messageBox = new MessageBox();
        mGameOptionsBox = new GameOptionsBox();
        PlayersBox playersBox = new PlayersBox();
        HBox buttons = new HBox(5.0f);

        Button backButton = new Button("Back");
        mStartButton = new Button("Start");

        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(backButton, mStartButton);

        root.setTop(topBox);
        root.setLeft(messageBox);
        root.setCenter(mGameOptionsBox);
        root.setRight(playersBox);
        root.setBottom(buttons);
        root.setPadding(new Insets(10.0f));

        mConnector.registerListener(this);

        mMessages = FXCollections.observableArrayList();
        messageBox.setMessages(mMessages);
        messageBox.setSendListener(pMessage -> mConnector.sendMessage(new TextMessage(FlowManager.getInstance().getUsername(), pMessage)));

        mPlayers = FXCollections.observableArrayList();
        playersBox.setPlayersList(mPlayers);

        playersBox.setListener(new PlayersBox.PlayerBoxListener() {
            @Override
            public void onCreateLocal() {
                mConnector.sendMessage(new NewPlayerRequest(FlowManager.getInstance().getUsername() + " "));
            }

            @Override
            public void onPlayerEdit(Player pPlayer) {
                mConnector.sendMessage(new PlayerUpdateRequest(pPlayer));
            }

            @Override
            public void onPlayerDelete(Player pPlayer) {
                mConnector.sendMessage(new PlayerDeleteRequest(pPlayer));
            }
        });

        mGameOptionsBox.setListener(new GameOptionsBox.GameOptionsChangeListener() {
            @Override
            public void onGameNameChange(String pName) {
                mConnector.sendMessage(new GameUpdateNameRequest(pName));
            }

            @Override
            public void onPowerUpSelectionChange(PowerUp.PowerType pType, boolean pState) {
                mConnector.sendMessage(new GamePowerUpStateRequest(pType, pState));
            }
        });

        mStartButton.setOnAction(event -> mConnector.sendMessage(new GameStartRequest()));
        backButton.setOnAction(event -> {
            mConnector.sendMessage(new LeaveGameRequest());
            FlowManager.getInstance().mainLobby();
            mConnector.unregisterListener(this);
        });

        update(mGame);
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
     * Update game settings
     *
     * @param pGame new Game object (descriptor)
     */
    private void update(Game pGame) {
        mGame = pGame;
        mPlayers.clear();
        mPlayers.addAll(mGame.getPlayers());
        mGameOptionsBox.setName(mGame.getName());
        mGameOptionsBox.setDisable(mGame.getHostID() != FlowManager.getInstance().getUserID());
        mGameOptionsBox.updateButtons(mGame.getPowerUps());
        mStartButton.setDisable((mGame.getHostID() != FlowManager.getInstance().getUserID()));
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
            case GAME_UPDATE:
                Game game = ((GameUpdateMessage) pMessage).getGame();
                Platform.runLater(() -> update(game));
                break;
            case GAME_START:
                LOG.info("Game started");
                mConnector.unregisterListener(this);
                Platform.runLater(() -> FlowManager.getInstance().gameScene(((GameStartMessage) pMessage).getGame()));
                break;
            default:
                break;
        }
    }
}
