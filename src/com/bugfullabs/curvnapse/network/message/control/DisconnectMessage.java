package com.bugfullabs.curvnapse.network.message.control;

import com.bugfullabs.curvnapse.network.message.Message;

public class DisconnectMessage extends Message {
    public DisconnectMessage() {
        super(Type.DISCONNECT);
    }
}
