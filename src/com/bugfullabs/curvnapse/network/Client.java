package com.bugfullabs.curvnapse.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private Socket mSocket;
    private PrintWriter mWriter;
    private BufferedReader mReader;

    public Client(Socket pSocket) throws IOException {
        mSocket = pSocket;
        mWriter = new PrintWriter(pSocket.getOutputStream(), true);
        mReader = new BufferedReader(new InputStreamReader(pSocket.getInputStream()));
    }

    public void sendMessage(String pMessage) {
        mWriter.print(pMessage);
    }
}
