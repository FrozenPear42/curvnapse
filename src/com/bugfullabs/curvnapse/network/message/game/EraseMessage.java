package com.bugfullabs.curvnapse.network.message.game;

import com.bugfullabs.curvnapse.network.message.Message;

public class EraseMessage extends Message {
    public EraseMessage() {
        super(Type.BOARD_ERASE);
    }
}
