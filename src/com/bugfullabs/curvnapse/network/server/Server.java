package com.bugfullabs.curvnapse.network.server;

import com.bugfullabs.curvnapse.network.message.HandshakeMessage;
import com.bugfullabs.curvnapse.network.message.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.logging.Logger;

import static com.bugfullabs.curvnapse.network.message.Message.Type.HANDSHAKE;

public class Server extends Thread implements ClientThread.ClientListener {
    private static final Logger LOG = Logger.getLogger(Server.class.getName());

    private ServerSocket mServerSocket;
    private LinkedList<ClientThread> mClients;
    private Lobby mLobby;

    public Server(int pPort, int pMaxGames) throws IOException {
        mServerSocket = new ServerSocket(pPort);
        mClients = new LinkedList<>();
        mLobby = new Lobby(pMaxGames);
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
        switch (pMessage.getType()) {
            case HANDSHAKE:
                HandshakeMessage msg = (HandshakeMessage) pMessage;
                LOG.info("HANDSHAKE: " + msg.getName());
                pClientThread.setUserName(msg.getName());
                mLobby.addClient(pClientThread);
                break;
            default:
                LOG.warning("Unsupported message: " + pMessage.getType().name());
                break;
        }
    }

}

