package com.example.iso8583poc.service;

import com.example.iso8583poc.domain.ISO8583DataElement;
import com.example.iso8583poc.customer_iso8583_library.CPayISO8583Packager;
import com.example.iso8583poc.domain.MessageTypeIndicator;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.springframework.stereotype.Service;
import org.xml.sax.DTDHandler;

@Service
public class ISO8583ParserService {

    private static CPayISO8583Packager packager = new CPayISO8583Packager();

    private static String getBitMaps(String msg) {
        String firstBitmap = msg.substring(4, 20);
        String binFirstBitMap = hexToBinary(firstBitmap);
        boolean hasSecondary = (binFirstBitMap.charAt(0)) == '1';
        if (!hasSecondary) {
            return firstBitmap;
        }
        return msg.substring(4, 36);
    }

    private static String hexToBinary(String s) {
        String result = "";

        for (int i = 0; i < s.length(); i++) {
            result = result + hexToBinary(s.charAt(i));
        }
        return result;
    }

    private static String hexToBinary(char Hex) {
        String[] staticLookup = new String[]{"0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111"};
        return staticLookup[Integer.parseInt(Character.toString(Hex), 16)];
    }

    public void parseHexMessage(String iso8583HexMessage) throws ISOException {

        System.out.println("Original iso8583 message : "+iso8583HexMessage);
        System.out.println("Length is : "+ iso8583HexMessage.length());

        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(packager);
        var byteRepresentationOfHexMessage = ISOUtil.hex2byte(iso8583HexMessage);
        isoMsg.unpack(byteRepresentationOfHexMessage);
        System.out.println("MTI: " + MessageTypeIndicator.getMessageTypeIndicatorByCode(isoMsg.getMTI()));
        System.out.println("MTI Code: " + isoMsg.getMTI());
        System.out.println("BitMaps: " + getBitMaps(iso8583HexMessage));

        for (int i = 1; i <= isoMsg.getMaxField(); i++) {
            if (isoMsg.hasField(i)) {
                System.out.println("Field-" + i + ": " + ISO8583DataElement.getByIndexNumber(i) + ", Data: " + isoMsg.getValue(i));
            }
        }
    }

    public void parseHexMessageWithAdditionalHeader(String messageToParse) throws ISOException {

        var twoByteHeaderLength = messageToParse.substring(0, 4);
        System.out.println("Header Length in Byte: "+ twoByteHeaderLength);
        System.out.println("Header Length in decimal: "+ Integer.parseInt(twoByteHeaderLength, 16));

        var TPDUHeader = messageToParse.substring(4, 14);
        System.out.println("TPDUHeader" + TPDUHeader);
        this.parseTPDUHeader(TPDUHeader);

        var iso8583HexMessage = messageToParse.substring(14);
        System.out.println("Original iso8583 message : "+iso8583HexMessage);

        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(packager);
        var byteRepresentationOfHexMessage = ISOUtil.hex2byte(iso8583HexMessage);
        isoMsg.unpack(byteRepresentationOfHexMessage);
        System.out.println("MTI: " + MessageTypeIndicator.getMessageTypeIndicatorByCode(isoMsg.getMTI()));
        System.out.println("MTI Code: " + isoMsg.getMTI());
        System.out.println("BitMaps: " + getBitMaps(iso8583HexMessage));

        for (int i = 1; i <= isoMsg.getMaxField(); i++) {
            if (isoMsg.hasField(i)) {
                System.out.println("Field-" + i + ": " + ISO8583DataElement.getByIndexNumber(i) + ", Data: " + isoMsg.getValue(i));
            }
        }
    }

    private void parseTPDUHeader(String TPDUHeader){
        String protocolID = TPDUHeader.substring(0,2);
        String terminalID = hexToBinary(TPDUHeader.substring(2,6));
        String destinationAddress = hexToBinary(TPDUHeader.substring(6));

        System.out.println("protocolID : "+protocolID);
        System.out.println("terminalID : "+terminalID);
        System.out.println("destinationAddress : "+destinationAddress);
    }
}
