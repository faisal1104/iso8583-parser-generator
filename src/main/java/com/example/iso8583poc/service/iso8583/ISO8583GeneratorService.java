package com.example.iso8583poc.service.iso8583;

import com.example.iso8583poc.custom_iso8583_packager.CPayISO8583Packager;
import com.example.iso8583poc.domain.iso8583.MessageTypeIndicator;
import org.apache.commons.codec.DecoderException;
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

    public String generateISO8583Message() throws ISOException, DecoderException {

        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(packager);

        isoMsg.setMTI(MessageTypeIndicator.NETWORK_MANAGEMENT_REQUEST.getCode());
/*        isoMsg.set(3, "000000");
        isoMsg.set(11, "000001");
        isoMsg.set(41, "29110001");*/
        isoMsg.set(TRANSACTION_DATE_TIME.getIndexNumber(), "1025084436");
        isoMsg.set(SYSTEM_TRACE_NUMBER.getIndexNumber(), "000004");
        isoMsg.set(NETWORK_MANAGEMENT_INFORMATION_CODE.getIndexNumber(), "003");

        var result = isoMsg.pack();
        var hexISOMessage = Hex.encodeHexString(result);
        this.generateHeaderForISOMessage(hexISOMessage);
        return hexISOMessage;
    }

    public String generatePreAuthRequestISO8583Message() throws ISOException, DecoderException {
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
        var hexISOMessage = Hex.encodeHexString(result);
        this.generateHeaderForISOMessage(hexISOMessage);
        return hexISOMessage;
    }

    private String generateTPDU() {
        String protocolID = "60";
        String terminalID = "0001";
        String destinationAddress = "0000";
        return protocolID.concat(terminalID).concat(destinationAddress);
    }

    private String generateHeaderForISOMessage(String hexISOMessage) throws DecoderException {

        var TPDUHeader = this.generateTPDU();
        System.out.println("TPDU Header Length : " + Hex.decodeHex(TPDUHeader).length + "Byte ");

        int messageByteLength = Hex.decodeHex(hexISOMessage).length;
        System.out.println("Original HexISO (MTI+Data Elements) Message Length : " + messageByteLength + "Byte ");

        var TPDUAndISOHexMessage = TPDUHeader.concat(hexISOMessage);
        System.out.println("TPDUAndISOHexMessage :: " + TPDUAndISOHexMessage);
        System.out.println("TPDU And ISO Message Length : " + Hex.decodeHex(TPDUAndISOHexMessage).length + "Byte ");

        String messageLengthHeaderFirstByte = Integer.toHexString(messageByteLength / 256);
        String messageLengthHeaderSecondByte = Integer.toHexString(messageByteLength % 256);


        messageLengthHeaderFirstByte = StringUtils.leftPad(messageLengthHeaderFirstByte, 2, '0');
        messageLengthHeaderSecondByte = StringUtils.leftPad(messageLengthHeaderSecondByte, 2, '0');

        var messageLengthInByte = messageLengthHeaderFirstByte.concat(messageLengthHeaderSecondByte);
        var hexMessageWithMsgLengthAndTPDUHeader = messageLengthInByte.concat(TPDUAndISOHexMessage);
        System.out.println("HexMessageWithMsgLengthAndTPDUHeader  : " + hexMessageWithMsgLengthAndTPDUHeader);
        // end region

        return hexMessageWithMsgLengthAndTPDUHeader;
    }
}