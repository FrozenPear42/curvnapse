package com.bugfullabs.curvnapse.network;

import java.io.Serializable;

public class Message implements Serializable {
    public enum Type { HANDSHAKE, ALIVE, DISCONNECT, };
}
