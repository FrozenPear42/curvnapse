package com.bugfullabs.curvnapse.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.logging.Logger;

public class Server extends Thread {
    private static final Logger LOG = Logger.getLogger(Server.class.getName());

    private ServerSocket mServerSocket;
    private LinkedList<ClientThread> mClients;
    private LinkedList<GameLobby> mLobbies;
    private LinkedList<GameThread> mGameThreads;

    public Server(int pPort, int pMaxGames) throws IOException {
        mServerSocket = new ServerSocket(pPort);
        mLobbies = new LinkedList<>();
        mGameThreads = new LinkedList<>();
        mClients = new LinkedList<>();
    }

    public void close() {
        try {
            mServerSocket.close();
            for(ClientThread client : mClients)
                client.disconnect();
            LOG.info("Closed server socket");
        } catch (Exception e) {
            LOG.warning("Could not stop server socket");
        }
    }

    @Override
    public void run() {
        Socket clientSocket;
        ClientThread clientThread;

        LOG.info("Accepting connections...");
        while (!mServerSocket.isClosed()) {
            try {
                clientSocket = mServerSocket.accept();
                clientThread = new ClientThread(clientSocket);
                clientThread.start();
                mClients.add(clientThread);
                LOG.info("Connection from " + clientSocket.getInetAddress());
            } catch (IOException e) {
                System.out.print("Could not accept client: " + e.getMessage());
            }
        }
    }
}

