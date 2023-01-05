package com.example.iso8583poc.controller;

import com.example.iso8583poc.service.iso8583.ISO8583GeneratorService;
import com.example.iso8583poc.service.iso8583.ISO8583ParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/iso")
public class ISOMessageProcessingController {

    private final ISO8583ParserService parserService;
    private final ISO8583GeneratorService generatorService;

    @GetMapping("parse")
    public ResponseEntity<Map<String, String>> getParsedISOHexMessage(@RequestParam String message, @RequestParam(required = false) boolean isHeaderExist) {
        try {
            return ResponseEntity.ok(parserService.parseAndPrintHexMessage(message, isHeaderExist));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping("generate-pre-ath-request")
    public ResponseEntity<String> generatePreAuthRequestISO8583Message() {
        try {
            return ResponseEntity.ok(generatorService.generatePreAuthRequestISO8583Message());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.noContent().build();
    }
}
