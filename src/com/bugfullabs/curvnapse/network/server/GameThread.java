package com.bugfullabs.curvnapse.network.server;


import com.bugfullabs.curvnapse.network.client.ServerConnector;
import com.bugfullabs.curvnapse.network.message.Message;

import java.util.Timer;
import java.util.TimerTask;

public class GameThread implements ServerConnector.MessageListener {
    private Timer mTimer;

    public GameThread() {
        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

            }
        }, 0, 1000/60);
    }

    @Override
    public void onClientMessage(Message pMessage) {

    }
}
