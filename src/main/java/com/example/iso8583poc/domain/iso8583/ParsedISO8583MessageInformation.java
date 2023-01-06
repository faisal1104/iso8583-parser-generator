package com.example.iso8583poc.domain.iso8583;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ParsedISO8583MessageInformation {
    private MessageTypeIndicator messageTypeIndicator; //MTI
    private String bitMap;
    private Map<ISO8583DataElementType, String> isoDataElementValueMap;
}
