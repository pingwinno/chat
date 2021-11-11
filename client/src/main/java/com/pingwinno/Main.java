package com.pingwinno;

import lombok.extern.slf4j.Slf4j;
import org.fusesource.jansi.AnsiConsole;

import java.util.Scanner;

@Slf4j
public class Main {

    public static void main(String[] args) {
        AnsiConsole.systemInstall();
        Scanner in = new Scanner(System.in);
        //get username
        log.debug("waiting for username...");
        System.out.println("Set username");
        String userName = "";
        boolean isUsernameEmpty = true;
        while (isUsernameEmpty) {
            userName = in.nextLine();
            if (userName.trim().isEmpty()) {
                System.out.println("Username is empty. Please enter username");
                continue;
            }
            isUsernameEmpty = false;
        }
        new Client(args[0], Integer.parseInt(args[1]), userName).start();
    }
}
