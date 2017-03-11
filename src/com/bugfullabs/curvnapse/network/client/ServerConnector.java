package com.bugfullabs.curvnapse.network.client;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

public class ServerConnector {
    private static final Logger LOG = Logger.getLogger(ServerConnector.class.getName());
    private Socket mSocket;
    private final ObjectOutputStream mObjectOutputStream;
    private final ObjectInputStream mObjectInputStream;

    public ServerConnector(String pIP, int pPort, String pName) throws IOException {
        mSocket = new Socket(pIP, pPort);
        mObjectOutputStream = new ObjectOutputStream(mSocket.getOutputStream());
        mObjectInputStream = new ObjectInputStream(mSocket.getInputStream());
    }
}
