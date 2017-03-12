package com.bugfullabs.curvnapse.gui;

import com.bugfullabs.curvnapse.network.client.Game;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

public class GameList extends ListView<Game> {
    private ObservableList<Game> mGames;

    public GameList() {
        super();
        mGames = FXCollections.observableArrayList();
        setCellFactory(param -> new GameListElement());
        setItems(mGames);
    }

    public void addGame(Game pGame) {
        Platform.runLater(() -> mGames.add(pGame));
    }

    public void removeGame(Game pGame) {
        Platform.runLater(() -> mGames.remove(pGame));
    }

    class GameListElement extends ListCell<Game> {
        @Override
        public void updateItem(Game pGame, boolean pEmpty) {
            super.updateItem(pGame, pEmpty);
            if (pGame != null) {
                HBox box = new HBox(5.0f);
                Label name = new Label("Ala ma algebre");
                box.getChildren().addAll(name);
                setGraphic(box);
            }
        }
    }
}
