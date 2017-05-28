package com.bugfullabs.curvnapse.network.message;

import java.io.Serializable;

/**
 * Abstract class for messages using in client - server communication
 */
public abstract class Message implements Serializable {

    /**
     * Messages types
     */
    public enum Type {
        HANDSHAKE,
        WELCOME,
        DISCONNECT,
        TEXT,
        UPDATE_REQUEST,
        GAME_CREATE,
        GAME_UPDATE,
        NAME_UPDATE_REQUEST,
        GAME_JOIN_REQUEST,
        GAME_JOIN,
        PLAYER_ADD_REQUEST,
        GAME_START_REQUEST,
        GAME_START,
        CONTROL_UPDATE,
        SNAKE_UPDATE,
        PLAYER_UPDATE_REQUEST,
        SPAWN_POWERUP,
        POWERUP_UPDATE,
        LEAVE_GAME,
        BOARD_ERASE,
        SNAKE_KILLED,
        ROUND_UPDATE,
        GAMEOVER,
        GAME_REMOVED, PLAYER_DELETE
    }

    private Type mType;

    /**
     * Create new message of given type
     *
     * @param pType message type
     */
    public Message(Type pType) {
        mType = pType;
    }

    /**
     * @return message type
     */
    public Type getType() {
        return mType;
    }
}
