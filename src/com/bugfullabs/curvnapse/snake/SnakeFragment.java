package com.bugfullabs.curvnapse.snake;

public class SnakeFragment {
    public enum Type {LINE, ARC}

    private Type mType;

    public SnakeFragment(Type pType) {
        mType = pType;
    }

    public Type getType() {
        return mType;
    }


}
