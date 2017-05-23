package com.bugfullabs.curvnapse.snake;

import com.bugfullabs.curvnapse.utils.SerializableColor;
import com.bugfullabs.curvnapse.utils.Vec2;

import java.io.Serializable;

public abstract class SnakeFragment implements Serializable {
    public enum Type {LINE, HEAD, ARC}

    private static long UID = 0;

    private Type mType;
    private long mUID;
    private SerializableColor mColor;
    private double mWidth;

    public SnakeFragment(Type pType, SerializableColor pColor, double pWidth) {
        mType = pType;
        mColor = pColor;
        mWidth = pWidth;
        mUID = UID;
        UID += 1;
    }

    public Type getType() {
        return mType;
    }

    public SerializableColor getColor() {
        return mColor;
    }

    public double getWidth() {
        return mWidth;
    }

    public long getUID() {
        return mUID;
    }

    public abstract boolean isCollision(Vec2 pPoint);

}
