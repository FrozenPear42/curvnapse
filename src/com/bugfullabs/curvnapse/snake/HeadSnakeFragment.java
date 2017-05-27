package com.bugfullabs.curvnapse.snake;

import com.bugfullabs.curvnapse.utils.SerializableColor;
import com.bugfullabs.curvnapse.utils.Vec2;

/**
 * Class representing non-colliding, point snake fragment
 */
public class HeadSnakeFragment extends SnakeFragment {

    private Vec2 mPosition;

    /**
     * create new head fragment at given point
     *
     * @param pColor    snake color
     * @param pWidth    head width
     * @param pPosition position of the head
     */
    public HeadSnakeFragment(SerializableColor pColor, double pWidth, Vec2 pPosition) {
        super(Type.HEAD, pColor, pWidth);
        mPosition = pPosition;
    }

    public Vec2 getPosition() {
        return mPosition;
    }

    /**
     * Check if collision with given point
     *
     * @param pPoint point to collide with
     * @return always false
     */
    @Override
    public boolean isCollision(Vec2 pPoint) {
        return false;
    }
}
