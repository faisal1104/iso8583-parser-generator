package com.example.iso8583poc.domain.echotest;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class EchoTestRequest {
    private String transmissionDateAndHour;
    private String systemTraceNumber;
    private String applicationSoftVersion;
    private String networkMessageType;
}
