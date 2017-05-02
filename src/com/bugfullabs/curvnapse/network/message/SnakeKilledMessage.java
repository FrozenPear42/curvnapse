package com.bugfullabs.curvnapse.network.message;

import com.bugfullabs.curvnapse.utils.Vec2;

public class SnakeKilledMessage extends Message {

    private Vec2 mPoint;

    public SnakeKilledMessage(Vec2 pPoint) {
        super(Type.SNAKE_KILLED);
        mPoint = pPoint;
    }

    public Vec2 getPoint() {
        return mPoint;
    }
}
