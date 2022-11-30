package com.example.iso8583poc.util;

import com.example.iso8583poc.domain.ISO8583DataElement;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class Util {

    public static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyMMdd");
    public static final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HHmmss");

    public static final Map<Integer, ISO8583DataElement> iso8583DataElementMap =
        new HashMap<>() {
            {
                for (var iso : ISO8583DataElement.values()) {
                    put(iso.getIndexNumber(), iso);
                }
            }
        };

    public static OffsetDateTime generateDateTime(String date, String time, String timezone) {
        var localDate = LocalDate.parse(date, dateFormat);
        var localTime = LocalTime.parse(time, timeFormat);
        return OffsetDateTime.of(localDate, localTime, getZoneOffsetByTimezone(timezone));
    }

    public static ZoneOffset getZoneOffsetByTimezone(String timezone) {
        ZoneOffset zoneOffset = ZoneOffset.UTC;
        if (Objects.nonNull(timezone)) {
            try {
                zoneOffset = ZoneOffset.of(timezone);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return zoneOffset;
    }


    public static String decimalToTwoBytesBinary(int decimalNumber){
        var decimalToBinary = Integer.toBinaryString(decimalNumber);
        return String.format("%016d", Integer.parseInt(decimalToBinary));
    }

    public static Integer binaryToDecimal(String binaryNumber){
        return Integer.parseInt(binaryNumber, 2);
    }
}
