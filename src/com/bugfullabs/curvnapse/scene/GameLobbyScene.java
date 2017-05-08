package com.bugfullabs.curvnapse.scene;

import com.bugfullabs.curvnapse.FlowManager;
import com.bugfullabs.curvnapse.game.Game;
import com.bugfullabs.curvnapse.gui.GameOptionsBox;
import com.bugfullabs.curvnapse.gui.MessageBox;
import com.bugfullabs.curvnapse.gui.PlayersBox;
import com.bugfullabs.curvnapse.network.client.ServerConnector;
import com.bugfullabs.curvnapse.network.message.*;
import com.bugfullabs.curvnapse.player.Player;
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

import javax.xml.ws.soap.MTOM;
import java.util.logging.Logger;


public class GameLobbyScene implements ServerConnector.MessageListener {
    private static final Logger LOG = Logger.getLogger(GameLobbyScene.class.getName());
    private BorderPane mRoot;
    private Scene mScene;

    private HBox mTopBox;
    private Label mTitle;

    private MessageBox mMessageBox;
    private GameOptionsBox mGameOptionsBox;
    private PlayersBox mPlayersBox;
    private HBox mButtons;
    private Button mBackButton;
    private Button mStartButton;

    private ServerConnector mConnector;
    private Game mGame;
    private ObservableList<Player> mPlayers;
    private ObservableList<TextMessage> mMessages;


    public GameLobbyScene(Game pGame) {
        mConnector = FlowManager.getInstance().getConnector();
        mGame = pGame;

        mRoot = new BorderPane();
        mScene = new Scene(mRoot);
        mScene.getStylesheets().add("resources/JMetro.css");

        mTopBox = new HBox(10.0f);
        mTopBox.setPadding(new Insets(10.0f));
        mTopBox.setAlignment(Pos.CENTER);
        mTitle = new Label("Game Lobby");
        mTitle.setStyle("-fx-font-size: 3em; -fx-font-weight: bold");
        mTopBox.getChildren().add(mTitle);

        mMessageBox = new MessageBox();
        mGameOptionsBox = new GameOptionsBox();
        mPlayersBox = new PlayersBox();
        mButtons = new HBox(5.0f);

        mBackButton = new Button("Back");
        mStartButton = new Button("Start");

        mButtons.setAlignment(Pos.CENTER);
        mButtons.getChildren().addAll(mBackButton, mStartButton);

        mRoot.setTop(mTopBox);
        mRoot.setLeft(mMessageBox);
        mRoot.setCenter(mGameOptionsBox);
        mRoot.setRight(mPlayersBox);
        mRoot.setBottom(mButtons);
        mRoot.setPadding(new Insets(10.0f));

        mConnector.registerListener(this);

        mMessages = FXCollections.observableArrayList();
        mMessageBox.setMessages(mMessages);
        mMessageBox.setSendListener(pMessage -> mConnector.sendMessage(new TextMessage(FlowManager.getInstance().getUsername(), pMessage)));

        mPlayers = FXCollections.observableArrayList();
        mPlayersBox.setPlayersList(mPlayers);

        mPlayersBox.setListener(new PlayersBox.PlayerBoxListener() {
            @Override
            public void onCreateLocal() {
                mConnector.sendMessage(new NewPlayerRequest(FlowManager.getInstance().getUsername() + " - "));
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
                mConnector.sendMessage(new GamePowerUpUpdateRequest(pType, pState));
            }
        });

        mStartButton.setOnAction(event -> mConnector.sendMessage(new GameStartRequest()));
        mBackButton.setOnAction(event -> {
            mConnector.sendMessage(new LeaveGameRequest());
            FlowManager.getInstance().mainLobby();
            mConnector.unregisterListener(this);
        });

        update(mGame);
    }

    public Scene getScene() {
        return mScene;
    }

    private void update(Game pGame) {
        mGame = pGame;
        mPlayers.clear();
        mPlayers.addAll(mGame.getPlayers());
        mGameOptionsBox.setName(mGame.getName());
        mGameOptionsBox.setDisable(mGame.getHostID() != FlowManager.getInstance().getUserID());
        mGameOptionsBox.updateButtons(mGame.getPowerUps());
    }

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
