package com.bugfullabs.curvnapse.snake;

import com.bugfullabs.curvnapse.utils.Vec2;
import com.sun.javafx.geom.Vec2d;
import com.sun.javafx.geom.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class Snake {
    public static final double DEFAULT_SPEED = 10.0f;
    public static final double DEFAULT_SIZE = 5.0f;
    public static final double DEFAULT_TURN_RADIUS = 10.0f;

    private enum State {
        TURNING_LEFT,
        TURNING_RIGHT,
        FORWARD
    }

    private int mUID;
    private double mSize;
    private double mAngle;
    private Vec2 mVelocity;
    private Vec2 mPosition;
    private Vec2 mTurnCenter;
    private double mTurnRadius;

    private List<ArcSnakeFragment> mArcFragments;
    private List<LineSnakeFragment> mLineFragments;

    private State mState;

    public Snake(int pUID, Vec2 pPosition, double pAngle) {
        mUID = pUID;
        mPosition = pPosition;
        mAngle = pAngle;

        mArcFragments = new ArrayList<>();
        mLineFragments = new ArrayList<>();

        mVelocity = Vec2.directed(DEFAULT_SPEED, pAngle);

        mTurnCenter = new Vec2();
        mTurnRadius = DEFAULT_TURN_RADIUS;

        mSize = DEFAULT_SIZE;

        mState = State.FORWARD;
        doLine();
    }

    private void step(double pDelta) {
        switch (mState) {
            case FORWARD:
                mPosition = Vec2.add(mPosition, Vec2.times(mVelocity, pDelta));
                mLineFragments.get(mLineFragments.size() - 1).updateHead(mPosition);
                break;
            case TURNING_LEFT:
                mAngle += (mVelocity.length() / mTurnRadius) * pDelta;
                mPosition.x = mTurnCenter.x - mTurnRadius * Math.sin(mAngle);
                mPosition.y = mTurnCenter.y + mTurnRadius * Math.cos(mAngle);
                mArcFragments.get(mLineFragments.size() - 1).updateHead(mAngle);
                break;
            case TURNING_RIGHT:
                mPosition.x = mTurnCenter.x + mTurnRadius * Math.sin(mAngle);
                mPosition.y = mTurnCenter.y - mTurnRadius * Math.cos(mAngle);
                mArcFragments.get(mLineFragments.size() - 1).updateHead(mAngle);

                break;
        }
    }

    private void doLine() {
        LineSnakeFragment line = new LineSnakeFragment(mPosition, mPosition, mSize);
        mLineFragments.add(line);
    }

    private void doArc() {

    }

}
