package com.bugfullabs.curvnapse.network.message.control;

import com.bugfullabs.curvnapse.utils.SerializableColor;
import javafx.scene.paint.Color;

/**
 * Text message with server header
 */
public class ServerTextMessage extends TextMessage {
    public ServerTextMessage(String pMessage) {
        super("Server", pMessage, new SerializableColor(Color.RED));
    }
}
