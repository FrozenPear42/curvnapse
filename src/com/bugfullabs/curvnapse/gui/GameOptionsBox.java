package com.bugfullabs.curvnapse.gui;

import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;


public class GameOptionsBox extends FlowPane {
    private Label mHeader;
    private PowerUpGrid mGrid;
    private PlayersBox mPlayersBox;

    public GameOptionsBox() {
        super();

    }


    class PowerUpGrid extends GridPane {
        private ToggleImageButton mPowerups[];

        public PowerUpGrid() {

        }
    }
    
}
