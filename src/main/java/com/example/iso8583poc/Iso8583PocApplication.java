package com.example.iso8583poc;

import com.example.iso8583poc.nettyserver.NettyWebServer;
import com.example.iso8583poc.service.iso8583.ISO8583GeneratorService;
import org.jpos.iso.ISOException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

@EnableFeignClients
@SpringBootApplication
public class Iso8583PocApplication {

    public static void main(String[] args) throws ISOException {
        ConfigurableApplicationContext ctx = SpringApplication.run(Iso8583PocApplication.class, args);

        System.out.println(" \n ---------------Generating New ISO8583------------------------ \n");
        ISO8583GeneratorService generatorService = new ISO8583GeneratorService();
        var generatedISO8583Message = generatorService.generatePreAuthRequestISO8583Message();
        System.out.println("New generated ISO message: \n" + generatedISO8583Message + "\n\n===================");

        NettyWebServer nettyWebServer = ctx.getBeanFactory().getBean(NettyWebServer.class);
        nettyWebServer.runWebServer();

    }
}
