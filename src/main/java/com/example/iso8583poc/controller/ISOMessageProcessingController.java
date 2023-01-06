package com.example.iso8583poc.controller;

import com.example.iso8583poc.domain.completion.CommanderCompletionRequest;
import com.example.iso8583poc.domain.iso8583.ISO8583MessageInformationRequest;
import com.example.iso8583poc.domain.iso8583.ISO8583MessageResponse;
import com.example.iso8583poc.domain.iso8583.ParsedISO8583MessageInformation;
import com.example.iso8583poc.domain.preauth.CommanderPreAuthRequest;
import com.example.iso8583poc.service.iso8583.ISO8583GeneratorService;
import com.example.iso8583poc.service.iso8583.ISO8583ParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/iso")
public class ISOMessageProcessingController {

    private final ISO8583ParserService parserService;
    private final ISO8583GeneratorService generatorService;

    @GetMapping("parse")
    public ResponseEntity<ParsedISO8583MessageInformation> getParsedISOHexMessage(@RequestParam String message) {
        try {
            return ResponseEntity.ok(parserService.parseHexMessage(message));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.noContent().build();
    }

    @PostMapping("generate")
    public ResponseEntity<ISO8583MessageResponse> getParsedISOHexMessage(@RequestBody ISO8583MessageInformationRequest request) {
        try {
            var response = generatorService.generateISO8583Message(request);
            return ResponseEntity.ok(new ISO8583MessageResponse(response));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.noContent().build();
    }

    @PostMapping("generate-pre-ath-request")
    public ResponseEntity<ISO8583MessageResponse> generatePreAuthRequestISO8583Message(@RequestBody CommanderPreAuthRequest preAuthRequest) {
        try {
            var response = generatorService.generatePreAuthRequestISO8583Message(preAuthRequest);
            return ResponseEntity.ok(new ISO8583MessageResponse(response));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("generate-completion-request")
    public ResponseEntity<ISO8583MessageResponse> generateCompletionRequestISO8583Message(@RequestBody CommanderCompletionRequest completionRequest) {
        try {
            var response = generatorService.generateCompletionRequestISO8583Message(completionRequest);
            return ResponseEntity.ok(new ISO8583MessageResponse(response));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("generate-echo-test-request")
    public ResponseEntity<ISO8583MessageResponse> generateEchoTestRequestISO8583Message() {
        try {
            var response = generatorService.generateEchoTestRequestISO8583Message();
            return ResponseEntity.ok(new ISO8583MessageResponse(response));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.noContent().build();
    }
}
