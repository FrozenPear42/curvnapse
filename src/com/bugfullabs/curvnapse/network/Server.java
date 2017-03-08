package com.bugfullabs.curvnapse.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.logging.Logger;

public class Server extends Thread {
    private static final Logger LOG = Logger.getLogger(Server.class.getName());

    private ServerSocket mSocket;
    private LinkedList<Client> mClients;
    private LinkedList<GameLobby> mLobbies;
    private int mMaxGames;

    public Server(int pPort, int pMaxGames) throws IOException {
        mSocket = new ServerSocket(pPort);
        mClients = new LinkedList<>();
        mLobbies = new LinkedList<>();
        LOG.info("Created server at port: " + pPort);
    }

    public void broadcastMessage(String pMessage) {
        for (Client c : mClients) {
            c.sendMessage(pMessage);
        }
    }

    @Override
    public void start() {
        LOG.info("Starting listening");
        super.start();
    }

    public void close() {
        try {
            mSocket.close();
            LOG.info("Closed server socket");
        } catch (Exception e) {
            LOG.warning("Could not close server socket");
        }
    }

    @Override
    public void run() {
        Socket clientSocket;
        while (!mSocket.isClosed()) {
            try {
                clientSocket = mSocket.accept();
                mClients.add(new Client(clientSocket));
                LOG.info("Connection from " + clientSocket.getInetAddress());
            } catch (IOException e) {
                System.out.print("Could not accept client: " + e.getMessage());
            }
        }
    }

}

