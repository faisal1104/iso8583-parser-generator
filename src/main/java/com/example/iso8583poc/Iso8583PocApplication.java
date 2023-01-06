package com.example.iso8583poc;

import com.example.iso8583poc.nettyserver.NettyWebServer;
import com.example.iso8583poc.service.iso8583.ISO8583GeneratorService;
import org.apache.commons.codec.DecoderException;
import org.jpos.iso.ISOException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

@EnableFeignClients
@SpringBootApplication
public class Iso8583PocApplication {

    public static void main(String[] args) throws ISOException, DecoderException {
        ConfigurableApplicationContext ctx = SpringApplication.run(Iso8583PocApplication.class, args);

        System.out.println(" \n ---------------Generating A Echo Test Message in ISO8583------------------------ \n");
        ISO8583GeneratorService generatorService = ctx.getBeanFactory().getBean(ISO8583GeneratorService.class);
        var generatedISO8583Message = generatorService.generateEchoTestRequestISO8583Message();
        System.out.println("Generated Echo Test ISO message: \n" + generatedISO8583Message + "\n\n===================");

        NettyWebServer nettyWebServer = ctx.getBeanFactory().getBean(NettyWebServer.class);
        nettyWebServer.runWebServer();
    }
}
