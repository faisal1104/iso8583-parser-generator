package com.example.iso8583poc.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ISo8583DataResponse {
    private String message;
}
