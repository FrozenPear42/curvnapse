package com.bugfullabs.curvnapse.network.message;


public class TextMessage extends Message {
    private String mMessage;

    public TextMessage(String pMessage) {
        super(Type.TEXT);
        mMessage = pMessage;
    }

    public String getMessage() {
        return mMessage;
    }
}
