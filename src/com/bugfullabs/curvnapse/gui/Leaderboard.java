package com.bugfullabs.curvnapse.gui;


import com.bugfullabs.curvnapse.player.Player;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class Leaderboard extends VBox {
    private ListView<Player> mPlayersList;

    public Leaderboard() {
        super(10.0f);
        setPadding(new Insets(10.f));
        mPlayersList = new ListView<>();
        mPlayersList.setCellFactory(param -> new PlayersLeaderboardListElement());
        getChildren().addAll(mPlayersList);
    }

    private class PlayersLeaderboardListElement extends ListCell<Player> {
        @Override
        public void updateItem(Player pPlayer, boolean pEmpty) {
            super.updateItem(pPlayer, pEmpty);
            VBox box = new VBox(5.0f);
            if (pPlayer != null) {
                Label label = new Label(pPlayer.getName());
                Label points = new Label("100");
                Rectangle color = new Rectangle(20, 20, pPlayer.getColor().toFXColor());
                box.getChildren().addAll(label, points, color);
                setEditable(pPlayer.isLocal());
            }
            setGraphic(box);
        }
    }
}
