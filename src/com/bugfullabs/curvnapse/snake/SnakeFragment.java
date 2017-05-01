package com.bugfullabs.curvnapse.snake;

import com.bugfullabs.curvnapse.player.PlayerColor;
import com.bugfullabs.curvnapse.utils.Vec2;

import java.io.Serializable;

public abstract class SnakeFragment implements Serializable {
    public enum Type {LINE, HEAD, ARC}

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

    public abstract boolean isCollision(Vec2 pPoint);

    public long getUID() {
        return mUID;
    }

}
