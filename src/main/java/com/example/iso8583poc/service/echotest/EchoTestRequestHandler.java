package com.example.iso8583poc.service.echotest;

import com.example.iso8583poc.domain.echotest.EchoTestRequest;
import com.example.iso8583poc.domain.iso8583.ISO8583DataElementType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class EchoTestRequestHandler {

    private final EchoTestResponseService echoTestResponseService;

    public String handleRequest(Map<ISO8583DataElementType, String> isoDataElementValueMap) {

        var echoTestRequest = new EchoTestRequest();

        for (var map : isoDataElementValueMap.entrySet()) {
            var iso8583DataElementType = map.getKey();
            var value = map.getValue();

            switch (iso8583DataElementType) {
                case TRANSACTION_DATE_TIME -> echoTestRequest.setTransmissionDateAndHour(value);
                case SYSTEM_TRACE_NUMBER -> echoTestRequest.setSystemTraceNumber(value);
                case APPLICATION_SOFT_VERSION -> echoTestRequest.setApplicationSoftVersion(value);
                case NETWORK_MANAGEMENT_INFORMATION_CODE -> echoTestRequest.setNetworkMessageType(value);
            }
        }

        System.out.println(echoTestRequest);
        return echoTestResponseService.generateResponseMessageAs8583(echoTestRequest);
    }
}
