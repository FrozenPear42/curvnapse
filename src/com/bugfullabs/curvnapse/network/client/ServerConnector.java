package com.bugfullabs.curvnapse.network.client;

import com.bugfullabs.curvnapse.network.message.HandshakeMessage;
import com.bugfullabs.curvnapse.network.message.Message;
import com.bugfullabs.curvnapse.network.server.ClientThread;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

public class ServerConnector extends Thread {
    private static final Logger LOG = Logger.getLogger(ServerConnector.class.getName());
    private Socket mSocket;
    private final ObjectOutputStream mObjectOutputStream;
    private final ObjectInputStream mObjectInputStream;
    private final CopyOnWriteArrayList<ServerConnector.MessageListener> mListeners;

    public ServerConnector(String pIP, int pPort, String pName) throws IOException {
        mSocket = new Socket(pIP, pPort);
        mObjectOutputStream = new ObjectOutputStream(mSocket.getOutputStream());
        mObjectInputStream = new ObjectInputStream(mSocket.getInputStream());
        mListeners = new CopyOnWriteArrayList<>();
        sendMessage(new HandshakeMessage(pName));
    }

    public void sendMessage(Message pMessage) {
        try {
            mObjectOutputStream.writeObject(pMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerListener(MessageListener pListener) {
        mListeners.add(pListener);
    }

    public void unregisterListener(MessageListener pListener) {
        mListeners.remove(pListener);
    }

    @Override
    public void run() {
        while (!mSocket.isClosed()) {
            try {
                Message message = (Message) mObjectInputStream.readObject();
                for (MessageListener listener : mListeners)
                    listener.onClientMessage(message);

            } catch (IOException | ClassNotFoundException e) {
                LOG.warning("Ups, could not fetch message from server");
                break;
            }
        }
    }


    public interface MessageListener {
        void onClientMessage(Message pMessage);
    }
}
