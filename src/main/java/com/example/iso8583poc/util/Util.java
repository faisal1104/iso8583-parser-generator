package com.example.iso8583poc.util;

import com.example.iso8583poc.domain.ISO8583DataElement;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
public class Util {

    public static final Map<Integer, ISO8583DataElement> iso8583DataElementMap =
        new HashMap<>() {
            {
                for (var iso: ISO8583DataElement.values()) {
                    put(iso.getIndexNumber(), iso);
                }
            }
        };
}
