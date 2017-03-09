package com.bugfullabs.curvnapse.server;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

public class Client {
    private static final Logger LOG = Logger.getLogger(Client.class.getName());
    private Socket mSocket;
    private PrintWriter mWriter;
    private BufferedReader mReader;
    private String mName;

    public Client(Socket pSocket) throws IOException {
        mSocket = pSocket;
        mWriter = new PrintWriter(pSocket.getOutputStream(), true);
        mReader = new BufferedReader(new InputStreamReader(pSocket.getInputStream()));
        //mName = mReader.readLine();
        //LOG.info("Client: " + mName);
    }

    public boolean hasNewMessage() {
        try {
            return mReader.ready();
        } catch (IOException e) {
            LOG.warning("IO Error occurred");
            return false;
        }
    }

    public String getMessage() {
        try {
            return mReader.readLine();
        } catch (IOException e) {
            LOG.warning("IO Error occurred");
            return null;
        }
    }

    public void sendMessage(String pMessage) {
        mWriter.print(pMessage);
    }
}
