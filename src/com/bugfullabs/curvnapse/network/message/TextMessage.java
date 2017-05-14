package com.bugfullabs.curvnapse.network.message;


import com.bugfullabs.curvnapse.player.PlayerColor;
import javafx.scene.paint.Color;

public class TextMessage extends Message {
    private String mAuthor;
    private String mMessage;
    private PlayerColor mTextColor;

    public TextMessage(String pAuthor, String pMessage, PlayerColor pTextColor) {
        super(Type.TEXT);
        mAuthor = pAuthor;
        mMessage = pMessage;
        mTextColor = pTextColor;
    }

    public TextMessage(String pAuthor, String pMessage) {
        super(Type.TEXT);
        mMessage = pMessage;
        mAuthor = pAuthor;
        mTextColor = new PlayerColor(Color.WHITE);
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getMessage() {
        return mMessage;
    }

    public PlayerColor getTextColor() {
        return mTextColor;
    }
}
