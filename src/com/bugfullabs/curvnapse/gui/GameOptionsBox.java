package com.bugfullabs.curvnapse.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;


public class GameOptionsBox extends VBox {
    private TextField mHeader;
    private PowerUpGrid mGrid;
    private PlayersBox mPlayersBox;

    public GameOptionsBox() {
        super(10.0f);
        setAlignment(Pos.CENTER);

        mHeader = new TextField("Test");
        mHeader.setPrefColumnCount(40);
        mHeader.setMaxWidth(200.0f);

        getChildren().addAll(mHeader);
    }


    class PowerUpGrid extends GridPane {
        private ToggleImageButton mPowerups[];

        public PowerUpGrid() {

        }
    }

}
