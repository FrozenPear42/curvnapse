package com.bugfullabs.curvnapse.network.server;


import com.bugfullabs.curvnapse.Main;
import com.bugfullabs.curvnapse.network.message.HandshakeMessage;
import com.bugfullabs.curvnapse.network.message.Message;
import com.bugfullabs.curvnapse.network.message.TextMessage;

import java.util.LinkedList;
import java.util.logging.Logger;

public class Lobby implements ClientThread.ClientListener {
    private LinkedList<ClientThread> mClients;
    private static final Logger LOG = Logger.getLogger(Lobby.class.getName());

    public Lobby() {
        mClients = new LinkedList<>();
    }

    public void addClient(ClientThread pClientThread, String pName) {
        for (ClientThread client : mClients)
            client.sendMessage(new HandshakeMessage(pName));

        mClients.add(pClientThread);
        pClientThread.registerListener(this);
    }

    @Override
    public void onClientMessage(ClientThread pClientThread, Message pMessage) {
        if (pMessage.getType() == Message.Type.TEXT) {
            TextMessage msg = (TextMessage) pMessage;
            LOG.info(msg.getAuthor() + "  " + msg.getMessage());
            for (ClientThread client : mClients)
                client.sendMessage(msg);
        }
    }
}
