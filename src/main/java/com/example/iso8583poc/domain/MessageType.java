package com.example.iso8583poc.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageType {
    PRE_AUTH_REQUEST("0100"),
    PRE_AUTH_RESPONSE("0110"),
    COMPILATION_REQUEST("0220"),
    COMPILATION_RESPONSE("0230"),
    REVERSAL_REQUEST("0400"),
    REVERSAL_RESPONSE("0410"),


    INVALID_MESSAGE_TYPE("0000"),
    ;

    private final String mti;

    public static MessageType getMessageTypeByMti(String mti) {
        for (var messageType : MessageType.values()) {
            if (messageType.getMti().equals(mti))
                return messageType;
        }
        return MessageType.INVALID_MESSAGE_TYPE;
    }
}
