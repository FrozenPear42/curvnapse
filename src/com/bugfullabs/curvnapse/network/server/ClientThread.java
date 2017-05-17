package com.bugfullabs.curvnapse.network.server;

import com.bugfullabs.curvnapse.network.message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;


/**
 * Server-side thread to interact with client via client socket
 */
public class ClientThread extends Thread {
    private static final Logger LOG = Logger.getAnonymousLogger();
    private static int UID = 0;
    private final Socket mSocket;
    private final CopyOnWriteArrayList<ClientListener> mListeners;
    private final ObjectOutputStream mObjectOutputStream;
    private final ObjectInputStream mObjectInputStream;
    private int mUID;
    private String mUserName;

    /**
     * Crate new client thread with given socket.
     * Thread will manage connection and all the received and sent messages
     * @param pSocket client socket
     * @throws IOException Thrown when could not create object stream via socket
     */
    public ClientThread(Socket pSocket) throws IOException {
        mSocket = pSocket;
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
        Message message;
        while (!mSocket.isClosed() && mSocket.isConnected()) {
            try {
                message = (Message) mObjectInputStream.readObject();
                for (ClientListener listener : mListeners)
                    listener.onClientMessage(this, message);
            } catch (SocketException e) {
                LOG.warning("Socket fail: " + e.getMessage());
                break;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Send given message to client
     * @param pMessage Message to be sent
     */
    public synchronized void sendMessage(Message pMessage) {
        try {
            mObjectOutputStream.writeObject(pMessage);
            mObjectOutputStream.reset();
        } catch (IOException e) {
            LOG.warning(e.getMessage());
        }
    }

    /**
     * Disconnects from client - closes it's socket
     */
    public void disconnect() {
        try {
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Register message listener
     * @param pClientListener listener
     */
    public void registerListener(ClientListener pClientListener) {
        mListeners.add(pClientListener);

    }

    /**
     * Remove message listener
     * @param pClientListener listener
     */
    public void removeListener(ClientListener pClientListener) {
        mListeners.remove(pClientListener);

    }

    /**
     * Set Client username
     * @param pName username
     */
    public void setUserName(String pName) {
        mUserName = pName;
    }

    /**
     * Get clientID
     * @return clientID
     */
    public int getID() {
        return mUID;
    }

    /**
     * Get username
     * @return client's username
     */
    public String getUsername() {
        return mUserName;
    }

    /**
     * Listener on client's messages
     */
    public interface ClientListener {
        void onClientMessage(ClientThread pClientThread, Message pMessage);
    }
}
