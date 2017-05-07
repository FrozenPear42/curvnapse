package com.bugfullabs.curvnapse.scene;

import com.bugfullabs.curvnapse.FlowManager;
import com.bugfullabs.curvnapse.game.Game;
import com.bugfullabs.curvnapse.gui.Leaderboard;
import com.bugfullabs.curvnapse.gui.LoginBox;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.logging.Logger;

public class GameOverScene {
    private static final Logger LOG = Logger.getLogger(MainLobbyScene.class.getName());
    private Scene mScene;
    private BorderPane mRoot;

    private HBox mTopBox;
    private Label mTitle;

    private VBox mStatsBox;
    private Label mWinnerLabel;
    private Leaderboard mLeaderboard;

    private HBox mButtonsBox;
    private Button mDoneButton;

    public GameOverScene(Game pGame) {
        mRoot = new BorderPane();

        mTopBox = new HBox(10.0f);
        mTopBox.setPadding(new Insets(50.0f, 10.0f, 10.0f, 10.0f));
        mTopBox.setAlignment(Pos.CENTER);

        mTitle = new Label("Game Over");
        mTitle.setStyle("-fx-font-size: 5em; -fx-font-weight: bold");
        mTopBox.getChildren().add(mTitle);

        mStatsBox = new VBox(10.0f);
        mStatsBox.setPadding(new Insets(10.0f));
        mStatsBox.setAlignment(Pos.CENTER);

        mWinnerLabel = new Label("The Winner is: " + pGame.getPlayers().get(0).getName());
        mWinnerLabel.setStyle("-fx-font-size: 3em; -fx-font-weight: bold");

        mLeaderboard = new Leaderboard();
        mLeaderboard.setPlayers(FXCollections.observableArrayList(pGame.getPlayers()));

        mStatsBox.getChildren().addAll(mWinnerLabel, mLeaderboard);

        mButtonsBox = new HBox(10.0f);
        mButtonsBox.setPadding(new Insets(10.0f));
        mButtonsBox.setAlignment(Pos.CENTER);

        mDoneButton = new Button("Done");
        mButtonsBox.getChildren().addAll(mDoneButton);

        mRoot.setTop(mTopBox);
        mRoot.setCenter(mStatsBox);
        mRoot.setBottom(mButtonsBox);

        mScene = new Scene(mRoot);
        mScene.getStylesheets().add("resources/JMetro.css");

        mDoneButton.setOnAction(action -> {

            FlowManager.getInstance().gameLobby(pGame);
        });
    }

    public Scene getScene() {
        return mScene;
    }
}
