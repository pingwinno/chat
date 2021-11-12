package com.pingwinno;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessagesBufferTest {

    @Test
    void should_returnMessagesList_when_addAndGetMessagesList() {
        var expectedMessage = "message";
        MessagesBuffer.addMessage(expectedMessage);
        assertEquals(expectedMessage, MessagesBuffer.getMessages().get(0));
    }

    @Test
    void should_returnTwentyLastMessages_when_addMoreThanTwentyMessages() {
        createMessages(30).forEach(MessagesBuffer::addMessage);
        var expectedFirstMessage = "message10";
        var expectedLastMessage = "message29";
        var expectedBufferSize = 20;
        assertEquals(expectedFirstMessage, MessagesBuffer.getMessages().get(0));
        assertEquals(expectedLastMessage, MessagesBuffer.getMessages().get(19));
        assertEquals(expectedBufferSize, MessagesBuffer.getMessages().size());
    }

    //возможно так писать не стоит, но на 10й строчке мне надоело менять текст сообщений руками
    List<String> createMessages(int numberOfMessages) {
        var list = new ArrayList<String>();
        for (int i = 0; i < numberOfMessages; i++) {
            list.add("message" + i);
        }
        return list;
    }
}