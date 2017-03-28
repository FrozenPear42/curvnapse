package com.bugfullabs.curvnapse.network.message;

import java.io.Serializable;

public abstract class Message implements Serializable {

    public enum Type {
        HANDSHAKE,
        WELCOME,
        ALIVE,
        DISCONNECT,
        TEXT,
        GAME_CREATE,
        GAME_UPDATE,
        GAME_JOIN_REQUEST,
        GAME_JOIN,
        PLAYER_ADD_REQUEST,
        GAME_START_REQUEST,
        GAME_START,
        CONTROL_UPDATE,
        SNAKE_UPDATE,
        ERROR
    }

    private Type mType;

    public Message(Type pType) {
        mType = pType;
    }

    public Type getType() {
        return mType;
    }
}
