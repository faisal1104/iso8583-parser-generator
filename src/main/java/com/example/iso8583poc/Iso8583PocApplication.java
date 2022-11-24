package com.example.iso8583poc;

import com.example.iso8583poc.service.ISO8583GeneratorService;
import com.example.iso8583poc.service.ISO8583ParserService;
import org.jpos.iso.ISOException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Iso8583PocApplication {

    private static String iso8583Message = "080020200000008000000000000000013239313130303031";
    private static String iso8583Message2 = "0800A02000000080001004000000000000000000000000013239313130303031001054455354204D455353470301";

    public static void main(String[] args) throws ISOException {
        SpringApplication.run(Iso8583PocApplication.class, args);
        ISO8583ParserService parserService = new ISO8583ParserService();
        parserService.parseMessage(iso8583Message);
        System.out.println(" \n --------------------------------------- \n");

        ISO8583GeneratorService generatorService = new ISO8583GeneratorService();
        var generatedISO8583Message = generatorService.generateISO8583Message();
        System.out.println("New generated ISO message: " + generatedISO8583Message + "\n=============");
        parserService.parseMessage(generatedISO8583Message);
    }

}
