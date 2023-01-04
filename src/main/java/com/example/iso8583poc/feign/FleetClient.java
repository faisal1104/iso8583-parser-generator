package com.example.iso8583poc.feign;


import com.example.iso8583poc.domain.preauth.CommanderPreAuthRequest;
import com.example.iso8583poc.domain.preauth.RuleValidationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "fleet-service", url = "http://localhost:5030/fleet")
public interface FleetClient {

    @PostMapping("validate-fleet-rule")
    ResponseEntity<RuleValidationResponse> validateRuleOfDriver(
        @RequestHeader("Authorization") String token,
        @RequestBody CommanderPreAuthRequest preAuthRequest);
}
