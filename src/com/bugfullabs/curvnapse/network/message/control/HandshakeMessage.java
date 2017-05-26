package com.bugfullabs.curvnapse.network.message.control;


import com.bugfullabs.curvnapse.network.message.Message;

/**
 * Client requires registration from the server
 */
public class HandshakeMessage extends Message {
    private String mName;

    public HandshakeMessage(String pName) {
        super(Type.HANDSHAKE);
        mName = pName;
    }

    public String getName() {
        return mName;
    }

}
