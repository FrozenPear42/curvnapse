package com.bugfullabs.curvnapse.snake;

import com.bugfullabs.curvnapse.utils.SerializableColor;
import com.bugfullabs.curvnapse.utils.Vec2;

import java.io.Serializable;

/**
 * Class for line snake fragments with it's own collision detection
 */
public class LineSnakeFragment extends SnakeFragment implements Serializable {
    private Vec2 mBegin;
    private Vec2 mEnd;

    private Vec2 mConstEdge;
    private Vec2 mEdge;
    private double mAngle;

    /**
     * Create new line fragment
     *
     * @param pBegin line begin vector
     * @param pEnd   line end vector
     * @param pAngle line angle
     * @param pColor line color
     * @param pWidth line width
     */
    public LineSnakeFragment(Vec2 pBegin, Vec2 pEnd, double pAngle, SerializableColor pColor, double pWidth) {
        super(Type.LINE, pColor, pWidth);
        mBegin = pBegin;
        mEnd = pEnd;

        mConstEdge = Vec2.directed(pWidth, pAngle + Math.PI / 2);
        mEdge = new Vec2(mBegin, mEnd);
        mAngle = pAngle;
    }

    /**
     * Update head of the fragment
     * @param pEnd new line end
     */
    public void updateHead(Vec2 pEnd) {
        mEnd = pEnd;
        mEdge = new Vec2(mBegin, mEnd);
    }

    /**
     * Check collision at given point
     * @param pPoint point to collide with
     * @return true if collision
     */
    @Override
    public boolean isCollision(Vec2 pPoint) {
        Vec2 origin = Vec2.sub(mBegin, Vec2.times(mConstEdge, 0.5));
        Vec2 point = new Vec2(origin, pPoint);

        double dotA = Vec2.dot(mConstEdge, point);
        double dotB = Vec2.dot(mEdge, point);

        return 0 <= dotA && dotA <= Vec2.dot(mConstEdge, mConstEdge) &&
                0 <= dotB && dotB <= Vec2.dot(mEdge, mEdge);
    }

    public Vec2 getBegin() {
        return mBegin;
    }

    public Vec2 getEnd() {
        return mEnd;
    }

    public double getAngle() {
        return mAngle;
    }
}
