package com.pingwinno;

import java.util.LinkedList;

public class ClientStorage {
    private static final LinkedList<ClientHandler> clients = new LinkedList<>();

    synchronized public static void addClient(ClientHandler handler) {
        clients.add(handler);
    }

    synchronized public static void removeClient(ClientHandler handler) {
        clients.remove(handler);
    }

    public static LinkedList<ClientHandler> getClients() {
        return new LinkedList<>(clients);
    }
}
