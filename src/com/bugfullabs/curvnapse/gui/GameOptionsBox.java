package com.bugfullabs.curvnapse.gui;

import com.bugfullabs.curvnapse.powerup.PowerUp;
import com.bugfullabs.curvnapse.utils.ResourceManager;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class GameOptionsBox extends VBox {
    private HBox mHeader;
    private TextField mName;
    private PowerUpGrid mGrid;
    private Button mSave;
    private GameOptionsChangeListener mListener;


    public GameOptionsBox() {
        super(10.0f);
        setAlignment(Pos.CENTER);

        mHeader = new HBox(10.0f);
        mHeader.setAlignment(Pos.CENTER);
        mName = new TextField("Test");
        mName.setPrefColumnCount(40);
        mName.setMaxWidth(200.0f);
        mSave = new Button("Save");
        mSave.setOnAction(event -> {
            if (mListener != null)
                mListener.onGameNameChange(mName.getText());
        });
        mHeader.getChildren().addAll(mName, mSave);

        mGrid = new PowerUpGrid();

        getChildren().addAll(mHeader, mGrid);
    }

    public void setListener(GameOptionsChangeListener pListener) {
        mListener = pListener;
    }

    public void setName(String pName) {
        mName.setText(pName);
    }

    public void updateButtons(boolean[] pPowerUps) {
        mGrid.updateButtons(pPowerUps);
    }

    class PowerUpGrid extends GridPane {
        private ToggleImageButton[] mButtons;

        public PowerUpGrid() {
            setAlignment(Pos.CENTER);
            mButtons = new ToggleImageButton[PowerUp.PowerType.values().length];

            for (PowerUp.PowerType type : PowerUp.PowerType.values()) {
                int i = type.ordinal();
                mButtons[i] = new ToggleImageButton(ResourceManager.getInstance().getPowerUpImage(type));
                mButtons[i].setOnToggleListener(isActive -> mListener.onPowerUpSelectionChange(type, isActive));
                add(mButtons[i], i % 4, i / 4);
            }

        }

        public void updateButtons(boolean[] pPowerUps) {
            for (int i = 0; i < pPowerUps.length; i++)
                mButtons[i].setState(pPowerUps[i]);
        }
    }

    public interface GameOptionsChangeListener {
        void onGameNameChange(String pName);

        void onPowerUpSelectionChange(PowerUp.PowerType pType, boolean pState);
    }

}
