package com.bugfullabs.curvnapse.network.message;

import java.io.Serializable;

public class Message implements Serializable {

    public enum Type {
        HANDSHAKE,
        ALIVE,
        DISCONNECT,
        TEXT,
        GAME_CREATE,
        GAME_UPDATE,
        GAME_JOIN_REQUEST,
        GAME_JOIN,
        ERROR
    }


    private Type mType;
    private String mName;

    public Message(Type pType) {
        mType = pType;
    }

    public Type getType() {
        return mType;
    }
}
