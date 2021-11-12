package com.pingwinno;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;

@Slf4j
public class ClientHandler {
    private final Server server;
    private final PrintStream outMessagesStream;
    private final BufferedReader inMessagesStream;
    private final Socket socket;

    public ClientHandler(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        this.outMessagesStream = new PrintStream(socket.getOutputStream());
        this.inMessagesStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void process() {
        //handle incoming message
        try {
            if (inMessagesStream.ready()) {
                var line = inMessagesStream.readLine();
                if (line.contains("!exit")) {
                    this.close();
                    log.info("Client disconnect");
                }
                //add message to storage
                MessagesBuffer.addMessage(line);
                log.trace(line);
                server.sendBroadcastMessage(line);
            }
        } catch (IOException e) {
            close();
        }
    }

    public void initConnection() {
        //send stored message to client
        for (String message : MessagesBuffer.getMessages()) {
            this.sendMsg(message);
        }
    }

    public void sendMsg(String msg) {
        try {
            outMessagesStream.println(msg);
        } catch (Exception e) {
            log.error("Can't send message");
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }
    }

}

