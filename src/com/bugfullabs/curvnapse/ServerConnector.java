package com.bugfullabs.curvnapse;

import com.bugfullabs.curvnapse.server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

public class ServerConnector {
    private static final Logger LOG = Logger.getLogger(ServerConnector.class.getName());
    private Socket mSocket;
    private PrintWriter mWriter;
    private BufferedReader mReader;

    public ServerConnector(String pIP, int pPort, String pName) throws IOException {
        mSocket = new Socket(pIP, pPort);
        mWriter = new PrintWriter(mSocket.getOutputStream(), true);
        mReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
        mWriter.println(pName);
    }
}
