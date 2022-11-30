package com.example.iso8583poc.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Accessors(chain = true)
public class ISo8583DataRequest {
    private String message;
}
