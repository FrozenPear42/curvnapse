package com.bugfullabs.curvnapse.network.message.game;

import com.bugfullabs.curvnapse.network.message.Message;

/**
 * Yeah, key was pressed or is no longer being pressed...
 * Basically message for informing server about keys being pressed
 */
public class ControlUpdateMessage extends Message {
    private int mPlayerID;
    private Direction mDirection;
    private Action mAction;

    /**
     * Moving direction
     */
    public enum Direction {
        LEFT,
        RIGHT
    }

    /**
     * Key status
     */
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
