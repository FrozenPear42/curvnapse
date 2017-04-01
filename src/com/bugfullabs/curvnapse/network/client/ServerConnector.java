package com.bugfullabs.curvnapse.network.client;

import com.bugfullabs.curvnapse.network.message.HandshakeMessage;
import com.bugfullabs.curvnapse.network.message.Message;
import com.bugfullabs.curvnapse.network.message.WelcomeMessage;

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

    public ServerConnector(String pIP, int pPort) throws IOException {
        mSocket = new Socket(pIP, pPort);
        mObjectOutputStream = new ObjectOutputStream(mSocket.getOutputStream());
        mObjectInputStream = new ObjectInputStream(mSocket.getInputStream());
        mListeners = new CopyOnWriteArrayList<>();
    }

    public synchronized void sendMessage(Message pMessage) {
        try {
            mObjectOutputStream.writeObject(pMessage);
            mObjectOutputStream.reset();
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

    public void handshake(String pName, HandshakeResultListener pListener) {
        final MessageListener listener = pMessage -> {
            if (pMessage.getType() == Message.Type.WELCOME) {
                pListener.onHandshakeResult(((WelcomeMessage) pMessage).getUserID());
            }
        };
        registerListener(listener);
        sendMessage(new HandshakeMessage(pName));
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

    public interface HandshakeResultListener {
        void onHandshakeResult(int pID);
    }
}
