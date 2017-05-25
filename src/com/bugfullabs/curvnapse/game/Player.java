package com.bugfullabs.curvnapse.game;

import com.bugfullabs.curvnapse.utils.SerializableColor;
import javafx.scene.input.KeyCode;

import java.io.Serializable;

/**
 * Class representing player entity
 */
public class Player implements Serializable {
    private static int UID = 0;

    private int mUID;
    private String mName;
    private int mOwner;
    private KeyCode mLeftKey;
    private KeyCode mRightKey;
    private SerializableColor mColor;
    private int mPoints;

    /**
     * Create player with given name, color, and owner client ID
     *
     * @param pName  Player name
     * @param pColor Player color
     * @param pOwner owner client ID
     */
    public Player(String pName, SerializableColor pColor, int pOwner) {
        mUID = UID;
        UID += 1;
        mOwner = pOwner;
        mName = pName;
        mLeftKey = KeyCode.LEFT;
        mRightKey = KeyCode.RIGHT;
        mColor = pColor;
        mPoints = 0;
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

    public SerializableColor getColor() {
        return mColor;
    }

    public int getPoints() {
        return mPoints;
    }

    public void setPoints(int pPoints) {
        mPoints = pPoints;
    }

    public void setName(String pName) {
        mName = pName;
    }
}
