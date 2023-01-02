package com.example.iso8583poc.service;

import com.example.iso8583poc.customer_iso8583_library.CPayISO8583Packager;
import com.example.iso8583poc.domain.MessageTypeIndicator;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import org.springframework.stereotype.Service;

@Service
public class ISO8583GeneratorService {

    private static CPayISO8583Packager packager = new CPayISO8583Packager();

    public String generateISO8583Message() throws ISOException {

        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(packager);

        isoMsg.setMTI(MessageTypeIndicator.NETWORK_MANAGEMENT_REQUEST.getCode());
        isoMsg.set(3, "000000");
        isoMsg.set(11, "000001");
        isoMsg.set(41, "29110001");

        var result = isoMsg.pack();
        var hexMessage = Hex.encodeHexString(result);

        int messageByteLength = hexMessage.length()/2;
        System.out.println("data element Message Length : "+messageByteLength + "Byte ");

        String messageLengthHeaderFirstByte = Integer.toHexString(messageByteLength / 256);
        String messageLengthHeaderSecondByte = Integer.toHexString(messageByteLength % 256);


        messageLengthHeaderFirstByte = StringUtils.leftPad(messageLengthHeaderFirstByte, 2, '0');
        messageLengthHeaderSecondByte = StringUtils.leftPad(messageLengthHeaderSecondByte, 2, '0');

        var hexMessageWithTwoByteHeader = messageLengthHeaderFirstByte.concat(messageLengthHeaderSecondByte).concat(hexMessage);
        System.out.println("hexMessageWithTwoByteHeader  : " + hexMessageWithTwoByteHeader);

        System.out.println("Full Message Length : "+hexMessageWithTwoByteHeader.length()/2 + "Byte ");
        return hexMessage;
    }

    public String generateISO8583Message2() throws ISOException {
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
        var iso8583Message = Hex.encodeHexString(result);

        System.out.println("Original iso8583 message : "+iso8583Message);
        return iso8583Message;
    }


    public String generateISO8583MessageWithFleetData() throws ISOException {
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
        return Hex.encodeHexString(result);
    }

    private String generateTPDU() {
        String protocolID = "60";
        String terminalID = "0001";
        String destinationAddress = "0000";
     return protocolID.concat(terminalID).concat(destinationAddress);
    }
}