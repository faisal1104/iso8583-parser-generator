package com.example.iso8583poc.service.iso8583;

import com.example.iso8583poc.custom_iso8583_packager.CPayISO8583Packager;
import com.example.iso8583poc.domain.iso8583.MessageTypeIndicator;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

import static com.example.iso8583poc.domain.iso8583.ISO8583DataElementType.*;
import static com.example.iso8583poc.util.Util.ISO8583_TIME_FORMAT;

@Service
public class ISO8583GeneratorService {

    private static final CPayISO8583Packager packager = new CPayISO8583Packager();

    public String generateISO8583Message() throws ISOException {

        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(packager);

        isoMsg.setMTI(MessageTypeIndicator.NETWORK_MANAGEMENT_REQUEST.getCode());
        isoMsg.set(3, "000000");
        isoMsg.set(11, "000001");
        isoMsg.set(41, "29110001");

        var result = isoMsg.pack();
        var hexMessage = Hex.encodeHexString(result);

        // region print length header & TPDU info
        int messageByteLength = hexMessage.length() / 2;
        System.out.println("Data element Message Length : " + messageByteLength + "Byte ");

        String messageLengthHeaderFirstByte = Integer.toHexString(messageByteLength / 256);
        String messageLengthHeaderSecondByte = Integer.toHexString(messageByteLength % 256);


        messageLengthHeaderFirstByte = StringUtils.leftPad(messageLengthHeaderFirstByte, 2, '0');
        messageLengthHeaderSecondByte = StringUtils.leftPad(messageLengthHeaderSecondByte, 2, '0');

        var hexMessageWithTwoByteHeader = messageLengthHeaderFirstByte.concat(messageLengthHeaderSecondByte).concat(hexMessage);
        System.out.println("hexMessageWithTwoByteHeader  : " + hexMessageWithTwoByteHeader);

        System.out.println("Full Message Length : " + hexMessageWithTwoByteHeader.length() / 2 + "Byte ");
        // end region

        return hexMessage;
    }

    public String generatePreAuthRequestISO8583Message() throws ISOException {
        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(packager);

        String fleetDataTwo = "deDR00098100219".concat("de00001444").concat("C000112/");
        //String fleetDataTwo = "deDR00098100219".concat("de00001899").concat("C000309/");

        isoMsg.setMTI(MessageTypeIndicator.PRE_AUTH_REQUEST.getCode());
        //isoMsg.set(ACCOUNT_NUMBER.getIndexNumber(), "7095041000090008113");
        isoMsg.set(ACCOUNT_NUMBER.getIndexNumber(), "7095041000090008114");
        isoMsg.set(PROCESSING_CODE.getIndexNumber(), "000000");
        isoMsg.set(TERMINAL_ID.getIndexNumber(), "TT123456");
        isoMsg.set(MERCHANT_ID_CODE.getIndexNumber(), "M10983");
        isoMsg.set(CURRENCY_CODE.getIndexNumber(), "840");
        isoMsg.set(ENTRY_MODE_IN_THE_POS.getIndexNumber(), "123");
        isoMsg.set(ACQUIRER_ID_CODE.getIndexNumber(), "123456789");
        isoMsg.set(TRANSACTION_LOCAL_HOUR.getIndexNumber(), LocalTime.now().format(ISO8583_TIME_FORMAT));
        isoMsg.set(FLEET_DATA_TWO.getIndexNumber(), fleetDataTwo);

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