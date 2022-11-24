package com.example.iso8583poc.service;

import com.example.iso8583poc.domain.ISO8583DataElement;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;

public class ISO8583ParserService {


    public void parseMessage(String iso8584Message) throws ISOException {
        ISOMsg isoMsg = new ISOMsg();

        GenericPackager packager = new GenericPackager("ISOMsg.xml");
        isoMsg.setPackager(packager);
        isoMsg.unpack(iso8584Message.getBytes());

        System.out.println("MTI: "+ isoMsg.getMTI());
        System.out.println("BitMaps: "+ getBitMaps(iso8584Message));

        for (int i = 1; i <= isoMsg.getMaxField(); i++) {
            if (isoMsg.hasField(i)) {
                System.out.println("Field-"+i+": "+ ISO8583DataElement.getByIndexNumber(i)+ ", Data: "+isoMsg.getValue(i));
            }
        }

    }


    private static String getBitMaps(String msg) {
        String firstBitmap = msg.substring(4, 20);
        String binFirstBitMap = hexToBinary(firstBitmap);
        boolean hasSecondary = (binFirstBitMap.charAt(0)) == '1';
        if (!hasSecondary) {
            return firstBitmap;
        }
        return msg.substring(4,36);
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
}
