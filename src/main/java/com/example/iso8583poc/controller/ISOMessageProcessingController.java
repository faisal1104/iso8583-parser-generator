package com.example.iso8583poc.controller;

import com.example.iso8583poc.service.iso8583.ISO8583ParserService;
import com.example.iso8583poc.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/iso")
public class ISOMessageProcessingController {

    private final ISO8583ParserService parserService;

    @GetMapping("parse/{msg}")
    public ResponseEntity<Map<String, String>> parseAndPrintIsoMessage(@PathVariable String msg) {
        try {
            return ResponseEntity.ok(parserService.parseAndPrintHexMessage(msg));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.noContent().build();
    }
}
