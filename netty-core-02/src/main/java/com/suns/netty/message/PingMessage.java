package com.suns.netty.message;

import lombok.Data;

@Data
public class PingMessage extends Message {

    private String source;

    public PingMessage() {
    }

    public PingMessage(String source) {
        this.source = source;
    }

    @Override
    public int getMessageType() {
        return MessageType.PING_MESSAGE;
    }
}
