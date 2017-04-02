package com.bugfullabs.curvnapse.snake;

import com.bugfullabs.curvnapse.player.PlayerColor;

import java.io.Serializable;

public class SnakeFragment implements Serializable {
    public enum Type {LINE, ARC}

    private static long UID = 0;

    private Type mType;
    private long mUID;
    private PlayerColor mColor;
    private double mWidth;

    public SnakeFragment(Type pType, PlayerColor pColor, double pWidth) {
        mType = pType;
        mColor = pColor;
        mWidth = pWidth;
        mUID = UID;
        UID += 1;
    }

    public Type getType() {
        return mType;
    }

    public PlayerColor getColor() {
        return mColor;
    }

    public double getWidth() {
        return mWidth;
    }

    public long getUID() {
        return mUID;
    }

}
