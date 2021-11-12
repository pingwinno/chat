package com.pingwinno;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class Server {
    private final int port;

    public Server(int port) {
        this.port = port;
    }

    @SneakyThrows
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            new Thread(this::processClients).start();
            log.info("Server is running");
            //handle incoming connections
            while (true) {
                Socket clientSocket = serverSocket.accept();
                log.info("Incoming connection");
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                clientHandler.initConnection();
                ClientStorage.addClient(clientHandler);
                log.info("Connection established");
            }
        } finally {
            log.info("Client closed");
            ClientStorage.getClients().forEach(ClientHandler::close);
        }
    }

    private void processClients() {
        while (true) {
            for (ClientHandler clientHandler : ClientStorage.getClients()) {
                clientHandler.process();
            }
        }
    }

    public void sendBroadcastMessage(String message) {
        ClientStorage.getClients().forEach(client -> client.sendMsg(message));
    }

    public void removeClient(ClientHandler client) {
        ClientStorage.removeClient(client);
    }
}
