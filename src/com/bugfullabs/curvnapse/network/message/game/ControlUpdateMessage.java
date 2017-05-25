package com.bugfullabs.curvnapse.network.message.game;

import com.bugfullabs.curvnapse.network.message.Message;

public class ControlUpdateMessage extends Message {
    private int mPlayerID;
    private Direction mDirection;
    private Action mAction;

    public enum Direction {
        LEFT,
        RIGHT
    }

    public enum Action {
        UP,
        DOWN
    }

    public ControlUpdateMessage(int pPlayerID, Direction pDirection, Action pAction) {
        super(Type.CONTROL_UPDATE);
        mPlayerID = pPlayerID;
        mDirection = pDirection;
        mAction = pAction;
    }

    public int getPlayerID() {
        return mPlayerID;
    }

    public Direction getDirection() {
        return mDirection;
    }

    public Action getAction() {
        return mAction;
    }
}
