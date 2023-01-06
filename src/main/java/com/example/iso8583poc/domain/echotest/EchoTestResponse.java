package com.example.iso8583poc.domain.echotest;

import com.example.iso8583poc.annotation.ISO8583DataElementInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class EchoTestResponse {
    @ISO8583DataElementInfo(fieldNumber = 7)
    private String transmissionDateAndHour;

    @ISO8583DataElementInfo(fieldNumber = 11)
    private String systemTraceNumber;

    @ISO8583DataElementInfo(fieldNumber = 60)
    private String applicationSoftVersion;

    @ISO8583DataElementInfo(fieldNumber = 70)
    private String networkMessageType;
}
