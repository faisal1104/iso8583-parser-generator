package com.example.iso8583poc.domain.completion;

import com.example.iso8583poc.annotation.ISO8583DataElementInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class CommanderCompletionResponse {

    @ISO8583DataElementInfo(fieldNumber = 2)
    private String cardNumber;

    @ISO8583DataElementInfo(fieldNumber = 3)
    private String processingCode = "000000";

    @ISO8583DataElementInfo(fieldNumber = 11)
    private String systemTraceNumber;

    @ISO8583DataElementInfo(fieldNumber = 39)
    private String responseCode;

    @ISO8583DataElementInfo(fieldNumber = 41)
    private String terminalId;

    @ISO8583DataElementInfo(fieldNumber = 49)
    private String currencyCode;
}
