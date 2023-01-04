package com.example.iso8583poc.domain.preauth;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class RuleValidationResponse {
    private Boolean isValidate;
    private String fuelCode;
}