package com.bugfullabs.curvnapse.server;

import com.bugfullabs.curvnapse.Main;
import com.bugfullabs.curvnapse.Message;

import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public class MessageDispatcher extends Thread {
    private static final Logger LOG = Logger.getLogger(MessageDispatcher.class.getName());

    private BlockingQueue<Message> mInMessages;
    private BlockingQueue<Message> mOutMessages;
    private LinkedList<Client> mClients;

    public MessageDispatcher() {
        mClients = new LinkedList<>();
    }

    public void registerClient(Client pClient) {
        mClients.add(pClient);

    }

    public void sendMessage(Message pMessage) {
        mOutMessages.add(pMessage);
    }

    @Override
    public void run() {
        while (true) {
            mClients.stream().filter(Client::hasNewMessage).forEach(pClient -> {
                LOG.info(pClient.getMessage());
            });
        }
    }
}
