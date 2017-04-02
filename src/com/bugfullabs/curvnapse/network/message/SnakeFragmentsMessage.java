package com.bugfullabs.curvnapse.network.message;

import com.bugfullabs.curvnapse.snake.SnakeFragment;

import java.util.LinkedList;

public class SnakeFragmentsMessage extends Message {
    private LinkedList<SnakeFragment> mSnakeFragments;

    public SnakeFragmentsMessage(LinkedList<SnakeFragment> pSnakeFragments) {
        super(Type.SNAKE_UPDATE);
        mSnakeFragments = pSnakeFragments;
    }

    public LinkedList<SnakeFragment> getSnakeFragments() {
        return mSnakeFragments;
    }
}
