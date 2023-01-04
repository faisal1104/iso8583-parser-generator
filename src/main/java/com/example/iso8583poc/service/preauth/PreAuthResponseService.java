package com.example.iso8583poc.service.preauth;

import com.example.iso8583poc.annotation.ISO8583DataElementInfo;
import com.example.iso8583poc.custom_iso8583_packager.CPayISO8583Packager;
import com.example.iso8583poc.domain.iso8583.MessageTypeIndicator;
import com.example.iso8583poc.domain.preauth.CommanderPreAuthRequest;
import com.example.iso8583poc.domain.preauth.CommanderPreAuthResponse;
import com.example.iso8583poc.domain.preauth.PreAuthResponseType;
import com.example.iso8583poc.domain.preauth.RuleValidationResponse;
import com.example.iso8583poc.feign.AuthClient;
import com.example.iso8583poc.feign.FleetClient;
import com.example.iso8583poc.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class PreAuthResponseService {
    private static final CPayISO8583Packager packager = new CPayISO8583Packager();
    private static final String INVALID_PRODUCT_CODE = "99";
    private final FleetClient fleetClient;
    private final AuthClient authClient;

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
            .setCardNumber(request.getCardNumber())
            .setTerminalId(request.getTerminalId())
            .setProcessingCode(request.getProcessingCode())

            .setCurrencyCodeNumber(request.getCurrencyCodeNumber());

        System.out.println(response);
        String preAuthResponseInISO8583 = StringUtils.EMPTY;
        try {
            preAuthResponseInISO8583 = this.generatePreAuthResponse(response);
        } catch (Exception e) {
            log.error("Failed to generatePreAuthResponse in ISO8583 format. {}", e.getMessage());
        }
        return preAuthResponseInISO8583;
    }

    private String generatePreAuthResponse(CommanderPreAuthResponse response) throws IllegalAccessException, ISOException {
        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(packager);
        isoMsg.setMTI(MessageTypeIndicator.PRE_AUTH_RESPONSE.getCode());

        for (Field field : response.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(ISO8583DataElementInfo.class)) {
                ISO8583DataElementInfo iso8583DataElementInfo = field.getAnnotation(ISO8583DataElementInfo.class);
                int fieldNumber = iso8583DataElementInfo.fieldNumber();
                if (Objects.nonNull(field.get(response))) {
                    String value = String.valueOf(field.get(response));
                    isoMsg.set(fieldNumber, value);
                }
            }
        }
        var result = isoMsg.pack();
        return Hex.encodeHexString(result);
    }


    private RuleValidationResponse validatePreAuthRequest(CommanderPreAuthRequest preAuthRequest) {
        var responseResponseEntity = fleetClient.validateRuleOfDriver(this.getCPayToken(), preAuthRequest);
        if (responseResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return responseResponseEntity.getBody();
        }

        return new RuleValidationResponse().setIsValidate(false);
    }

    private String getCPayToken() {
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", "faisal_company_2022c@yopmail.com");
        loginRequest.put("password", "Aa&12345");
        var loginResponse = authClient.getToken(loginRequest).getBody();
        if (loginResponse.containsKey("access_token"))
            return "Bearer " + loginResponse.get("access_token");
        else
            return StringUtils.EMPTY;
    }
}
