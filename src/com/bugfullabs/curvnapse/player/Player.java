package com.bugfullabs.curvnapse.player;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Player {
    private String mName;

    private boolean mIsLocal;
    private KeyCode mLeftKey;
    private KeyCode mRightKey;
    private Color mColor;


    public Player(String pName, Color pColor, boolean pIsLocal) {
        mIsLocal = pIsLocal;
        mName = pName;
        mLeftKey = KeyCode.LEFT;
        mRightKey = KeyCode.RIGHT;
        mColor = pColor;
    }

    public String getName() {
        return mName;
    }

    public boolean isLocal() {
        return mIsLocal;
    }

    public KeyCode getLeftKey() {
        return mLeftKey;
    }

    public void setLeftKey(KeyCode pLeftKey) {
        this.mLeftKey = pLeftKey;
    }

    public KeyCode getRightKey() {
        return mRightKey;
    }

    public void setRightKey(KeyCode pRightKey) {
        this.mRightKey = pRightKey;
    }

    public Color getColor() {
        return mColor;
    }
}
