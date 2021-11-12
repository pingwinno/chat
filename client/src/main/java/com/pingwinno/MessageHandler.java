package com.pingwinno;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.fusesource.jansi.Ansi.Color;
import static org.fusesource.jansi.Ansi.ansi;

@Slf4j
public class MessageHandler {
    private static final String MESSAGE_TEMPLATE = "[%s] %s:%s";
    private final ObjectMapper mapper = JsonMapper.builder()
            .findAndAddModules()
            .build();
    private final String username;
    private final Color color;


    public MessageHandler(String username, Color color) {
        this.color = color;
        this.username = username;
    }

    @SneakyThrows
    public String makeMessage(String message, LocalDateTime time) {
        return mapper.writeValueAsString(
                MessageModel.builder()
                        .message(message)
                        .color(color)
                        .time(time)
                        .username(username)
                        .build());
    }

    @SneakyThrows
    public String renderMessage(String message) {
        var incomingMessage = mapper.readValue(message, MessageModel.class);
        return String.format(MESSAGE_TEMPLATE, incomingMessage.getTime().format(DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm:ss")),
                ansi().fg(incomingMessage.getColor()).a(incomingMessage.getUsername()).reset(), incomingMessage.getMessage());
    }
}
