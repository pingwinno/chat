package com.pingwinno;

public class Main {

    public static void main(String[] args) {
        new Server(Integer.parseInt(args[0])).start();
    }
}
