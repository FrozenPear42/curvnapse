package com.bugfullabs.curvnapse.network.server;

import com.bugfullabs.curvnapse.network.message.HandshakeMessage;
import com.bugfullabs.curvnapse.network.message.Message;
import com.bugfullabs.curvnapse.network.message.WelcomeMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.logging.Logger;

/**
 * Main server thread - accepts new connections, and sends them to lobby
 */
public class Server extends Thread implements ClientThread.ClientListener {
    private static final Logger LOG = Logger.getLogger(Server.class.getName());

    private ServerSocket mServerSocket;
    private LinkedList<ClientThread> mClients;

    private Lobby mLobby;

    /**
     * Create new server thread
     *
     * @param pPort     server port
     * @param pMaxGames maximum count of games running simultaneously
     * @throws IOException thrown when could not create socket
     */
    public Server(int pPort, int pMaxGames) throws IOException {
        mServerSocket = new ServerSocket(pPort);
        mClients = new LinkedList<>();
        mLobby = new Lobby(this, pMaxGames);
    }

    /**
     * Stop accepting new connections and disconnect form all the clients
     */
    public void close() {
        try {
            mServerSocket.close();
            mClients.forEach(ClientThread::disconnect);
            LOG.info("Closed server socket");
        } catch (Exception e) {
            LOG.warning("Could not stop server socket");
        }
    }

    /**
     * Thread main function - accept new connections, create client threads for them and register to Handshake message listener
     */
    @Override
    public void run() {
        LOG.info("Accepting connections...");

        while (!mServerSocket.isClosed()) {
            try {
                Socket clientSocket = mServerSocket.accept();
                ClientThread clientThread = new ClientThread(clientSocket);
                clientThread.registerListener(this);
                clientThread.start();
                mClients.add(clientThread);
                LOG.info("Connection from " + clientSocket.getInetAddress());
            } catch (IOException e) {
                LOG.warning("Could not accept client: " + e.getMessage());
            }
        }
    }

    /**
     * Listener on client messages - here only listen on Handshake message required to join the lobby
     * @param pClientThread Sender client thread
     * @param pMessage message
     */
    @Override
    public synchronized void onClientMessage(ClientThread pClientThread, Message pMessage) {
        switch (pMessage.getType()) {
            case HANDSHAKE:
                HandshakeMessage msg = (HandshakeMessage) pMessage;
                LOG.info("HANDSHAKE: " + msg.getName());
                pClientThread.setUserName(msg.getName());
                pClientThread.sendMessage(new WelcomeMessage(pClientThread.getID()));
                mLobby.addClient(pClientThread);
                break;
        }
    }

}

