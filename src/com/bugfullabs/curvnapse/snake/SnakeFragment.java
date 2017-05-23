package com.bugfullabs.curvnapse.snake;

import com.bugfullabs.curvnapse.utils.SerializableColor;
import com.bugfullabs.curvnapse.utils.Vec2;

import java.io.Serializable;

/**
 * Abstract class for snakefragment of any type
 */
public abstract class SnakeFragment implements Serializable {
    /**
     * Snake fragment types
     */
    public enum Type {
        LINE, HEAD, ARC
    }

    private static long UID = 0;

    private Type mType;
    private long mUID;
    private SerializableColor mColor;
    private double mWidth;

    /**
     * Create new snake fragment
     *
     * @param pType  type
     * @param pColor snake color
     * @param pWidth snake wwidth
     */
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

    /**
     * Check collision at given point
     *
     * @param pPoint point to collide with
     * @return true if collision
     */
    public abstract boolean isCollision(Vec2 pPoint);

}
