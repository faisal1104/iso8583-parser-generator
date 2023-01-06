package com.example.iso8583poc.service.completion;

import com.example.iso8583poc.domain.completion.CommanderCompletionRequest;
import com.example.iso8583poc.domain.completion.CommanderCompletionResponse;
import com.example.iso8583poc.domain.iso8583.MessageTypeIndicator;
import com.example.iso8583poc.domain.preauth.PreAuthResponseType;
import com.example.iso8583poc.service.iso8583.ISO8583GeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CompletionResponseService {

    private final ISO8583GeneratorService iso8583GeneratorService;

    public String generateResponseMessageAs8583(CommanderCompletionRequest request) {
        var response = new CommanderCompletionResponse();

        response
            .setSystemTraceNumber(request.getSystemTraceNumber())
            .setCardNumber(request.getCardNumber())
            .setTerminalId(request.getTerminalId())
            .setResponseCode(PreAuthResponseType.AUTHORIZED.getCode())
            .setProcessingCode(request.getProcessingCode())
            .setCurrencyCode(request.getCurrencyCode());

        System.out.println(response);
        String preAuthResponseInISO8583 = StringUtils.EMPTY;
        try {
            preAuthResponseInISO8583 = iso8583GeneratorService.generateCommanderResponseInISO8583Format(MessageTypeIndicator.COMPLETION_RESPONSE, response);
        } catch (Exception e) {
            log.error("Failed to generatePreAuthResponse in ISO8583 format. {}", e.getMessage());
        }
        return preAuthResponseInISO8583;
    }
}
