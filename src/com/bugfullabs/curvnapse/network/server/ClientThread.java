package com.bugfullabs.curvnapse.network.server;

import com.bugfullabs.curvnapse.network.message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

public class ClientThread extends Thread {
    private static final Logger LOG = Logger.getLogger(ClientThread.class.getName());
    private static int UID = 0;
    private final Socket mSocket;
    private final CopyOnWriteArrayList<ClientListener> mListeners;
    private final ObjectOutputStream mObjectOutputStream;
    private final ObjectInputStream mObjectInputStream;
    private int mUID;
    private String mUserName;

    public ClientThread(Socket pSocket) throws IOException {
        mSocket = pSocket;
        mUID = UID;
        UID += 1;
        mObjectOutputStream = new ObjectOutputStream(mSocket.getOutputStream());
        mObjectInputStream = new ObjectInputStream(mSocket.getInputStream());
        mListeners = new CopyOnWriteArrayList<>();
        mUserName = "Player " + mUID;
    }

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


    public synchronized void sendMessage(Message pMessage) {
        try {
            mObjectOutputStream.writeObject(pMessage);
            mObjectOutputStream.reset();
        } catch (IOException e) {
            LOG.warning(e.getMessage());
        }
    }

    public void disconnect() {
        try {
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerListener(ClientListener pClientListener) {
        mListeners.add(pClientListener);

    }

    public void removeListener(ClientListener pClientListener) {
        mListeners.remove(pClientListener);

    }

    public void setUserName(String pName) {
        mUserName = pName;
    }

    public int getID() {
        return mUID;
    }


    public String getUsername() {
        return mUserName;
    }

    public interface ClientListener {
        void onClientMessage(ClientThread pClientThread, Message pMessage);
    }
}
