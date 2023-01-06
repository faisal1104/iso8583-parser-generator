package com.example.iso8583poc.domain.iso8583;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class ISO8583MessageInformationRequest {
    private MessageTypeIndicator messageTypeIndicator; //MTI
    private Map<ISO8583DataElementType, String> isoDataElementValueMap;

    public ISO8583MessageInformationRequest(MessageTypeIndicator messageTypeIndicator, Map<ISO8583DataElementType, String> isoDataElementValueMap) {
        this.messageTypeIndicator = messageTypeIndicator;
        this.isoDataElementValueMap = isoDataElementValueMap;
    }
}
