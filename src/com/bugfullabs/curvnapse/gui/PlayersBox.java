package com.bugfullabs.curvnapse.gui;

import com.bugfullabs.curvnapse.player.Player;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class PlayersBox extends VBox {
    private Button mAddPlayerButton;
    private ListView<Player> mPlayersList;
    private PlayerBoxListener mListener;

    public PlayersBox() {
        super(10.0f);
        setPadding(new Insets(10.0f));
        setAlignment(Pos.CENTER);
        mAddPlayerButton = new Button("Add Local");
        mPlayersList = new ListView<>();
        mPlayersList.setCellFactory(param -> new PlayersListElement());
        getChildren().addAll(mPlayersList, mAddPlayerButton);

        mAddPlayerButton.setOnAction(event -> {
            if (mListener != null)
                mListener.onCreateLocal();
        });

    }

    public void setPlayersList(ObservableList<Player> pList) {
        mPlayersList.setItems(pList);
    }

    public void setListener(PlayerBoxListener pListener) {
        mListener = pListener;
    }

    public interface PlayerBoxListener {
        void onCreateLocal();
    }

    private class PlayersListElement extends ListCell<Player> {
        @Override
        public void updateItem(Player pPlayer, boolean pEmpty) {
            super.updateItem(pPlayer, pEmpty);
            VBox box = new VBox(5.0f);
            if (pPlayer != null) {
                Label label = new Label(pPlayer.getName());
                label.setTextFill(pPlayer.getColor().toFXColor());
                Label keys = new Label(String.format("Left: %s\nRight: %s",
                        pPlayer.getLeftKey().getName(),
                        pPlayer.getRightKey().getName()));
                Rectangle color = new Rectangle(20, 20, pPlayer.getColor().toFXColor());
                box.getChildren().addAll(label, keys, color);
                setEditable(pPlayer.isLocal());
            }
            setGraphic(box);
        }
    }
}
