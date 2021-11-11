package com.pingwinno;

import java.util.LinkedList;

public class MessagesBuffer {
    private static final int BUFFER_SIZE = 20;
    private static final LinkedList<String> buffer = new LinkedList<>();

    synchronized public static void addMessage(String message) {
        if (buffer.size() > BUFFER_SIZE) buffer.removeFirst();
        buffer.add(message);
    }

    synchronized public static LinkedList<String> getMessages() {
        return new LinkedList<>(buffer);
    }
}
