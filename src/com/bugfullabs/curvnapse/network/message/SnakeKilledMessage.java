package com.bugfullabs.curvnapse.network.message;

import com.bugfullabs.curvnapse.utils.Vec2;

public class SnakeKilledMessage extends Message {

    private Vec2 mPoint;
    private boolean mSelfKill;

    public SnakeKilledMessage(Vec2 pPoint, boolean pSelfKill) {
        super(Type.SNAKE_KILLED);
        mPoint = pPoint;
        mSelfKill = pSelfKill;
    }

    public Vec2 getPoint() {
        return mPoint;
    }

    public boolean isSelfKill() {
        return mSelfKill;
    }
}
