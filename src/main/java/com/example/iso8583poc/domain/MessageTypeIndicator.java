package com.example.iso8583poc.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageTypeIndicator {
    PRE_AUTH_REQUEST("0100"),
    PRE_AUTH_RESPONSE("0110"),
    COMPLETION_REQUEST("0220"),
    COMPLETION_RESPONSE("0230"),
    REVERSAL_REQUEST("0400"),
    REVERSAL_RESPONSE("0410"),
    NETWORK_MANAGEMENT_REQUEST("0800"),
    NETWORK_MANAGEMENT_RESPONSE("0810"),


    INVALID_MESSAGE_TYPE("0000"),
    ;

    private final String code;

    public static MessageTypeIndicator getMessageTypeIndicatorByCode(String code) {
        for (var messageType : MessageTypeIndicator.values()) {
            if (messageType.getCode().equals(code))
                return messageType;
        }
        return MessageTypeIndicator.INVALID_MESSAGE_TYPE;
    }
}
