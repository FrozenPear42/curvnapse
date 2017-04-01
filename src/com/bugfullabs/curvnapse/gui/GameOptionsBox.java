package com.bugfullabs.curvnapse.gui;

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
        getChildren().addAll(mHeader);
    }

    public void setListener(GameOptionsChangeListener pListener) {
        mListener = pListener;
    }

    public void setName(String pName) {
        mName.setText(pName);
    }

    class PowerUpGrid extends GridPane {
        private ToggleImageButton mPowerups[];

        public PowerUpGrid() {
        }
    }

    public interface GameOptionsChangeListener {
        void onGameNameChange(String pName);

        void onPowerUpSelectionChange(boolean[] pPowerUps);
    }

}
