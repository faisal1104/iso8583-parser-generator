package com.example.iso8583poc.service.preauth;

import com.example.iso8583poc.domain.iso8583.MessageTypeIndicator;
import com.example.iso8583poc.domain.preauth.CommanderPreAuthRequest;
import com.example.iso8583poc.domain.preauth.CommanderPreAuthResponse;
import com.example.iso8583poc.domain.preauth.PreAuthResponseType;
import com.example.iso8583poc.domain.preauth.RuleValidationResponse;
import com.example.iso8583poc.feign.AuthClient;
import com.example.iso8583poc.feign.FleetClient;
import com.example.iso8583poc.service.iso8583.ISO8583GeneratorService;
import com.example.iso8583poc.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PreAuthResponseService {
    private static final String INVALID_PRODUCT_CODE = "99";

    private final FleetClient fleetClient;
    private final AuthClient authClient;
    private final ISO8583GeneratorService iso8583GeneratorService;

    @Value("${dummy.username}")
    private String username;

    @Value("${dummy.password}")
    private String password;

    public String generateResponseMessageAs8583(CommanderPreAuthRequest request) {
        CommanderPreAuthResponse response = new CommanderPreAuthResponse();

        //region validate pre-auth request
        var validationResponse = this.validatePreAuthRequest(request);
        if (validationResponse.getIsValidate()) {
            response
                .setRestrictedProductCode(Util.formattedFuelCode(validationResponse.getFuelCode()))
                .setResponseCode(PreAuthResponseType.AUTHORIZED.getCode());
        } else
            response
                .setRestrictedProductCode(Util.formattedFuelCode(INVALID_PRODUCT_CODE)).setResponseCode(PreAuthResponseType.UNAUTHORIZED.getCode());
        //end region validate

        response
            .setTransactionValue(request.getTransactionValue())
            .setSystemTraceNumber(request.getSystemTraceNumber())
            .setRetrievalReferenceNumber("123456789000")
            .setCardNumber(request.getCardNumber())
            .setTerminalId(request.getTerminalId())
            .setProcessingCode(request.getProcessingCode())

            .setCurrencyCode(request.getCurrencyCode());

        System.out.println(response);
        String preAuthResponseInISO8583 = StringUtils.EMPTY;
        try {
            preAuthResponseInISO8583 = iso8583GeneratorService.generateCommanderResponseInISO8583Format(MessageTypeIndicator.PRE_AUTH_RESPONSE, response);
        } catch (Exception e) {
            log.error("Failed to generatePreAuthResponse in ISO8583 format. {}", e.getMessage());
        }
        return preAuthResponseInISO8583;
    }


    private RuleValidationResponse validatePreAuthRequest(CommanderPreAuthRequest preAuthRequest) {
        var token = this.getCPayToken();
        if (StringUtils.isNotBlank(token)) {
            try {
                var responseResponseEntity = fleetClient.validateRuleOfDriver(token, preAuthRequest);
                if (responseResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
                    return responseResponseEntity.getBody();
                }
            } catch (Exception e) {
                log.error("Failed to validate rule from FLEET service. {}", e.getMessage());
            }
        }
        return new RuleValidationResponse().setIsValidate(false);
    }

    private String getCPayToken() {
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", username);
        loginRequest.put("password", password);
        try {
            var loginResponse = authClient.getToken(loginRequest).getBody();
            if (loginResponse.containsKey("access_token"))
                return "Bearer " + loginResponse.get("access_token");
        } catch (Exception e) {
            log.error("Failed to get token from AUTH service. {}", e.getMessage());
        }
        return StringUtils.EMPTY;
    }
}
