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

/**
 * Represents Game Over scene for given {@link Game}
 */
public class GameOverScene {
    private static final Logger LOG = Logger.getLogger(MainLobbyScene.class.getName());
    private Scene mScene;

    /**
     * Create new GameOver screen for given Game
     *
     * @param pGame game
     */
    public GameOverScene(Game pGame) {
        BorderPane root = new BorderPane();

        HBox topBox = new HBox(10.0f);
        topBox.setPadding(new Insets(50.0f, 10.0f, 10.0f, 10.0f));
        topBox.setAlignment(Pos.CENTER);

        Label title = new Label("Game Over");
        title.setStyle("-fx-font-size: 5em; -fx-font-weight: bold");
        topBox.getChildren().add(title);

        VBox statsBox = new VBox(10.0f);
        statsBox.setPadding(new Insets(10.0f));
        statsBox.setAlignment(Pos.CENTER);

        Label winnerLabel = new Label("The Winner is: " + pGame.getPlayers().get(0).getName());
        winnerLabel.setStyle("-fx-font-size: 3em; -fx-font-weight: bold");

        Leaderboard leaderboard = new Leaderboard();
        leaderboard.setPlayers(FXCollections.observableArrayList(pGame.getPlayers()));

        statsBox.getChildren().addAll(winnerLabel, leaderboard);

        HBox buttonsBox = new HBox(10.0f);
        buttonsBox.setPadding(new Insets(10.0f));
        buttonsBox.setAlignment(Pos.CENTER);

        Button doneButton = new Button("Done");
        buttonsBox.getChildren().addAll(doneButton);

        root.setTop(topBox);
        root.setCenter(statsBox);
        root.setBottom(buttonsBox);

        mScene = new Scene(root);
        mScene.getStylesheets().add("resources/JMetro.css");

        doneButton.setOnAction(action -> {

            FlowManager.getInstance().gameLobby(pGame);
        });
    }

    /**
     * ReturnsJFX scene for this screen
     *
     * @return JFX {@link Scene}
     */
    public Scene getScene() {
        return mScene;
    }
}
