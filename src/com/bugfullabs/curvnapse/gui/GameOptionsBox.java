package com.bugfullabs.curvnapse.gui;

import com.bugfullabs.curvnapse.powerup.PowerUp;
import com.bugfullabs.curvnapse.utils.ResourceManager;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Control class for game options box (all the settings)
 */
public class GameOptionsBox extends VBox {
    private TextField mName;
    private PowerUpGrid mGrid;
    private GameOptionsChangeListener mListener;

    /**
     * Create new {@link GameOptionsBox}
     */
    public GameOptionsBox() {
        super(10.0f);
        setAlignment(Pos.CENTER);

        HBox header = new HBox(10.0f);
        header.setAlignment(Pos.CENTER);
        mName = new TextField("Test");
        mName.setPrefColumnCount(40);
        mName.setMaxWidth(200.0f);
        Button save = new Button("Save");
        save.setOnAction(event -> {
            if (mListener != null)
                mListener.onGameNameChange(mName.getText());
        });
        header.getChildren().addAll(mName, save);

        mGrid = new PowerUpGrid();

        getChildren().addAll(header, mGrid);
    }

    /**
     * set listener on change events
     *
     * @param pListener lestener
     */
    public void setListener(GameOptionsChangeListener pListener) {
        mListener = pListener;
    }

    /**
     * Set displayed game name
     *
     * @param pName game name
     */
    public void setName(String pName) {
        mName.setText(pName);
    }

    /**
     * update buttons in PowerUp grid
     *
     * @param pPowerUps list of boolean matching new buttons state
     */
    public void updateButtons(boolean[] pPowerUps) {
        mGrid.updateButtons(pPowerUps);
    }

    /**
     * Listener on game options changes
     */
    public interface GameOptionsChangeListener {
        /**
         * Invoked when game name is changed
         *
         * @param pName new game name
         */
        void onGameNameChange(String pName);

        /**
         * invoked on button in {@link PowerUpGrid} toggled
         *
         * @param pType  selected button
         * @param pState nutton state
         */
        void onPowerUpSelectionChange(PowerUp.PowerType pType, boolean pState);
    }

    /**
     * Grid of PowerUp selection buttons
     */
    class PowerUpGrid extends GridPane {
        private ToggleImageButton[] mButtons;

        /**
         * Create new Grid
         */
        PowerUpGrid() {
            setAlignment(Pos.CENTER);
            mButtons = new ToggleImageButton[PowerUp.PowerType.values().length];

            for (PowerUp.PowerType type : PowerUp.PowerType.values()) {
                int i = type.ordinal();
                mButtons[i] = new ToggleImageButton(ResourceManager.getInstance().getPowerUpImage(type));
                mButtons[i].setOnToggleListener(isActive -> mListener.onPowerUpSelectionChange(type, isActive));
                add(mButtons[i], i % 4, i / 4);
            }
        }

        /**
         * Update buttons
         *
         * @param pPowerUps list of boolean matching new buttons state
         */
        void updateButtons(boolean[] pPowerUps) {
            for (int i = 0; i < pPowerUps.length; i++)
                mButtons[i].setState(pPowerUps[i]);
        }
    }
}
