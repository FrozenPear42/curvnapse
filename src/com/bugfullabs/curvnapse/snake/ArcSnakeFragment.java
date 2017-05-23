package com.bugfullabs.curvnapse.snake;

import com.bugfullabs.curvnapse.utils.SerializableColor;
import com.bugfullabs.curvnapse.utils.MathUtils;
import com.bugfullabs.curvnapse.utils.Vec2;

import java.io.Serializable;

public class ArcSnakeFragment extends SnakeFragment implements Serializable {

    private double mStartAngle;
    private double mAngle;
    private double mRadius;

    private double mMinRadius;
    private double mMaxRadius;

    private Vec2 mCenter;

    public ArcSnakeFragment(double pStartAngle, double pRadius, Vec2 pCenter, SerializableColor pColor, double pWidth) {
        super(Type.ARC, pColor, pWidth);
        mStartAngle = pStartAngle;
        mAngle = 0;
        mRadius = pRadius;
        mCenter = pCenter;

        mMinRadius = mRadius - 0.5 * pWidth;
        mMaxRadius = mRadius + 0.5 * pWidth;
    }

    public void updateHead(double pAngle) {
        if (mAngle >= 2 * Math.PI || mAngle <= -2 * Math.PI)
            return;
        mAngle += pAngle;
    }

    public double getStartAngle() {
        return mStartAngle;
    }

    public double getAngle() {
        return mAngle;
    }

    public double getRadius() {
        return mRadius;
    }

    public Vec2 getCenter() {
        return mCenter;
    }

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

    public boolean isFull() {
        return mAngle >= 2 * Math.PI || mAngle <= -2 * Math.PI;
    }
}
