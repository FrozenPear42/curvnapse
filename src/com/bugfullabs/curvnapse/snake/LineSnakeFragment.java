package com.bugfullabs.curvnapse.snake;

import com.bugfullabs.curvnapse.utils.Vec2;

import java.io.Serializable;

public class LineSnakeFragment extends SnakeFragment implements Serializable {
    private Vec2 mBegin;
    private Vec2 mEnd;
    private double mSize;


    public LineSnakeFragment(Vec2 pBegin, Vec2 pEnd, double pSize) {
        super(Type.LINE);
        mBegin = pBegin;
        mEnd = pEnd;
        mSize = pSize;
    }

    public void updateHead(Vec2 pEnd) {
        mEnd = pEnd;
    }

    public Vec2 getBegin() {
        return mBegin;
    }

    public Vec2 getEnd() {
        return mEnd;
    }

    public double getSize() {
        return mSize;
    }
}
