package com.example.iso8583poc.service.echotest;

import com.example.iso8583poc.domain.echotest.EchoTestRequest;
import com.example.iso8583poc.domain.echotest.EchoTestResponse;
import com.example.iso8583poc.domain.iso8583.MessageTypeIndicator;
import com.example.iso8583poc.service.iso8583.ISO8583GeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class EchoTestResponseService {

    private final ISO8583GeneratorService iso8583GeneratorService;

    public String generateResponseMessageAs8583(EchoTestRequest echoTestRequest) {
        var response = new EchoTestResponse();
        BeanUtils.copyProperties(echoTestRequest, response);
        System.out.println(response);
        String preAuthResponseInISO8583 = StringUtils.EMPTY;
        try {
            preAuthResponseInISO8583 = iso8583GeneratorService.generateCommanderResponseInISO8583Format(MessageTypeIndicator.NETWORK_MANAGEMENT_RESPONSE, response);
        } catch (Exception e) {
            log.error("Failed to generatePreAuthResponse in ISO8583 format. {}", e.getMessage());
        }
        return preAuthResponseInISO8583;
    }
}
