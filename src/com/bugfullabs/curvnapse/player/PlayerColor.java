package com.bugfullabs.curvnapse.player;

import javafx.scene.paint.Color;

import java.io.Serializable;

public class PlayerColor implements Serializable {
    private double mR;
    private double mG;
    private double mB;

    public PlayerColor(double pR, double pG, double pB) {
        mR = pR;
        mG = pG;
        mB = pB;
    }

    public PlayerColor(Color pColor) {
        mR = pColor.getRed();
        mG = pColor.getGreen();
        mB = pColor.getBlue();
    }

    public Color toFXColor() {
        return new Color(mR, mG, mB, 1.0f);
    }
}
