package com.example.iso8583poc.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "auth-service", url = "${auth.server.url}")
public interface AuthClient {
    @PostMapping(value = "oauth/login")
    ResponseEntity<Map<String, String>> getToken(@RequestBody Map<String, String> request);
}
