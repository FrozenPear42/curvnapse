package com.bugfullabs.curvnapse.network.message.control;

import com.bugfullabs.curvnapse.network.message.control.TextMessage;

/**
 * Created by wojciech on 29.03.17.
 */
public class ServerTextMessage extends TextMessage {
    public ServerTextMessage(String pMessage) {
        super("Server", pMessage);
    }
}
