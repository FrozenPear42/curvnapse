package com.bugfullabs.curvnapse.network.server;

import com.bugfullabs.curvnapse.network.client.Game;
import com.bugfullabs.curvnapse.network.message.GameCreateRequestMessage;
import com.bugfullabs.curvnapse.network.message.GameUpdateMessage;
import com.bugfullabs.curvnapse.network.message.HandshakeMessage;
import com.bugfullabs.curvnapse.network.message.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.logging.Logger;

public class Server extends Thread implements ClientThread.ClientListener {
    private static final Logger LOG = Logger.getLogger(Server.class.getName());

    private ServerSocket mServerSocket;
    private LinkedList<ClientThread> mClients;
    private LinkedList<GameLobby> mLobbies;
    private LinkedList<GameThread> mGameThreads;
    private Lobby mLobby;

    public Server(int pPort, int pMaxGames) throws IOException {
        mServerSocket = new ServerSocket(pPort);
        mLobbies = new LinkedList<>();
        mGameThreads = new LinkedList<>();
        mClients = new LinkedList<>();
        mLobby = new Lobby();
    }

    public void close() {
        try {
            mServerSocket.close();
            for (ClientThread client : mClients)
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
                clientThread.registerListener(this);
                clientThread.start();
                mClients.add(clientThread);
                LOG.info("Connection from " + clientSocket.getInetAddress());
            } catch (IOException e) {
                System.out.print("Could not accept client: " + e.getMessage());
            }
        }
    }

    @Override
    public void onClientMessage(ClientThread pClientThread, Message pMessage) {
        if (pMessage.getType() == Message.Type.HANDSHAKE) {
            HandshakeMessage msg = (HandshakeMessage) pMessage;
            LOG.info("new message from client: " + msg.getName());
            mLobby.addClient(pClientThread, msg.getName());
        }
        if (pMessage.getType() == Message.Type.GAME_CREATE) {
            GameCreateRequestMessage msg = (GameCreateRequestMessage) pMessage;
            LOG.info("new game request");
            for (ClientThread client : mClients) {
                client.sendMessage(new GameUpdateMessage(new Game()));
            }
        }

    }
}

