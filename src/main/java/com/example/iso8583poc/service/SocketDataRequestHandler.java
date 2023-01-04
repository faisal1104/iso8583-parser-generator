package com.example.iso8583poc.service;

import com.example.iso8583poc.domain.ServerDataRequest;
import com.example.iso8583poc.domain.ServerDataResponse;
import com.example.iso8583poc.service.iso8583.ISO8583ParserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jpos.iso.ISOException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SocketDataRequestHandler {

    private final ISO8583ParserService iso8583ParserService;
    private final ObjectMapper objectMapper;

    public String handleRequest(String messageText) throws JsonProcessingException, ISOException {
        ServerDataRequest request = objectMapper.readValue(messageText, ServerDataRequest.class);

        var preAuthResponse = iso8583ParserService.parseHexMessageAndGetCommanderPreAuthRequest(request.getMessage());
        return objectMapper.writeValueAsString(new ServerDataResponse().setMessage(preAuthResponse));
    }
}
