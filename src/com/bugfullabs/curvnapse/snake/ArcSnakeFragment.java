package com.bugfullabs.curvnapse.snake;

import com.bugfullabs.curvnapse.utils.SerializableColor;
import com.bugfullabs.curvnapse.utils.MathUtils;
import com.bugfullabs.curvnapse.utils.Vec2;

import java.io.Serializable;

/**
 * Class for snake arc fragments
 */
public class ArcSnakeFragment extends SnakeFragment implements Serializable {

    private double mStartAngle;
    private double mAngle;
    private double mRadius;

    private double mMinRadius;
    private double mMaxRadius;

    private Vec2 mCenter;

    /**
     * Create new arc fragment with given params
     *
     * @param pStartAngle beginning angle
     * @param pRadius     arc radius
     * @param pCenter     arc center
     * @param pColor      arc color
     * @param pWidth      arc width
     */
    public ArcSnakeFragment(double pStartAngle, double pRadius, Vec2 pCenter, SerializableColor pColor, double pWidth) {
        super(Type.ARC, pColor, pWidth);
        mStartAngle = pStartAngle;
        mAngle = 0;
        mRadius = pRadius;
        mCenter = pCenter;

        mMinRadius = mRadius - 0.5 * pWidth;
        mMaxRadius = mRadius + 0.5 * pWidth;
    }

    /**
     * Update head(extend by given angle)
     *
     * @param pAngle delta angle
     */
    public void updateHead(double pAngle) {
        if (mAngle >= 2 * Math.PI || mAngle <= -2 * Math.PI)
            return;
        mAngle += pAngle;
    }

    /**
     * @return beginning angle
     */
    public double getStartAngle() {
        return mStartAngle;
    }

    /**
     * @return current angle
     */
    public double getAngle() {
        return mAngle;
    }

    /**
     * @return radius
     */
    public double getRadius() {
        return mRadius;
    }

    /**
     * @return arc center
     */
    public Vec2 getCenter() {
        return mCenter;
    }


    /**
     * Check collision at given point
     *
     * @param pPoint point to collide with
     * @return true if collision
     */
    @Override
    public boolean isCollision(Vec2 pPoint) {
        double x = pPoint.x - mCenter.x;
        double y = pPoint.y - mCenter.y;

        double radius = Math.sqrt(x * x + y * y);
        if (mMinRadius <= radius && radius <= mMaxRadius) {

            Vec2 point = new Vec2(x, -y);
            double pointAngle = MathUtils.normalizeAngle(point.angle());

            if (mAngle < 0) { //Turning clockwise
                double angle = pointAngle - mStartAngle;
                return mAngle <= angle && angle <= 0;
            } else if (mAngle > 0) { //Turning counterclockwise
                double angle = pointAngle - mStartAngle;
                return 0 <= angle && angle <= mAngle;
            }
        }
        return false;
    }

    /**
     * does the line make full circle
     *
     * @return true if it does, great! :D
     */
    public boolean isFull() {
        return mAngle >= 2 * Math.PI || mAngle <= -2 * Math.PI;
    }
}
