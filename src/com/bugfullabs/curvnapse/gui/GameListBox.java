package com.bugfullabs.curvnapse.gui;

import com.bugfullabs.curvnapse.network.client.Game;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GameListBox extends VBox {
    private GameList mGameList;
    private HBox mButtons;
    private Button mJoinButton;
    private Button mCreateButton;

    private GameListBoxListener mListener;

    public GameListBox() {
        super(5.0f);
        setPadding(new Insets(10.0f));
        mGameList = new GameList();
        mButtons = new HBox(5.0f);
        mJoinButton = new Button("Join");
        mCreateButton = new Button("Create new");

        mButtons.setAlignment(Pos.CENTER);
        mButtons.getChildren().addAll(mJoinButton, mCreateButton);
        getChildren().addAll(mGameList, mButtons);

        mJoinButton.setOnAction(event -> {
            if (mListener != null)
                mListener.onJoin(mGameList.getSelectionModel().getSelectedItem());
        });

        mCreateButton.setOnAction(event -> {
            if (mListener != null)
                mListener.onCreate();
        });

        mJoinButton.setDisable(true);
        mGameList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null)
                mJoinButton.setDisable(true);
            else
                mJoinButton.setDisable(false);
        });

    }

    public void setListener(GameListBoxListener pListener) {
        mListener = pListener;
    }

    public void updateGame(Game pGame) {
        mGameList.updateGame(pGame);
    }

    public interface GameListBoxListener {
        void onJoin(Game pGame);

        void onCreate();
    }

}
