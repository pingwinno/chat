package com.pingwinno;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

@Slf4j
public class ClientHandler implements Runnable {
    private final Server server;
    private final PrintWriter outMessagesStream;
    private final Scanner inMessagesStream;
    private final Socket socket;

    public ClientHandler(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        this.outMessagesStream = new PrintWriter(socket.getOutputStream());
        this.inMessagesStream = new Scanner(socket.getInputStream());
    }

    @Override
    public void run() {
        //send stored message to client
        for (String message : MessagesBuffer.getMessages()) {
            this.sendMsg(message);
        }
        //handle incoming message
        while (true) {
            if (inMessagesStream.hasNext()) {
                String clientMessage = inMessagesStream.nextLine();
                if (clientMessage.contains("!exit")) {
                    this.close();
                    log.info("Client disconnect");
                    break;
                }
                //add message to storage
                MessagesBuffer.addMessage(clientMessage);
                log.debug(clientMessage);
                server.sendBroadcastMessage(clientMessage);
            }
        }
        log.info("connection closed");
    }

    public void sendMsg(String msg) {
        try {
            outMessagesStream.println(msg);
            outMessagesStream.flush();
        } catch (Exception e) {
            log.error("Can't send message");
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            outMessagesStream.close();
            inMessagesStream.close();
            socket.close();
            server.removeClient(this);
        } catch (IOException e) {
            log.error("Client handler closing error");
            e.printStackTrace();
        }
    }

}

