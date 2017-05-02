package com.bugfullabs.curvnapse.snake;

import com.bugfullabs.curvnapse.player.PlayerColor;
import com.bugfullabs.curvnapse.powerup.PowerUp;
import com.bugfullabs.curvnapse.utils.MathUtils;
import com.bugfullabs.curvnapse.utils.Vec2;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Snake {

    private enum State {
        TURNING_LEFT,
        TURNING_RIGHT,
        FORWARD,
        STOP
    }

    private static final double DEFAULT_SPEED = 0.08f;
    private static final double DEFAULT_SIZE = 2.5f;
    private static final double DEFAULT_TURN_RADIUS = 20.0f;
    private static final int HOLE_TIME = 3000;

    private int mUID;
    private double mSize;
    private double mAngle;
    private Vec2 mVelocity;
    private Vec2 mPosition;
    private Vec2 mTurnCenter;
    private double mTurnRadius;
    private double mSpeed;

    private boolean mDead;
    private boolean mInvisible;

    private PlayerColor mColor;

    private State mState;

    private List<ArcSnakeFragment> mArcFragments;
    private List<LineSnakeFragment> mLineFragments;

    private List<Pair<PowerUp, Double>> mPowerUps;

    private Random mRandom;
    private boolean mHoleNow;
    private int mHoleTime;

    private boolean mConfused;

    public Snake(int pUID, Vec2 pPosition, double pAngle, PlayerColor pColor) {
        mColor = pColor;
        mUID = pUID;
        mPosition = pPosition;
        mAngle = pAngle;

        mArcFragments = new ArrayList<>();
        mLineFragments = new ArrayList<>();
        mPowerUps = new ArrayList<>();
        mRandom = new Random();
        mHoleTime = mRandom.nextInt(5000) + 5000;

        mSpeed = DEFAULT_SPEED;
        mVelocity = Vec2.directed(DEFAULT_SPEED, pAngle);
        mTurnCenter = new Vec2();
        mTurnRadius = DEFAULT_TURN_RADIUS;
        mSize = DEFAULT_SIZE;
        mState = State.FORWARD;
        mDead = false;
        mInvisible = false;
        mHoleNow = false;
        mConfused = false;

        applyChange();
    }

    public void step(double pDelta) {
        mPowerUps.replaceAll(pair -> new Pair<>(pair.getKey(), pair.getValue() - pDelta));
        mPowerUps.stream().filter(pair -> pair.getValue() <= 0).forEach(pair -> pair.getKey().onEnd(this));
        mPowerUps.removeIf(pair -> pair.getValue() <= 0);

        mHoleTime -= pDelta;
        if (mHoleTime <= 0) {
            if (!mHoleNow) {
                setInvisible(true);
                mHoleNow = true;
                mHoleTime = mRandom.nextInt(1000) + 100;
            } else {
                setInvisible(false);
                mHoleNow = false;
                mHoleTime = mRandom.nextInt(3000) + 3000;
            }
        }

        double deltaAngle = (mVelocity.length() / mTurnRadius) * pDelta;
        switch (mState) {
            case FORWARD:
                mPosition = Vec2.add(mPosition, Vec2.times(mVelocity, pDelta));
                if (!mInvisible)
                    mLineFragments.get(mLineFragments.size() - 1).updateHead(mPosition);
                break;

            case TURNING_LEFT:
                mAngle += deltaAngle;
                mAngle = MathUtils.normalizeAngle(mAngle);
                mPosition.x = mTurnCenter.x + mTurnRadius * Math.sin(mAngle);
                mPosition.y = mTurnCenter.y + mTurnRadius * Math.cos(mAngle);
                if (!mInvisible)
                    mArcFragments.get(mArcFragments.size() - 1).updateHead(deltaAngle);
                break;

            case TURNING_RIGHT:
                mAngle -= (mVelocity.length() / mTurnRadius) * pDelta;
                mAngle = MathUtils.normalizeAngle(mAngle);
                mPosition.x = mTurnCenter.x - mTurnRadius * Math.sin(mAngle);
                mPosition.y = mTurnCenter.y - mTurnRadius * Math.cos(mAngle);
                if (!mInvisible)
                    mArcFragments.get(mArcFragments.size() - 1).updateHead(-deltaAngle);
                break;
        }
    }


    public void addPowerUp(PowerUp pPowerUp) {
        mPowerUps.add(0, new Pair<>(pPowerUp, pPowerUp.getDuration()));
        pPowerUp.onBegin(this);
    }

    public void turnLeft() {
        if (mDead) return;
        if (!mConfused) {
            doArc(true);
            mState = State.TURNING_LEFT;
        } else {
            doArc(false);
            mState = State.TURNING_RIGHT;
        }
    }

    public void turnRight() {
        if (mDead) return;
        if (mConfused) {
            doArc(true);
            mState = State.TURNING_LEFT;
        } else {
            doArc(false);
            mState = State.TURNING_RIGHT;
        }
    }

    public void turnEnd() {
        if (mDead) return;
        doLine();
        mState = State.FORWARD;
    }

    private void doLine() {
        mVelocity = Vec2.directed(mSpeed, mAngle);
        mVelocity.y = -mVelocity.y;

        LineSnakeFragment line = new LineSnakeFragment(mPosition, mPosition, mAngle, mColor, mSize);
        mLineFragments.add(line);
    }

    private void doArc(boolean pLeft) {
        if (pLeft) {
            double startAngle = MathUtils.normalizeAngle(mAngle - Math.PI / 2);
            mTurnCenter.x = mPosition.x - mTurnRadius * Math.sin(mAngle);
            mTurnCenter.y = mPosition.y - mTurnRadius * Math.cos(mAngle);
            ArcSnakeFragment arc = new ArcSnakeFragment(startAngle, mTurnRadius, mTurnCenter, mColor, mSize);
            mArcFragments.add(arc);
        } else {
            double startAngle = MathUtils.normalizeAngle(mAngle + Math.PI / 2);
            mTurnCenter.x = mPosition.x + mTurnRadius * Math.sin(mAngle);
            mTurnCenter.y = mPosition.y + mTurnRadius * Math.cos(mAngle);

            ArcSnakeFragment arc = new ArcSnakeFragment(startAngle, mTurnRadius, mTurnCenter, mColor, mSize);
            mArcFragments.add(arc);
        }
    }

    private void applyChange() {
        switch (mState) {
            case STOP:
                break;
            case FORWARD:
                doLine();
                break;
            case TURNING_LEFT:
                doArc(true);
                break;
            case TURNING_RIGHT:
                doArc(false);
                break;
        }
    }

    public void teleport(Vec2 pDestination) {
        mPosition = pDestination;
        applyChange();
    }


    public void erase() {
        mArcFragments.clear();
        mLineFragments.clear();
        applyChange();
    }

    public Vec2 getPosition() {
        return mPosition;
    }

    public double getSize() {
        return mSize;
    }

    public double getTurnRadius() {
        return mTurnRadius;
    }

    public double getSpeed() {
        return mSpeed;
    }

    public boolean isConfused() {
        return mConfused;
    }


    public boolean isDead() {
        return mDead;
    }

    public SnakeFragment getLastFragment() {
        if (mInvisible)
            return new HeadSnakeFragment(mColor, mSize, mPosition);

        if (mState == State.FORWARD)
            return mLineFragments.get(mLineFragments.size() - 1);
        else if (mState == State.TURNING_RIGHT || mState == State.TURNING_LEFT)
            return mArcFragments.get(mArcFragments.size() - 1);
        return null;
    }

    public void setSize(double pSize) {
        mSize = pSize;
        applyChange();
    }

    public void setTurnRadius(double pTurnRadius) {
        mTurnRadius = pTurnRadius;
        applyChange();
    }

    public void setSpeed(double pSpeed) {
        mSpeed = pSpeed;
        applyChange();
    }


    private void setInvisible(boolean pInvisible) {
        mInvisible = pInvisible;
        if (!pInvisible)
            applyChange();
    }

    public void setConfused(boolean pConfused) {
        mConfused = pConfused;
    }

    public boolean isCollisionAtPoint(Vec2 pPoint) {

        for (SnakeFragment fragment : mLineFragments) {
            if (fragment.isCollision(pPoint))
                return true;
        }
        for (SnakeFragment fragment : mArcFragments) {
            if (fragment.isCollision(pPoint))
                return true;
        }
        return false;
    }

    public boolean checkSelfCollision() {
        System.out.println("self collision at " + mPosition.x + " " + mPosition.y);
        return false;
    }

}
