package com.bugfullabs.curvnapse.network.message;

import com.bugfullabs.curvnapse.snake.SnakeFragment;

public class SnakeFragmentMessage extends Message {
    private SnakeFragment mSnakeFragment;

    public SnakeFragmentMessage(SnakeFragment pSnakeFragment) {
        super(Type.SNAKE_UPDATE);
        mSnakeFragment = pSnakeFragment;
    }
}
