package com.example.iso8583poc.controller;

import com.example.iso8583poc.util.Util;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/iso")
public class HomeController {


    public OffsetDateTime getOffsetDateTimeFromDateAndTime(
        @RequestParam(defaultValue = "Z") String timeZone,
        @RequestParam String date,
        @RequestParam String time) {
        return Util.generateDateTime(date, time, timeZone);
    }
}
