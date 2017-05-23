package com.bugfullabs.curvnapse.snake;

import com.bugfullabs.curvnapse.utils.SerializableColor;
import com.bugfullabs.curvnapse.powerup.PowerUp;
import com.bugfullabs.curvnapse.utils.MathUtils;
import com.bugfullabs.curvnapse.utils.Vec2;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class representing snake on the board, handling all the movement and generating all snake vector fragments
 */
public class Snake {

    /**
     * Snake state enum
     */
    private enum State {
        TURNING_LEFT,
        TURNING_RIGHT,
        FORWARD,
        STOP
    }

    private static final double DEFAULT_SPEED = 0.10f;
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

    private SerializableColor mColor;

    private State mState;

    private List<ArcSnakeFragment> mArcFragments;
    private List<LineSnakeFragment> mLineFragments;

    private List<Pair<PowerUp, Double>> mPowerUps;

    private Random mRandom;
    private boolean mHoleNow;
    private int mHoleTime;

    private boolean mConfused;

    /**
     * Create new snake with given params
     *
     * @param pUID      ID
     * @param pPosition snake head position
     * @param pAngle    direction
     * @param pColor    color of the snake
     */
    public Snake(int pUID, Vec2 pPosition, double pAngle, SerializableColor pColor) {
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

    /**
     * Do step movement
     *
     * @param pDelta delta time in seconds
     */
    public void step(double pDelta) {
        if (mDead)
            return;

        mPowerUps.replaceAll(pair -> new Pair<>(pair.getKey(), pair.getValue() - pDelta));
        mPowerUps.stream().filter(pair -> pair.getValue() <= 0).forEach(pair -> pair.getKey().onEnd(this));
        mPowerUps.removeIf(pair -> pair.getValue() <= 0);

        mHoleTime -= pDelta;
//        if (mHoleTime <= 0) {
//            if (!mHoleNow) {
//                setInvisible(true);
//                mHoleNow = true;
//                mHoleTime = mRandom.nextInt(1000) + 100;
//            } else {
//                setInvisible(false);
//                mHoleNow = false;
//                mHoleTime = mRandom.nextInt(3000) + 3000;
//            }
//        }


        double deltaAngle = (mVelocity.length() / mTurnRadius) * pDelta;
        switch (mState) {
            case FORWARD:
                mPosition = Vec2.add(mPosition, Vec2.times(mVelocity, pDelta));
                if (!mInvisible)
                    mLineFragments.get(mLineFragments.size() - 1).updateHead(new Vec2(mPosition));
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

    /**
     * Activates PowerUp for the snake
     *
     * @param pPowerUp PowerUp to activate
     */
    public void addPowerUp(PowerUp pPowerUp) {
        mPowerUps.add(0, new Pair<>(pPowerUp, pPowerUp.getDuration()));
        pPowerUp.onBegin(this);
        applyChange();
    }

    /**
     * Change snake movement state to left
     */
    public void turnLeft() {
        if (mDead) return;
        mState = !mConfused ? State.TURNING_LEFT : State.TURNING_RIGHT;
        applyChange();
    }

    /**
     * Change snake movement state to right
     */
    public void turnRight() {
        if (mDead) return;
        mState = mConfused ? State.TURNING_LEFT : State.TURNING_RIGHT;
        applyChange();
    }

    /**
     * Stop turning, change movement type to FORWARD
     */
    public void turnEnd() {
        if (mDead) return;
        doLine();
        mState = State.FORWARD;
    }

    /**
     * Begin line
     */
    private void doLine() {
        mVelocity = Vec2.directed(mSpeed, mAngle);
        mVelocity.y = -mVelocity.y;

        LineSnakeFragment line = new LineSnakeFragment(new Vec2(mPosition), new Vec2(mPosition), mVelocity.angle(), mColor, mSize);
        mLineFragments.add(line);
    }

    /**
     * Begin new arc
     *
     * @param pLeft true if turning left
     */
    private void doArc(boolean pLeft) {
        if (pLeft) {
            double startAngle = MathUtils.normalizeAngle(mAngle - Math.PI / 2);
            mTurnCenter.x = mPosition.x - mTurnRadius * Math.sin(mAngle);
            mTurnCenter.y = mPosition.y - mTurnRadius * Math.cos(mAngle);
            Vec2 turnCenter = new Vec2(mTurnCenter.x, mTurnCenter.y);
            ArcSnakeFragment arc = new ArcSnakeFragment(startAngle, mTurnRadius, turnCenter, mColor, mSize);
            mArcFragments.add(arc);
        } else {
            double startAngle = MathUtils.normalizeAngle(mAngle + Math.PI / 2);
            mTurnCenter.x = mPosition.x + mTurnRadius * Math.sin(mAngle);
            mTurnCenter.y = mPosition.y + mTurnRadius * Math.cos(mAngle);
            Vec2 turnCenter = new Vec2(mTurnCenter.x, mTurnCenter.y);
            ArcSnakeFragment arc = new ArcSnakeFragment(startAngle, mTurnRadius, turnCenter, mColor, mSize);
            mArcFragments.add(arc);
        }
    }

    /**
     * Apply change of state
     */
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

    /**
     * Change head position
     *
     * @param pDestination new head position
     */
    public void teleport(Vec2 pDestination) {
        mPosition = pDestination;
        applyChange();
    }


    /**
     * Remove all snake fragments and start new fragment
     */
    public void erase() {
        mArcFragments.clear();
        mLineFragments.clear();
        applyChange();
    }

    /**
     * Kill the snake
     */
    public void kill() {
        mDead = true;
    }

    /**
     * Return actual head position
     *
     * @return head position
     */
    public Vec2 getPosition() {
        return mPosition;
    }

    /**
     * @return size of the snake
     */
    public double getSize() {
        return mSize;
    }

    /**
     * @return tuen radius
     */
    public double getTurnRadius() {
        return mTurnRadius;
    }

    /**
     * @return snake speed
     */
    public double getSpeed() {
        return mSpeed;
    }

    public boolean isConfused() {
        return mConfused;
    }

    /**
     * @return true if snake is dead
     */
    public boolean isDead() {
        return mDead;
    }

    /**
     * @return last snake fragment
     */
    public SnakeFragment getLastFragment() {
        if (mInvisible)
            return new HeadSnakeFragment(mColor, mSize, mPosition);

        if (mState == State.FORWARD)
            return mLineFragments.get(mLineFragments.size() - 1);
        else if (mState == State.TURNING_RIGHT || mState == State.TURNING_LEFT)
            return mArcFragments.get(mArcFragments.size() - 1);
        return null;
    }

    /**
     * @return new Snake Head Fragment for plotting while invisible
     */
    public SnakeFragment getHead() {
        return new HeadSnakeFragment(mColor, mSize, mPosition);
    }

    /**
     * @return snake color
     */
    public SerializableColor getColor() {
        return mColor;
    }

    /**
     * Cjhange snake size to given size
     * @param pSize
     */
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


    public boolean isInvisible() {
        return mInvisible;
    }

    public int getLinesCount() {
        return mLineFragments.size();
    }

    public int getArcsCount() {
        return mArcFragments.size();
    }

    /**
     * Check collision at given point
     *
     * @param pPoint point to collide with
     * @return *true* if collision, false otherwise
     */
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

    /**
     * Returns all the snake fragments
     *
     * @return alle the fragments gdmn
     */
    public List<SnakeFragment> getFragments() {
        ArrayList<SnakeFragment> list = new ArrayList<>();
        list.addAll(mLineFragments);
        list.addAll(mArcFragments);
        return list;
    }


    /**
     * Check collision with itself
     *
     * @return true if collision
     */
    public boolean checkSelfCollision() {
        if (mState == State.FORWARD) {
            if (mLineFragments.size() <= 1)
                return false;

            for (SnakeFragment fragment : mLineFragments.subList(0, mLineFragments.size() - 1)) {
                if (fragment.isCollision(mPosition))
                    return true;
            }

            for (SnakeFragment fragment : mArcFragments) {
                if (fragment.isCollision(mPosition))
                    return true;
            }
        } else if (mState != State.STOP) {

            for (SnakeFragment fragment : mLineFragments) {
                if (fragment.isCollision(mPosition))
                    return true;
            }

            if (mArcFragments.size() == 1) {
                return mArcFragments.get(0).isFull();
            }

            for (SnakeFragment fragment : mArcFragments.subList(0, mArcFragments.size() - 1)) {
                if (fragment.isCollision(mPosition))
                    return true;
            }
        }

        return false;
    }

}
