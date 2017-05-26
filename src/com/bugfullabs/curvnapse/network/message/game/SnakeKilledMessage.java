package com.bugfullabs.curvnapse.network.message.game;

import com.bugfullabs.curvnapse.network.message.Message;
import com.bugfullabs.curvnapse.utils.SerializableColor;
import com.bugfullabs.curvnapse.utils.Vec2;

/**
 * One of the snakes just died poorly...
 * Wondering which?
 */
public class SnakeKilledMessage extends Message {

    private Vec2 mPoint;
    private SerializableColor mKillerColor;

    public SnakeKilledMessage(Vec2 pPoint, SerializableColor pKillerColor) {
        super(Type.SNAKE_KILLED);
        mPoint = pPoint;
        mKillerColor = pKillerColor;
    }

    public Vec2 getPoint() {
        return mPoint;
    }

    public SerializableColor getKillerColor() {
        return mKillerColor;
    }
}
