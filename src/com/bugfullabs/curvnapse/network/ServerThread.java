package com.bugfullabs.curvnapse.network;

import com.bugfullabs.curvnapse.player.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread {
    private final ServerSocket mSocket;

    public ServerThread(int pPort) throws IOException {
        mSocket = new ServerSocket(pPort);
    }

    private class PlayerServerThread extends Thread {
        private Socket mSocket;

        PlayerServerThread(Socket pSecket) {
            mSocket = pSecket;
        }

    }
}

