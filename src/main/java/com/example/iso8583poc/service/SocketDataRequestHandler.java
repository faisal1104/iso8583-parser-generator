package com.example.iso8583poc.service;

import com.example.iso8583poc.domain.ISo8583DataRequest;
import com.example.iso8583poc.domain.ISo8583DataResponse;
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

    private final ISO8583GeneratorService iso8583GeneratorService;
    private final ISO8583ParserService iso8583ParserService;
    private final ObjectMapper objectMapper;

    public String handleRequest(String messageText) throws JsonProcessingException, ISOException {
        ISo8583DataRequest request = objectMapper.readValue(messageText, ISo8583DataRequest.class);

        iso8583ParserService.parseMessage(request.getMessage());

        return objectMapper.writeValueAsString(new ISo8583DataResponse().setMessage("Request Accepted"));
    }
}
