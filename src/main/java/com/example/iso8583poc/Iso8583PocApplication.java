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

    public static void main(String[] args) throws ISOException {
        ConfigurableApplicationContext ctx = SpringApplication.run(Iso8583PocApplication.class, args);

        System.out.println(" \n ---------------Generating New ISO8583------------------------ \n");
        ISO8583GeneratorService generatorService = new ISO8583GeneratorService();
        var generatedISO8583Message = generatorService.generateISO8583Message();
        System.out.println("New generated ISO message: \n" + generatedISO8583Message + "\n\n===================");
        parser(generatedISO8583Message);

        NettyWebServer nettyWebServer = ctx.getBeanFactory().getBean(NettyWebServer.class);
        nettyWebServer.runWebServer();

    }

    private static void parser(String generatedISO8583Message) throws ISOException {
        ISO8583ParserService parserService = new ISO8583ParserService();
        System.out.println(" \n ----------------Now Parsing----------------------- \n");
        parserService.parseHexMessage("00808220000000000000040000000000000010250844360000040003333031");
        System.out.println(" \n --------------------------------------- \n");
    }
}
