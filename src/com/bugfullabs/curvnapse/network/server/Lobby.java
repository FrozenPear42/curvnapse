package com.bugfullabs.curvnapse.network.server;


import com.bugfullabs.curvnapse.network.message.HandshakeMessage;
import com.bugfullabs.curvnapse.network.message.Message;
import com.bugfullabs.curvnapse.network.message.TextMessage;

import java.util.LinkedList;

public class Lobby implements ClientThread.ClientListener {
    private LinkedList<ClientThread> mClients;

    public Lobby() {
        mClients = new LinkedList<>();
    }

    public void addClient(ClientThread pClientThread, String pName) {
        for(ClientThread client : mClients)
            client.sendMessage(new HandshakeMessage(pName));

        mClients.add(pClientThread);
        pClientThread.registerListener(this);
    }

    @Override
    public void onClientMessage(ClientThread pClientThread, Message pMessage) {
        if(pMessage.getType() == Message.Type.TEXT) {
            TextMessage msg = (TextMessage) pMessage;
            for(ClientThread client : mClients)
                client.sendMessage(msg);
        }
    }
}
