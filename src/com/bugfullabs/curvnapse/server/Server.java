package com.bugfullabs.curvnapse.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class Server extends Thread {
    private static final Logger LOG = Logger.getLogger(Server.class.getName());

    private Thread mAcceptThread;
    private List<GameThread> mGameThreads;
    private ServerSocket mServerSocket;
    private final LinkedList<Client> mClients;
    private LinkedList<GameLobby> mLobbies;
    private int mMaxGames;

    public Server(int pPort, int pMaxGames) throws IOException {
        mServerSocket = new ServerSocket(pPort);
        mClients = new LinkedList<>();
        mLobbies = new LinkedList<>();
        mGameThreads = new LinkedList<>();
        mAcceptThread = new Thread(() -> {
            Socket clientSocket;
            LOG.info("Accepting connections...");
            while (!mServerSocket.isClosed()) {
                try {
                    clientSocket = mServerSocket.accept();
                    mClients.add(new Client(clientSocket));
                    LOG.info("Connection from " + clientSocket.getInetAddress());
                } catch (IOException e) {
                    System.out.print("Could not accept client: " + e.getMessage());
                }
            }
        });
        mAcceptThread.start();
    }

    public void broadcastMessage(String pMessage) {
        for (Client c : mClients) {
            c.sendMessage(pMessage);
        }
    }

    @Override
    public void start() {
        LOG.info("Starting server");
        super.start();
    }

    public void close() {
        try {
            mServerSocket.close();
            LOG.info("Closed server socket");
        } catch (Exception e) {
            LOG.warning("Could not stop server socket");
        }
    }

    @Override
    public void run() {
        mClients.parallelStream().filter(Client::hasNewMessage).forEach(c -> {
            LOG.info(c.getMessage());
        });
    }
}

