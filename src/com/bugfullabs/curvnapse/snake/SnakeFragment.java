package com.bugfullabs.curvnapse.snake;

import java.io.Serializable;

public class SnakeFragment implements Serializable {
    public enum Type {LINE, ARC}

    private static long UID = 0;

    private Type mType;
    private long mUID;

    public SnakeFragment(Type pType) {
        mType = pType;
        mUID = UID;
        UID += 1;
    }

    public Type getType() {
        return mType;
    }

    public long getUID() {
        return mUID;
    }
}
