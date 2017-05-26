package com.bugfullabs.curvnapse.network.message.control;

import com.bugfullabs.curvnapse.network.message.Message;

/**
 * client has disconnected
 */
public class DisconnectMessage extends Message {
    public DisconnectMessage() {
        super(Type.DISCONNECT);
    }
}
