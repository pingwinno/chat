package com.pingwinno;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.fusesource.jansi.Ansi;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.Scanner;

@Slf4j
public class Client {
    private final String serverHost;
    private final int serverPort;
    private final MessageHandler messageHandler;
    private Socket clientSocket;
    private Scanner inputMessagesStream;
    private PrintWriter outputMessagesStream;

    public Client(String serverHost, int serverPort, String username) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.messageHandler = new MessageHandler(username, Ansi.Color.values()[new Random().nextInt(0, Ansi.Color.values().length)]);
    }

    @SneakyThrows
    public void start() {
        new Thread(this::handleConsoleInput).start();
        log.info("initialize client");
        clientSocket = new Socket(serverHost, serverPort);
        inputMessagesStream = new Scanner(clientSocket.getInputStream());
        outputMessagesStream = new PrintWriter(clientSocket.getOutputStream());
        new Thread(() -> {
            log.debug("start incoming message loop");
            //start incoming message loop
            while (clientSocket != null) {
                if (inputMessagesStream.hasNext()) {
                    String inMes = inputMessagesStream.nextLine();
                    System.out.println(messageHandler.renderMessage(inMes));
                }
            }
        }).start();
    }

    public void sendMessage(String message) {
        log.debug("sending message");
        outputMessagesStream.println(message);
        outputMessagesStream.flush();
    }

    @SneakyThrows
    private void handleConsoleInput() {
        Scanner in = new Scanner(System.in);
        //parse user input loop
        log.debug("Connection established");
        System.out.println("Waiting for input...");
        while (true) {
            String message = in.nextLine();
            if (message == null || message.trim().isEmpty()) {
                System.out.println("Empty message not allowed");
                continue;
            }
            this.sendMessage(messageHandler.makeMessage(message, LocalDateTime.now()));
            if (message.equals("!exit")) {
                log.debug("Closing client");
                this.close();
                System.exit(0);
            }
        }
    }


    public void close() throws IOException {
        clientSocket.close();
        outputMessagesStream.close();
        inputMessagesStream.close();
    }
}