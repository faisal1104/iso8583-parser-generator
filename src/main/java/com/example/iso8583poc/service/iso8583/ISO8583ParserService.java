package com.example.iso8583poc.service.iso8583;

import com.example.iso8583poc.iso8583packager.CustomISO8583Packager;
import com.example.iso8583poc.domain.iso8583.ISO8583DataElementType;
import com.example.iso8583poc.domain.iso8583.MessageTypeIndicator;
import com.example.iso8583poc.domain.iso8583.ParsedISO8583MessageInformation;
import com.example.iso8583poc.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ISO8583ParserService {

    @Value("${iso8583.header.active}")
    private boolean isAdditionalHeaderExistInISO8583Message;

    private static final CustomISO8583Packager packager = new CustomISO8583Packager();

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

    public ParsedISO8583MessageInformation parseHexMessage(String iso8583HexMessage) throws ISOException {
        if (isAdditionalHeaderExistInISO8583Message) {
            log.info("Message Length & TPDU header is present in this message");
            var totalHeaderLength = Util.getTotalHeaderLengthOfISOMessage();
            this.parseTPDUHeader(iso8583HexMessage.substring(Util.MESSAGE_LENGTH_HEADER_BYTE * 2, totalHeaderLength));
            iso8583HexMessage = iso8583HexMessage.substring(totalHeaderLength);
        }

        System.out.println("Original iso8583 message : " + iso8583HexMessage);
        System.out.println("Length is : " + iso8583HexMessage.length());

        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(packager);
        var byteRepresentationOfHexMessage = ISOUtil.hex2byte(iso8583HexMessage);
        isoMsg.unpack(byteRepresentationOfHexMessage);

        var messageTypeIndicator = MessageTypeIndicator.getMessageTypeIndicatorByCode(isoMsg.getMTI());
        var bitMaps = getBitMaps(iso8583HexMessage);
        System.out.println("MTI: " + messageTypeIndicator);
        System.out.println("MTI Code: " + isoMsg.getMTI());
        System.out.println("BitMaps: " + bitMaps);

        Map<ISO8583DataElementType, String> isoDataElementValueMap = new LinkedHashMap<>();
        for (int fieldNumber = 1; fieldNumber <= isoMsg.getMaxField(); fieldNumber++) {
            if (isoMsg.hasField(fieldNumber)) {
                var iso8583DataElement = ISO8583DataElementType.getByIndexNumber(fieldNumber);
                var value = String.valueOf(isoMsg.getValue(fieldNumber));
                System.out.println("Field-" + fieldNumber + ": " + iso8583DataElement + ", Data: " + value);
                isoDataElementValueMap.put(iso8583DataElement, value);
            }
        }

        return new ParsedISO8583MessageInformation(messageTypeIndicator, bitMaps, isoDataElementValueMap);
    }

    private void parseTPDUHeader(String TPDUHeader) {
        String protocolID = TPDUHeader.substring(0, 2);
        String terminalID = hexToBinary(TPDUHeader.substring(2, 6));
        String destinationAddress = hexToBinary(TPDUHeader.substring(6));

        System.out.println("protocolID : " + protocolID);
        System.out.println("terminalID : " + terminalID);
        System.out.println("destinationAddress : " + destinationAddress);
    }
}
