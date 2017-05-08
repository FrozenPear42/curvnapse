package com.bugfullabs.curvnapse.gui;

import com.bugfullabs.curvnapse.FlowManager;
import com.bugfullabs.curvnapse.player.Player;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PlayersBox extends VBox {
    private Label mTitle;
    private Button mAddPlayerButton;
    private ListView<Player> mPlayersList;
    private PlayerBoxListener mListener;

    public PlayersBox() {
        super(10.0f);
        setPadding(new Insets(10.0f));
        setAlignment(Pos.TOP_CENTER);

        mTitle = new Label("Players");
        mTitle.setStyle("-fx-font-size: 2em; -fx-font-weight: bold");

        mAddPlayerButton = new Button("Add Local");
        mPlayersList = new ListView<>();
        mPlayersList.setCellFactory(param -> new PlayersListElement());
        getChildren().addAll(mTitle, mPlayersList, mAddPlayerButton);

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

        void onPlayerEdit(Player pPlayer);

        void onPlayerDelete(Player pPlayer);
    }

    private class PlayersListElement extends ListCell<Player> {

        @Override
        public void updateItem(Player pPlayer, boolean pEmpty) {
            super.updateItem(pPlayer, pEmpty);
            VBox box = new VBox(10.0f);
            box.setAlignment(Pos.CENTER);
            if (pPlayer != null) {

                Label label = new Label(pPlayer.getName());
                label.setTextFill(pPlayer.getColor().toFXColor());
                label.setStyle("-fx-text-fill: " + pPlayer.getColor().toHex());

                HBox buttons = new HBox(10.0f);
                buttons.setAlignment(Pos.CENTER);

                if (pPlayer.getOwner() == FlowManager.getInstance().getUserID()) {
                    Button delete = new Button("x");
                    delete.getStyleClass().add("delete");
                    delete.setMaxWidth(10);
                    delete.setMaxHeight(10);

                    Button left = new Button(pPlayer.getLeftKey().getName());
                    Button right = new Button(pPlayer.getRightKey().getName());
                    buttons.getChildren().addAll(delete, left, right);

                    left.setOnAction(e -> {
                        left.setText("---");
                        right.setDisable(true);
                        setOnKeyPressed(keyEvent -> {
                            right.setDisable(false);
                            left.setText(pPlayer.getLeftKey().getName());
                            setOnKeyPressed(null);
                            pPlayer.setLeftKey(keyEvent.getCode());
                            mListener.onPlayerEdit(pPlayer);
                        });
                    });

                    right.setOnAction(e -> {
                        right.setText("---");
                        left.setDisable(true);
                        setOnKeyPressed(keyEvent -> {
                            left.setDisable(false);
                            right.setText(pPlayer.getRightKey().getName());
                            setOnKeyPressed(null);
                            pPlayer.setRightKey(keyEvent.getCode());
                            mListener.onPlayerEdit(pPlayer);
                        });
                    });

                    delete.setOnAction(e -> mListener.onPlayerDelete(pPlayer));

                    setEditable(true);
                } else {
                    Label left = new Label(pPlayer.getLeftKey().getName());
                    Label right = new Label(pPlayer.getRightKey().getName());
                    buttons.getChildren().addAll(left, right);
                    setEditable(false);
                }

                box.getChildren().addAll(label, buttons);
            }
            setGraphic(box);
        }

    }
}
