package com.bugfullabs.curvnapse.snake;

import com.bugfullabs.curvnapse.player.PlayerColor;
import com.bugfullabs.curvnapse.utils.MathUtils;
import com.bugfullabs.curvnapse.utils.Vec2;

import java.io.Serializable;

public class ArcSnakeFragment extends SnakeFragment implements Serializable {

    private double mStartAngle;
    private double mAngle;
    private double mRadius;
    private Vec2 mCenter;

    public ArcSnakeFragment(double pStartAngle, double pRadius, Vec2 pCenter, PlayerColor pColor, double pWidth) {
        super(Type.ARC, pColor, pWidth);
        mStartAngle = pStartAngle;
        mAngle = 0;
        mRadius = pRadius;
        mCenter = pCenter;
    }

    public void updateHead(double pAngle) {
        mAngle += pAngle;
        //mAngle = MathUtils.normalizeAngle(mAngle);
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
}
