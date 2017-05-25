package com.bugfullabs.curvnapse.network.server;

import com.bugfullabs.curvnapse.network.message.control.DisconnectMessage;
import com.bugfullabs.curvnapse.network.message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;


/**
 * Server-side thread to interact with client via client socket
 */
public class ClientThread extends Thread {
    private static final Logger LOG = Logger.getAnonymousLogger();
    private static int UID = 0;
    private final Socket mSocket;
    private final ClientConnectionListener mConnectionListener;
    private final List<ClientMessageListener> mListeners;
    private final ObjectOutputStream mObjectOutputStream;
    private final ObjectInputStream mObjectInputStream;
    private int mUID;
    private String mUserName;

    /**
     * Crate new client thread with given socket.
     * Thread will manage connection and all the received and sent messages
     *
     * @param pSocket client socket
     * @throws IOException Thrown when could not create object stream via socket
     */
    public ClientThread(Socket pSocket, ClientConnectionListener pListener) throws IOException {
        mSocket = pSocket;
        mConnectionListener = pListener;
        mUID = UID;
        UID += 1;
        mObjectOutputStream = new ObjectOutputStream(mSocket.getOutputStream());
        mObjectInputStream = new ObjectInputStream(mSocket.getInputStream());
        mListeners = new CopyOnWriteArrayList<>();
        mUserName = "Player " + mUID;
    }

    /**
     * Thread run function
     * Thread waits for message and when received dispatches it to all listeners
     */
    @Override
    public void run() {

        while (!mSocket.isClosed() && mSocket.isConnected()) {
            try {
                final Message message = (Message) mObjectInputStream.readObject();

                mListeners.forEach(l -> l.onClientMessage(this, message));

            } catch (SocketException e) {
                mConnectionListener.onDisconnect(this);
                break;
            } catch (IOException | ClassNotFoundException e) {
                LOG.warning("Somenthing wrong");
            }
        }
    }


    /**
     * Send given message to client
     *
     * @param pMessage Message to be sent
     */
    public synchronized void sendMessage(Message pMessage) {
        try {
            mObjectOutputStream.writeObject(pMessage);
            mObjectOutputStream.reset();
        } catch (IOException e) {
            mConnectionListener.onDisconnect(this);
        }
    }

    /**
     * Disconnects from client - closes it's socket
     */
    public void disconnect() {
        try {
            mListeners.forEach(l -> l.onClientMessage(this, new DisconnectMessage()));
            mSocket.close();
            mListeners.clear();
            mObjectInputStream.close();
            mObjectInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Register message listener
     *
     * @param pClientMessageListener listener
     */
    public void registerListener(ClientMessageListener pClientMessageListener) {
        mListeners.add(pClientMessageListener);
    }

    /**
     * Remove message listener
     *
     * @param pClientMessageListener listener
     */
    public void removeListener(ClientMessageListener pClientMessageListener) {
        mListeners.remove(pClientMessageListener);
    }

    /**
     * Set Client username
     *
     * @param pName username
     */
    public void setUserName(String pName) {
        mUserName = pName;
    }

    /**
     * Get clientID
     *
     * @return clientID
     */
    public int getID() {
        return mUID;
    }

    /**
     * Get username
     *
     * @return client's username
     */
    public String getUsername() {
        return mUserName;
    }

    /**
     * Listener on client's messages
     */
    public interface ClientMessageListener {
        void onClientMessage(ClientThread pClientThread, Message pMessage);
    }

    /**
     * Listener on connection status
     */
    public interface ClientConnectionListener {
        void onDisconnect(ClientThread pClientThread);
    }

}
