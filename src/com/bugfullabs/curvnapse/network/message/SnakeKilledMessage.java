package com.bugfullabs.curvnapse.network.message;

import com.bugfullabs.curvnapse.player.PlayerColor;
import com.bugfullabs.curvnapse.utils.Vec2;

public class SnakeKilledMessage extends Message {

    private Vec2 mPoint;
    private PlayerColor mKillerColor;

    public SnakeKilledMessage(Vec2 pPoint, PlayerColor pKillerColor) {
        super(Type.SNAKE_KILLED);
        mPoint = pPoint;
        mKillerColor = pKillerColor;
    }

    public Vec2 getPoint() {
        return mPoint;
    }

    public PlayerColor getKillerColor() {
        return mKillerColor;
    }
}
