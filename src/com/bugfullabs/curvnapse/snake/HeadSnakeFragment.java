package com.bugfullabs.curvnapse.snake;

import com.bugfullabs.curvnapse.player.PlayerColor;
import com.bugfullabs.curvnapse.utils.Vec2;

public class HeadSnakeFragment extends SnakeFragment {

    private Vec2 mPosition;

    public HeadSnakeFragment(PlayerColor pColor, double pWidth, Vec2 pPosition) {
        super(Type.HEAD, pColor, pWidth);
        mPosition = pPosition;
    }

    public Vec2 getPosition() {
        return mPosition;
    }

    public void setPosition(Vec2 pPosition) {
        mPosition = pPosition;
    }

    @Override
    public boolean isCollision(Vec2 pPoint) {
        return false;
    }
}
