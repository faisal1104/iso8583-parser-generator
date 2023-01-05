package com.example.iso8583poc.util;

import com.example.iso8583poc.domain.iso8583.ISO8583DataElementType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class Util {

    public static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MMdd");
    public static final DateTimeFormatter ISO8583_TIME_FORMAT = DateTimeFormatter.ofPattern("HHmmss");

    public static final Map<Integer, ISO8583DataElementType> iso8583DataElementMap =
        new HashMap<>() {
            {
                for (var iso : ISO8583DataElementType.values()) {
                    put(iso.getIndexNumber(), iso);
                }
            }
        };

    public static String decimalToTwoByteHex(int decimalNumber) {
        var hex = Integer.toHexString(decimalNumber);
        // For example : 34 -> 0022
        return StringUtils.leftPad(hex, 4, '0');
    }

    public static Integer hexToDecimal(String hexNumber) {
        return Integer.parseInt(hexNumber, 16);
    }


/*    public static Integer binaryToDecimal(String binaryNumber){
        return Integer.parseInt(binaryNumber, 2);
    }

    public static String decimalToTwoBytesBinary(int decimalNumber){
        var decimalToBinary = Integer.toBinaryString(decimalNumber);
        return String.format("%016d", Integer.parseInt(decimalToBinary));
    }*/

    public static String formattedFuelCode(String fuelCode) {
        return StringUtils.rightPad(fuelCode, 10, '0');
    }

    public static LocalTime getLocalTimeFromString(String localTime) {
        return LocalTime.parse(localTime, ISO8583_TIME_FORMAT);
    }
}
