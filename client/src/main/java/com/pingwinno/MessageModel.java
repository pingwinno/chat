package com.pingwinno;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import org.fusesource.jansi.Ansi;

import java.time.LocalDateTime;

@Builder
@Jacksonized
@Getter
public class MessageModel {
    private LocalDateTime time;
    private String username;
    private String message;
    private Ansi.Color color;
}
