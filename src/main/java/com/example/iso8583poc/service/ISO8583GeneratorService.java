package com.example.iso8583poc.service;

import com.example.iso8583poc.util.Util;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;
import org.springframework.stereotype.Service;

@Service
public class ISO8583GeneratorService {

    public String generateISO8583Message() throws ISOException {
        GenericPackager packager = new GenericPackager("ISOMsg.xml");

        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(packager);

        isoMsg.setMTI("0100");
        isoMsg.set(2, "4321123443211234");
        isoMsg.set(3, "000000");
        isoMsg.set(4, "000000012300");
        isoMsg.set(7, "0304054133");
        isoMsg.set(11, "001205");
        isoMsg.set(14, "0205");
        isoMsg.set(18, "5399");
        isoMsg.set(22, "022");
        isoMsg.set(25, "00");
        isoMsg.set(35, "2312312332");
        isoMsg.set(37, "206305000014");
        isoMsg.set(41, "29110001");
        isoMsg.set(42, "1001001");
        isoMsg.set(48, "1200000000");
        isoMsg.set(49, "840");

        var result = isoMsg.pack();
        var iso8583Message = new String(result);

        System.out.println("Original iso8583 message : "+iso8583Message);

        var messageLength = iso8583Message.length();
        System.out.println("Original ISO Message length is : "+ messageLength);
        var messageLengthInByteHexData = Util.decimalToTwoByteHex(messageLength);
        System.out.println("Message length in two byte hex data : "+ messageLengthInByteHexData);
        return messageLengthInByteHexData.concat(iso8583Message);
    }

    public String generateISO8583Message2() throws ISOException {
        GenericPackager packager = new GenericPackager("ISOMsg.xml");

        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(packager);

        isoMsg.setMTI("0800");
        isoMsg.set(3, "000000");
        isoMsg.set(11, "000001");
        isoMsg.set(41, "29110001");

        var result = isoMsg.pack();
        return new String(result);
    }


    public String generateISO8583MessageWithFleetData() throws ISOException {
        GenericPackager packager = new GenericPackager("ISOMsg.xml");

        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(packager);

        isoMsg.setMTI("0100");
        isoMsg.set(2, "4321123443211234");
        isoMsg.set(3, "000000");
        isoMsg.set(4, "000000012300");
        isoMsg.set(7, "0304054133");
        isoMsg.set(11, "001205");
        isoMsg.set(14, "0205");
        isoMsg.set(18, "5399");
        isoMsg.set(22, "022");
        isoMsg.set(25, "00");
        isoMsg.set(35, "2312312332");
        isoMsg.set(37, "206305000014");
        isoMsg.set(41, "29110001");
        isoMsg.set(42, "1001001");
        isoMsg.set(48, "1200000000");
        isoMsg.set(49, "840");

        var result = isoMsg.pack();
        return new String(result);
    }
}
