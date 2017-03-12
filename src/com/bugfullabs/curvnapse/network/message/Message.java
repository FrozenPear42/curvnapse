package com.bugfullabs.curvnapse.network.message;

import java.io.Serializable;
import java.io.StreamCorruptedException;

public class Message implements Serializable {

    public enum Type {UNSPECIFIED, HANDSHAKE, ALIVE, DISCONNECT, TEXT, GAME_CREATE, GAME_UPDATE}

    private Type mType;
    private String mName;

    public Message(Type pType) {
        mType = pType;
    }

    public Type getType(){
        return mType;
    }
}
