package com.bugfullabs.curvnapse.network.message;


public class TextMessage extends Message {
    private String mAuthor;
    private String mMessage;

    public TextMessage(String pAuthor, String pMessage) {
        super(Type.TEXT);
        mMessage = pMessage;
        mAuthor = pAuthor;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getMessage() {
        return mMessage;
    }
}
