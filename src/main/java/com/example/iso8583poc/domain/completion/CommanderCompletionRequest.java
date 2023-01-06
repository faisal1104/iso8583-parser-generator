package com.example.iso8583poc.domain.completion;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalTime;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class CommanderCompletionRequest {
    private String cardNumber;
    private String processingCode = "000000";
    private String transactionValue;
    private LocalTime transactionLocalHour;
    private String merchantIdCode;
    private String terminalId;
    private String currencyCode;
    private String posEntryMode;
    private String acquirerIdCode;
    private String systemTraceNumber;
    private String posConditionCode = "06";
    private String fleetDataTwo;
}
