package com.bugfullabs.curvnapse.network.message;

import java.io.Serializable;

public abstract class Message implements Serializable {

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
        PLAYER_UPDATE_REQUEST, SPAWN_POWERUP, POWERUP_UPDATE, LEAVE_GAME, ERROR
    }

    private Type mType;

    public Message(Type pType) {
        mType = pType;
    }

    public Type getType() {
        return mType;
    }
}
