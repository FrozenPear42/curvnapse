package com.bugfullabs.curvnapse.gui;

import com.bugfullabs.curvnapse.player.Player;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Observer;


public class PlayersBox extends VBox {
    private Button mAddPlayerButton;
    private ListView<Player> mPlayersList;

    public PlayersBox() {
        super(10.0f);
        setPadding(new Insets(10.0f));
        setAlignment(Pos.CENTER);
        mAddPlayerButton = new Button("Add Local");
        mPlayersList = new ListView<>();
        mPlayersList.setCellFactory(param -> new PlayersListElement());
        getChildren().addAll(mPlayersList, mAddPlayerButton);
    }

    public void setPlayersList(ObservableList<Player> pList) {
        mPlayersList.setItems(pList);
    }

    private class PlayersListElement extends ListCell<Player> {
        @Override
        public void updateItem(Player pPlayer, boolean pEmpty) {
            super.updateItem(pPlayer, pEmpty);
            if (pPlayer != null) {
                VBox box = new VBox(5.0f);
                Label label = new Label(pPlayer.getName());
                Label keys = new Label(String.format("Left: %s Right: %s",
                        pPlayer.getLeftKey().getName(),
                        pPlayer.getRightKey().getName()));
                Rectangle color = new Rectangle(10, 10, pPlayer.getColor());
                box.getChildren().addAll(label, keys, color);
                setGraphic(box);
            }
        }
    }

    private class PlayerItem extends AnchorPane {
    }
}
