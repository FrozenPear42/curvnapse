package com.bugfullabs.curvnapse.network.message;


public class TextMessage extends Message {
    private String mMessage;

    public TextMessage(String pMessage) {
        super(Type.HANDSHAKE);
        mMessage = pMessage;
    }

    public String getmMessage() {
        return mMessage;
    }
}
