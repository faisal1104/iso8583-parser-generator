package com.example.iso8583poc.domain.preauth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PreAuthResponseType {
    AUTHORIZED("00"),
    UNAUTHORIZED("10");

    private final String code;

    public PreAuthResponseType getPreAuthResponseTypeByCode(String code) {
        for (var preAuthResponse : PreAuthResponseType.values()) {
            if (preAuthResponse.getCode().equals(code))
                return preAuthResponse;
        }

        return UNAUTHORIZED;
    }
}
