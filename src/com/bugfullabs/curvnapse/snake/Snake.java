package com.bugfullabs.curvnapse.snake;

import com.bugfullabs.curvnapse.player.PlayerColor;
import com.bugfullabs.curvnapse.powerup.FastPowerUp;
import com.bugfullabs.curvnapse.powerup.PowerUp;
import com.bugfullabs.curvnapse.powerup.SlowPowerUp;
import com.bugfullabs.curvnapse.utils.MathUtils;
import com.bugfullabs.curvnapse.utils.Vec2;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Snake {
    private static final Logger LOG = Logger.getLogger("SNAKE");
    public static final double DEFAULT_SPEED = 0.08f;
    public static final double DEFAULT_SIZE = 2.5f;
    public static final double DEFAULT_TURN_RADIUS = 20.0f;

    private enum State {
        TURNING_LEFT,
        TURNING_RIGHT,
        FORWARD,
        STOP
    }

    private int mUID;
    private double mSize;
    private double mAngle;
    private Vec2 mVelocity;
    private Vec2 mPosition;
    private Vec2 mTurnCenter;
    private double mTurnRadius;
    private double mSpeed;

    private PlayerColor mColor;

    private List<ArcSnakeFragment> mArcFragments;
    private List<LineSnakeFragment> mLineFragments;

    private State mState;

    private List<Pair<PowerUp, Double>> mPowerUps;

    public Snake(int pUID, Vec2 pPosition, double pAngle, PlayerColor pColor) {
        mColor = pColor;
        mUID = pUID;
        mPosition = pPosition;
        mAngle = pAngle;

        mArcFragments = new ArrayList<>();
        mLineFragments = new ArrayList<>();
        mPowerUps = new ArrayList<>();

        mSpeed = DEFAULT_SPEED;
        mVelocity = Vec2.directed(DEFAULT_SPEED, pAngle);

        mTurnCenter = new Vec2();
        mTurnRadius = DEFAULT_TURN_RADIUS;

        mSize = DEFAULT_SIZE;

        mState = State.FORWARD;
        doLine();
    }

    public void step(double pDelta) {
        mPowerUps.replaceAll(pair -> new Pair<>(pair.getKey(), pair.getValue() - pDelta));
        mPowerUps.stream().filter(pair -> pair.getValue() <= 0).forEach(pair -> pair.getKey().onEnd(this));
        mPowerUps.removeIf(pair -> pair.getValue() <= 0);

        double deltaAngle = (mVelocity.length() / mTurnRadius) * pDelta;
        switch (mState) {
            case FORWARD:
                mPosition = Vec2.add(mPosition, Vec2.times(mVelocity, pDelta));
                mLineFragments.get(mLineFragments.size() - 1).updateHead(mPosition);
                break;

            case TURNING_LEFT:
                mAngle += deltaAngle;
                mAngle = MathUtils.normalizeAngle(mAngle);
                mPosition.x = mTurnCenter.x + mTurnRadius * Math.sin(mAngle);
                mPosition.y = mTurnCenter.y + mTurnRadius * Math.cos(mAngle);
                mArcFragments.get(mArcFragments.size() - 1).updateHead(deltaAngle);
                break;

            case TURNING_RIGHT:
                mAngle -= (mVelocity.length() / mTurnRadius) * pDelta;
                mAngle = MathUtils.normalizeAngle(mAngle);
                mPosition.x = mTurnCenter.x - mTurnRadius * Math.sin(mAngle);
                mPosition.y = mTurnCenter.y - mTurnRadius * Math.cos(mAngle);
                mArcFragments.get(mArcFragments.size() - 1).updateHead(-deltaAngle);
                break;
        }
    }

    public void addPowerUp(PowerUp pPowerUp) {
        mPowerUps.add(0, new Pair<>(pPowerUp, pPowerUp.getDuration()));
        pPowerUp.onBegin(this);
    }

    public void turnLeft() {
        if (!isAlive())
            return;
        doArc(true);
        mState = State.TURNING_LEFT;
    }

    public void turnRight() {
        if (!isAlive())
            return;
        doArc(false);
        mState = State.TURNING_RIGHT;
    }

    public void turnEnd() {
        if (!isAlive())
            return;
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


    List<SnakeFragment> getFragments() {
        List<SnakeFragment> fragments = new ArrayList<>(mArcFragments);
        fragments.addAll(mLineFragments);
        return fragments;
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

    public void kill() {
        mState = State.STOP;
    }

    public boolean isAlive() {
        return mState != State.STOP;
    }

    public SnakeFragment getLastFragment() {
        if (mState == State.FORWARD)
            return mLineFragments.get(mLineFragments.size() - 1);
        else if (mState == State.TURNING_RIGHT || mState == State.TURNING_LEFT)
            return mArcFragments.get(mArcFragments.size() - 1);

        return null;
    }

}
