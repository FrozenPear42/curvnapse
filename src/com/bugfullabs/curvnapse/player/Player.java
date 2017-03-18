package com.bugfullabs.curvnapse.player;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.io.Serializable;

public class Player implements Serializable {
    private static int UID = 0;

    private int mUID;
    private String mName;
    private boolean mIsLocal;
    private KeyCode mLeftKey;
    private KeyCode mRightKey;
    private PlayerColor mColor;


    public Player(String pName, PlayerColor pColor, boolean pIsLocal) {
        mUID = UID;
        UID += 1;
        mIsLocal = pIsLocal;
        mName = pName;
        mLeftKey = KeyCode.LEFT;
        mRightKey = KeyCode.RIGHT;
        mColor = pColor;
    }

    public int getID() {
        return mUID;
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

    public PlayerColor getColor() {
        return mColor;
    }

}
