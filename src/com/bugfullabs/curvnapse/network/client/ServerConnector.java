package com.bugfullabs.curvnapse.network.client;

import com.bugfullabs.curvnapse.network.message.control.DisconnectMessage;
import com.bugfullabs.curvnapse.network.message.control.HandshakeMessage;
import com.bugfullabs.curvnapse.network.message.Message;
import com.bugfullabs.curvnapse.network.message.control.WelcomeMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

/**
 * Running thread for interacting wt server-side va Socket connection
 */
public class ServerConnector extends Thread {
    private static final Logger LOG = Logger.getLogger(ServerConnector.class.getName());
    private Socket mSocket;
    private final ObjectOutputStream mObjectOutputStream;
    private final ObjectInputStream mObjectInputStream;
    private final CopyOnWriteArrayList<ServerConnector.MessageListener> mListeners;
    private final ConnectionLostListener mConnectionLostListener;

    /**
     * Create connector connected to given server
     *
     * @param pIP   Server IP
     * @param pPort Server Port
     * @throws IOException thrown on connection error
     */
    public ServerConnector(String pIP, int pPort, ConnectionLostListener pListener) throws IOException {
        mSocket = new Socket(pIP, pPort);
        mObjectOutputStream = new ObjectOutputStream(mSocket.getOutputStream());
        mObjectInputStream = new ObjectInputStream(mSocket.getInputStream());
        mListeners = new CopyOnWriteArrayList<>();
        mConnectionLostListener = pListener;
    }

    /**
     * Register listener on messages received from server
     *
     * @param pListener listener
     */
    public void registerListener(MessageListener pListener) {
        mListeners.add(pListener);
    }

    /**
     * Remove registered listener
     *
     * @param pListener listener to be removed
     */
    public void unregisterListener(MessageListener pListener) {
        mListeners.remove(pListener);
    }

    /**
     * Send handshake message to server
     *
     * @param pName     username
     * @param pListener handshake callback function
     */
    public void handshake(String pName, HandshakeResultListener pListener) {
        final MessageListener listener = pMessage -> {
            if (pMessage.getType() == Message.Type.WELCOME) {
                pListener.onHandshakeResult(((WelcomeMessage) pMessage).getUserID());
            }
        };
        registerListener(listener);
        sendMessage(new HandshakeMessage(pName));
    }

    /**
     * Send message to server
     *
     * @param pMessage message to sent
     */
    public synchronized void sendMessage(Message pMessage) {
        try {
            mObjectOutputStream.writeObject(pMessage);
            mObjectOutputStream.reset();
        } catch (IOException e) {
            LOG.warning("Could not send message to the server, probably connection is closed or broken");
            disconnect();
        }
    }


    /**
     * Thread run function
     * Receives messages from server
     */
    @Override
    public void run() {
        while (!mSocket.isClosed()) {
            try {
                Message message = (Message) mObjectInputStream.readObject();
                for (MessageListener listener : mListeners)
                    listener.onClientMessage(message);

            } catch (IOException | ClassNotFoundException e) {
                LOG.warning("Ups, could not fetch message from server");
                disconnect();
                break;
            }
        }
    }

    /**
     * Disconnects fromm the server
     */
    public void disconnect() {
        mListeners.forEach(l -> l.onClientMessage(new DisconnectMessage()));
        mConnectionLostListener.onConnectionLost();
        mListeners.clear();
        try {
            mSocket.close();
        } catch (Exception e) {
            LOG.warning("Could not close client socket");
        }
    }

    /**
     * Client message listener
     */
    public interface MessageListener {
        /**
         * Invoked on message received
         *
         * @param pMessage received message
         */
        void onClientMessage(Message pMessage);
    }

    /**
     * Handshake result listener
     */
    public interface HandshakeResultListener {
        /**
         * Invoked on handshake message accept
         *
         * @param pID userID received from server
         */
        void onHandshakeResult(int pID);
    }

    /**
     * Listener on connection lost
     */
    public interface ConnectionLostListener {
        /**
         * Invoked when connection with server is lost
         */
        void onConnectionLost();
    }

}
