package com.bugfullabs.curvnapse.server;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

public class Client {
    private static final Logger LOG = Logger.getLogger(Client.class.getTypeName());
    private Socket mSocket;
    private PrintWriter mWriter;
    private BufferedReader mReader;
    private String mName;

    public Client(Socket pSocket) throws IOException {
        mSocket = pSocket;
        mWriter = new PrintWriter(pSocket.getOutputStream(), true);
        mReader = new BufferedReader(new InputStreamReader(pSocket.getInputStream()));
        mName = mReader.readLine();
        LOG.info("Client: " + mName);
    }

    public void sendMessage(String pMessage) {
        mWriter.print(pMessage);
    }
}
