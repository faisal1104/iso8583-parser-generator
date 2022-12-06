package com.example.iso8583poc;

import com.example.iso8583poc.server.NettyWebServer;
import com.example.iso8583poc.service.ISO8583GeneratorService;
import com.example.iso8583poc.service.ISO8583ParserService;
import org.jpos.iso.ISOException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Iso8583PocApplication {

    private static String iso8583Message = "0800202000000080000000000000000129110001";
    private static String iso8583Message2 = "0800A02000000080001004000000000000000000000000013239313130303031001054455354204D455353470301";

    public static void main(String[] args) throws ISOException {
        ConfigurableApplicationContext ctx = SpringApplication.run(Iso8583PocApplication.class, args);

        System.out.println(" \n ---------------Generating New ISO8583------------------------ \n");
        ISO8583GeneratorService generatorService = new ISO8583GeneratorService();
        var generatedISO8583Message = generatorService.generateISO8583Message();
        System.out.println("New generated ISO message: \n" + generatedISO8583Message + "\n\n===================");


        NettyWebServer nettyWebServer = ctx.getBeanFactory().getBean(NettyWebServer.class);
        nettyWebServer.runWebServer();

    }

    private static void parser(String generatedISO8583Message) throws ISOException {
        ISO8583ParserService parserService = new ISO8583ParserService();
        System.out.println(" \n ----------------Now Parsing----------------------- \n");
        parserService.parseMessage(generatedISO8583Message);
        System.out.println(" \n --------------------------------------- \n");
    }
}
