package com.bugfullabs.curvnapse.gui;


import com.bugfullabs.curvnapse.player.Player;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Leaderboard extends VBox {
    private ListView<Player> mPlayersList;

    public Leaderboard() {
        super(10.0f);
        setPadding(new Insets(10.f));
        setAlignment(Pos.CENTER);

        mPlayersList = new ListView<>();
        mPlayersList.setCellFactory(param -> new PlayersLeaderboardListElement());
        getChildren().addAll(mPlayersList);
        setMaxWidth(200);
    }

    public void setPlayers(ObservableList<Player> pPlayers) {
        mPlayersList.setItems(pPlayers);
    }

    private class PlayersLeaderboardListElement extends ListCell<Player> {
        @Override
        public void updateItem(Player pPlayer, boolean pEmpty) {
            super.updateItem(pPlayer, pEmpty);
            HBox box = new HBox(10.0f);
            if (pPlayer != null) {
                Label points = new Label("100");
                points.setStyle("-fx-background-color: "+ pPlayer.getColor().toHex() +"; -fx-font-weight: bolder");
                Label label = new Label(pPlayer.getName());
                label.setTextFill(pPlayer.getColor().toFXColor());
                box.getChildren().addAll(points, label);
            }
            setGraphic(box);
        }
    }
}
