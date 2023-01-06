package com.example.iso8583poc.service;

import com.example.iso8583poc.domain.ServerDataRequest;
import com.example.iso8583poc.domain.ServerDataResponse;
import com.example.iso8583poc.domain.iso8583.MessageTypeIndicator;
import com.example.iso8583poc.service.completion.CompletionRequestHandler;
import com.example.iso8583poc.service.echotest.EchoTestRequestHandler;
import com.example.iso8583poc.service.iso8583.ISO8583ParserService;
import com.example.iso8583poc.service.preauth.PreAuthRequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jpos.iso.ISOException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SocketDataRequestHandler {

    private final ISO8583ParserService iso8583ParserService;
    private final PreAuthRequestHandler preAuthRequestHandler;
    private final CompletionRequestHandler completionRequestHandler;
    private final EchoTestRequestHandler echoTestRequestHandler;
    private final ObjectMapper objectMapper;

    public String handleRequest(String messageText) throws JsonProcessingException, ISOException {
        ServerDataRequest request = objectMapper.readValue(messageText, ServerDataRequest.class);

        var parsedISO8583MessageInformation = iso8583ParserService.parseHexMessage(request.getRequestMessage());
        var messageTypeIndicator = parsedISO8583MessageInformation.getMessageTypeIndicator();
        var isoDataElementValueMap = parsedISO8583MessageInformation.getIsoDataElementValueMap();

        String response = StringUtils.EMPTY;

        if (messageTypeIndicator.equals(MessageTypeIndicator.PRE_AUTH_REQUEST)) {
            response = preAuthRequestHandler.handlePreAuthRequest(isoDataElementValueMap);
        } else if (messageTypeIndicator.equals(MessageTypeIndicator.COMPLETION_REQUEST)) {
            response = completionRequestHandler.handleCompletionRequest(isoDataElementValueMap);
        } else if (messageTypeIndicator.equals(MessageTypeIndicator.NETWORK_MANAGEMENT_REQUEST)) {
            response = echoTestRequestHandler.handleRequest(isoDataElementValueMap);
        }

        return objectMapper.writeValueAsString(new ServerDataResponse().setResponseMessage(response));
    }
}
