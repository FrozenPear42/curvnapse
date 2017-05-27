package com.bugfullabs.curvnapse.gui;

import com.bugfullabs.curvnapse.game.Game;
import com.bugfullabs.curvnapse.powerup.PowerUp;
import com.bugfullabs.curvnapse.utils.ResourceManager;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Control class for list of currently available games
 */
public class GameListBox extends VBox {

    private ListView<Game> mGameList;
    private Button mJoinButton;
    private GameListBoxListener mListener;

    /**
     * Create new {@link GameListBox}
     */
    public GameListBox() {
        super(10.0f);
        setPadding(new Insets(10.0f));
        setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Games");
        title.setStyle("-fx-font-size: 2em; -fx-font-weight: bold");

        mGameList = new ListView<>();
        mGameList.setCellFactory(pGameListView -> new GameListElement());

        HBox buttons = new HBox(5.0f);
        mJoinButton = new Button("Join");
        Button createButton = new Button("Create new");

        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(mJoinButton, createButton);

        getChildren().addAll(title, mGameList, buttons);

        mJoinButton.setOnAction(event -> {
            if (mListener != null)
                mListener.onJoin(mGameList.getSelectionModel().getSelectedItem());
        });

        createButton.setOnAction(event -> {
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

    /**
     * Set {@link Game} list source
     * @param pGames games to be displayed
     */
    public void setGames(ObservableList<Game> pGames) {
        mGameList.setItems(pGames);
    }

    /**
     * Set listener
     * @param pListener listener
     */
    public void setListener(GameListBoxListener pListener) {
        mListener = pListener;
    }

    /**
     * Listener invoked on game selection events
     */
    public interface GameListBoxListener {
        /**
         * invoked on game selected
         * @param pGame selected game
         */
        void onJoin(Game pGame);

        /**
         * invoked on game created
         */
        void onCreate();
    }

    /**
     * Game list cell
     */
    private class GameListElement extends ListCell<Game> {
        GameListElement() {
            getStyleClass().add("game");
        }

        @Override
        public void updateItem(Game pGame, boolean pEmpty) {
            super.updateItem(pGame, pEmpty);
            HBox box = new HBox(5.0f);
            if (pGame != null) {
                box.setAlignment(Pos.CENTER_LEFT);
                Label name = new Label(pGame.getName());
                name.setMinWidth(200);
                name.setMaxWidth(200);
                Label players = new Label(pGame.getPlayers().size() + "/" + pGame.getMaxPlayers());
                players.setMinWidth(40);
                players.setAlignment(Pos.CENTER_LEFT);
                box.getChildren().addAll(players, name);

                for (PowerUp.PowerType type : PowerUp.PowerType.values()) {
                    if (pGame.getPowerUps()[type.ordinal()]) {
                        ImageView v = new ImageView(ResourceManager.getInstance().getPowerUpImage(type));
                        v.setPreserveRatio(true);
                        v.setFitWidth(24);
                        v.setFitHeight(24);
                        box.getChildren().add(v);
                    }
                }
            }
            setGraphic(box);
        }
    }

}
