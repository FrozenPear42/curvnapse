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

    public String toHex() {
        String hex = "#";
        String res;
        res = Integer.toHexString((int) (mR * 255));
        if (res.length() == 1)
            res = "0" + res;
        hex += res;
        res = Integer.toHexString((int) (mG * 255));
        if (res.length() == 1)
            res = "0" + res;
        hex += res;
        res = Integer.toHexString((int) (mB * 255));
        if (res.length() == 1)
            res = "0" + res;
        hex += res;
        return hex;
    }

    @Override
    public boolean equals(Object pObject) {
        if(!(pObject instanceof PlayerColor))
            return false;
        return mR == ((PlayerColor)pObject).mR && mG == ((PlayerColor)pObject).mG && mB == ((PlayerColor)pObject).mB;

    }
}
