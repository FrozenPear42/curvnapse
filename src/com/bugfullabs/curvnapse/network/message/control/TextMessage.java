package com.bugfullabs.curvnapse.network.message.control;


import com.bugfullabs.curvnapse.network.message.Message;
import com.bugfullabs.curvnapse.utils.SerializableColor;
import javafx.scene.paint.Color;

public class TextMessage extends Message {
    private String mAuthor;
    private String mMessage;
    private SerializableColor mTextColor;

    public TextMessage(String pAuthor, String pMessage, SerializableColor pTextColor) {
        super(Type.TEXT);
        mAuthor = pAuthor;
        mMessage = pMessage;
        mTextColor = pTextColor;
    }

    public TextMessage(String pAuthor, String pMessage) {
        super(Type.TEXT);
        mMessage = pMessage;
        mAuthor = pAuthor;
        mTextColor = new SerializableColor(Color.WHITE);
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getMessage() {
        return mMessage;
    }

    public SerializableColor getTextColor() {
        return mTextColor;
    }
}
