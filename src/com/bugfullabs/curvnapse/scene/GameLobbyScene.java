package com.bugfullabs.curvnapse.scene;

import com.bugfullabs.curvnapse.gui.GameOptionsBox;
import com.bugfullabs.curvnapse.gui.MessageBox;
import com.bugfullabs.curvnapse.gui.PlayersBox;
import com.bugfullabs.curvnapse.network.client.ServerConnector;
import com.bugfullabs.curvnapse.network.message.*;
import com.bugfullabs.curvnapse.player.Player;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.logging.Logger;


public class GameLobbyScene implements ServerConnector.MessageListener {
    private static final Logger LOG = Logger.getLogger(GameLobbyScene.class.getName());
    private BorderPane mRoot;
    private Scene mScene;

    private MessageBox mMessageBox;
    private GameOptionsBox mGameOptionsBox;
    private PlayersBox mPlayersBox;
    private HBox mButtons;
    private Button mBackButton;
    private Button mStartButton;

    private ServerConnector mConnector;
    private Stage mMainStage;
    private ObservableList<Player> mPlayers;

    public GameLobbyScene(Stage pMainSatage, ServerConnector pConnector) {
        mMainStage = pMainSatage;
        mConnector = pConnector;

        mRoot = new BorderPane();
        mScene = new Scene(mRoot);

        mMessageBox = new MessageBox();
        mGameOptionsBox = new GameOptionsBox();
        mPlayersBox = new PlayersBox();
        mButtons = new HBox(5.0f);

        mBackButton = new Button("Back");
        mStartButton = new Button("Start");

        mButtons.setAlignment(Pos.CENTER);
        mButtons.getChildren().addAll(mBackButton, mStartButton);

        mRoot.setLeft(mMessageBox);
        mRoot.setCenter(mGameOptionsBox);
        mRoot.setRight(mPlayersBox);
        mRoot.setBottom(mButtons);
        mRoot.setPadding(new Insets(10.0f));
        mConnector.registerListener(this);

        mMessageBox.setSendListener(pMessage -> mConnector.sendMessage(new TextMessage("MS", pMessage)));

        mPlayers = FXCollections.observableArrayList();
        mPlayersBox.setPlayersList(mPlayers);

        mPlayersBox.setListener(() -> mConnector.sendMessage(new NewPlayerRequestMessage("Player")));

        mStartButton.setOnAction(event -> mConnector.sendMessage(new GameStartRequestMessage()));
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
            case PLAYER_ADD:
                Platform.runLater(() -> mPlayers.add(((NewPlayerMessage) pMessage).getPlayer()));
                break;
            case GAME_START:
                LOG.info("Game started");
                mConnector.unregisterListener(this);
                Platform.runLater(() -> mMainStage.setScene(new GameScene(mConnector, ((GameStartMessage) pMessage).getPlayers()).getScene()));
                break;
            default:
                break;
        }
    }
}