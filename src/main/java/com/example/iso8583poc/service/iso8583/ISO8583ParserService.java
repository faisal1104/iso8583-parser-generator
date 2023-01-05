package com.example.iso8583poc.service.iso8583;

import com.example.iso8583poc.custom_iso8583_packager.CPayISO8583Packager;
import com.example.iso8583poc.domain.iso8583.ISO8583DataElementType;
import com.example.iso8583poc.domain.iso8583.MessageTypeIndicator;
import com.example.iso8583poc.service.preauth.PreAuthRequestHandler;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ISO8583ParserService {

    private static final CPayISO8583Packager packager = new CPayISO8583Packager();
    private final PreAuthRequestHandler preAuthRequestHandler;

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

    public Map<String, String> parseAndPrintHexMessage(String iso8583HexMessage, boolean isHeaderExist) throws ISOException, DecoderException {
        if (isHeaderExist) {
            //If header exist then there will be 2 byte MessageLengthHeader and 5 byte TPDU header
            var messageLengthAndTPDUHeader = iso8583HexMessage.substring(0, 14);
            System.out.println("MessageLengthAndTPDUHeader : " + messageLengthAndTPDUHeader);
            iso8583HexMessage = iso8583HexMessage.substring(14);
        }

        System.out.println("Original iso8583 message : " + iso8583HexMessage);
        System.out.println("Length is : " + Hex.decodeHex(iso8583HexMessage).length + "byte");

        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(packager);
        var byteRepresentationOfHexMessage = ISOUtil.hex2byte(iso8583HexMessage);
        isoMsg.unpack(byteRepresentationOfHexMessage);

        Map<String, String> responseMap = new LinkedHashMap<>();
        responseMap.put("MTI", MessageTypeIndicator.getMessageTypeIndicatorByCode(isoMsg.getMTI()).name());

        System.out.println("MTI: " + MessageTypeIndicator.getMessageTypeIndicatorByCode(isoMsg.getMTI()));
        System.out.println("MTI Code: " + isoMsg.getMTI());
        System.out.println("BitMaps: " + getBitMaps(iso8583HexMessage));

        for (int i = 1; i <= isoMsg.getMaxField(); i++) {
            if (isoMsg.hasField(i)) {
                var iso8583DataElement = ISO8583DataElementType.getByIndexNumber(i);
                var value = String.valueOf(isoMsg.getValue(i));
                System.out.println("Field-" + i + ": " + iso8583DataElement + ", Data: " + value);
                responseMap.put(iso8583DataElement.name(), value);
            }
        }
        return responseMap;
    }

    public String parseHexMessageAndGetCommanderPreAuthRequest(String iso8583HexMessage) throws ISOException {

        System.out.println("Original iso8583 message : " + iso8583HexMessage);
        System.out.println("Length is : " + iso8583HexMessage.length());

        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(packager);
        var byteRepresentationOfHexMessage = ISOUtil.hex2byte(iso8583HexMessage);
        isoMsg.unpack(byteRepresentationOfHexMessage);

        var messageTypeIndicator = MessageTypeIndicator.getMessageTypeIndicatorByCode(isoMsg.getMTI());
        System.out.println("MTI: " + messageTypeIndicator);
        System.out.println("MTI Code: " + isoMsg.getMTI());
        System.out.println("BitMaps: " + getBitMaps(iso8583HexMessage));

        if (messageTypeIndicator.equals(MessageTypeIndicator.PRE_AUTH_REQUEST)) {
            return preAuthRequestHandler.handlePreAuthRequest(isoMsg);
        } else
            return StringUtils.EMPTY;
    }

    public void parseHexMessageWithAdditionalHeader(String messageToParse) throws ISOException {

        var twoByteHeaderLength = messageToParse.substring(0, 4);
        System.out.println("Header Length in Byte: " + twoByteHeaderLength);
        System.out.println("Header Length in decimal: " + Integer.parseInt(twoByteHeaderLength, 16));

        var TPDUHeader = messageToParse.substring(4, 14);
        System.out.println("TPDUHeader" + TPDUHeader);
        this.parseTPDUHeader(TPDUHeader);

        var iso8583HexMessage = messageToParse.substring(14);
        System.out.println("Original iso8583 message : " + iso8583HexMessage);

        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(packager);
        var byteRepresentationOfHexMessage = ISOUtil.hex2byte(iso8583HexMessage);
        isoMsg.unpack(byteRepresentationOfHexMessage);
        System.out.println("MTI: " + MessageTypeIndicator.getMessageTypeIndicatorByCode(isoMsg.getMTI()));
        System.out.println("MTI Code: " + isoMsg.getMTI());
        System.out.println("BitMaps: " + getBitMaps(iso8583HexMessage));

        for (int i = 1; i <= isoMsg.getMaxField(); i++) {
            if (isoMsg.hasField(i)) {
                System.out.println("Field-" + i + ": " + ISO8583DataElementType.getByIndexNumber(i) + ", Data: " + isoMsg.getValue(i));
            }
        }
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
