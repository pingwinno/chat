package com.pingwinno;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

@Slf4j
public class Server {
    private final int port;
    private final ArrayList<ClientHandler> clients = new ArrayList<>();

    public Server(int port) {
        this.port = port;
    }

    @SneakyThrows
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log.info("Server is running");
            //handle incoming connections
            while (true) {
                Socket clientSocket = serverSocket.accept();
                log.info("Incoming connection");
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
                log.info("Connection established");
            }
        } finally {
            log.info("Client closed");
            clients.forEach(ClientHandler::close);
        }
    }

    public void sendBroadcastMessage(String message) {
        clients.forEach(client -> client.sendMsg(message));
    }

    public void removeClient(ClientHandler client) {
        clients.remove(client);
    }
}
