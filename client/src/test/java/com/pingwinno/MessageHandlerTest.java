package com.pingwinno;

import org.fusesource.jansi.Ansi;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageHandlerTest {
    private static final String JSON_MESSAGE = "{\"time\":[2021,11,10,9,13,12],\"username\":\"user\",\"message\":\"message\",\"color\":\"GREEN\"}";
    private static final LocalDateTime expectedTime = LocalDateTime.of(2021, 11, 10, 9, 13, 12);
    private static final String EXPECTED_MESSAGE = "[10 Nov 2021 09:13:12] " + Ansi.ansi().fg(Ansi.Color.GREEN).a("user").reset() + ":message";
    private final MessageHandler messageHandler = new MessageHandler("user", Ansi.Color.GREEN);

    @Test
    void should_returnJsonMessage_when_passMessageText() {
        assertEquals(JSON_MESSAGE, messageHandler.makeMessage("message", expectedTime));
    }

    @Test
    void should_renderMessage_when_passJson() {
        assertEquals(EXPECTED_MESSAGE, messageHandler.renderMessage(JSON_MESSAGE));
    }
}