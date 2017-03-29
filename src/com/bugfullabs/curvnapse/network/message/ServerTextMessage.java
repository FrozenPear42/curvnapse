package com.bugfullabs.curvnapse.network.message;

/**
 * Created by wojciech on 29.03.17.
 */
public class ServerTextMessage extends TextMessage {
    public ServerTextMessage(String pMessage) {
        super("Server", pMessage);
    }
}
