package com.bugfullabs.curvnapse.player;

import javafx.scene.input.KeyCode;

import java.io.Serializable;

public class Player implements Serializable {
    private static int UID = 0;

    private int mUID;
    private String mName;
    private int mOwner;
    private KeyCode mLeftKey;
    private KeyCode mRightKey;
    private PlayerColor mColor;


    public Player(String pName, PlayerColor pColor, int pOwner) {
        mUID = UID;
        UID += 1;
        mOwner = pOwner;
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

    public int getOwner() {
        return mOwner;
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
