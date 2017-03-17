package com.bugfullabs.curvnapse.network.server;

import com.bugfullabs.curvnapse.network.message.Message;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
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

    public ClientThread(Socket pSocket) throws IOException {
        mSocket = pSocket;
        mUID = UID;
        UID += 1;
        mObjectOutputStream = new ObjectOutputStream(mSocket.getOutputStream());
        mObjectInputStream = new ObjectInputStream(mSocket.getInputStream());
        mListeners = new CopyOnWriteArrayList<>();
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

    public void sendMessage(Message pMessage) {
        try {
            mObjectOutputStream.writeObject(pMessage);
        } catch (IOException e) {
            e.printStackTrace();
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

    public int getID() {
        return mUID;
    }

    public interface ClientListener {
        void onClientMessage(ClientThread pClientThread, Message pMessage);
    }
}
