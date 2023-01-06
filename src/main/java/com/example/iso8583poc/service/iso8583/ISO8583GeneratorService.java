package com.example.iso8583poc.service.iso8583;

import com.example.iso8583poc.annotation.ISO8583DataElementInfo;
import com.example.iso8583poc.iso8583packager.CustomISO8583Packager;
import com.example.iso8583poc.domain.completion.CommanderCompletionRequest;
import com.example.iso8583poc.domain.iso8583.ISO8583DataElementType;
import com.example.iso8583poc.domain.iso8583.ISO8583MessageInformationRequest;
import com.example.iso8583poc.domain.iso8583.MessageTypeIndicator;
import com.example.iso8583poc.domain.preauth.CommanderPreAuthRequest;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.example.iso8583poc.domain.iso8583.ISO8583DataElementType.*;
import static com.example.iso8583poc.util.Util.ISO8583_TIME_FORMAT;

@Service
public class ISO8583GeneratorService {

    @Value("${iso8583.header.active}")
    private boolean isAdditionalHeaderExistInISO8583Message;

    private static final CustomISO8583Packager packager = new CustomISO8583Packager();


    public String generateCommanderResponseInISO8583Format(MessageTypeIndicator messageTypeIndicator, Object response) throws IllegalAccessException, ISOException, DecoderException {
        Map<ISO8583DataElementType, String> isoDataElementValueMap = new HashMap<>();
        for (Field field : response.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(ISO8583DataElementInfo.class)) {
                ISO8583DataElementInfo iso8583DataElementInfo = field.getAnnotation(ISO8583DataElementInfo.class);
                int fieldNumber = iso8583DataElementInfo.fieldNumber();
                if (Objects.nonNull(field.get(response))) {
                    String value = String.valueOf(field.get(response));
                    isoDataElementValueMap.put(ISO8583DataElementType.getByIndexNumber(fieldNumber), value);
                }
            }
        }
        var iso8583MessageInformationRequest = new ISO8583MessageInformationRequest(messageTypeIndicator, isoDataElementValueMap);
        return this.generateISO8583Message(iso8583MessageInformationRequest);
    }

    public String generateISO8583Message(ISO8583MessageInformationRequest iso8583MessageInformationRequest) throws ISOException, DecoderException {
        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(packager);

        isoMsg.setMTI(iso8583MessageInformationRequest.getMessageTypeIndicator().getCode());

        for (var map : iso8583MessageInformationRequest.getIsoDataElementValueMap().entrySet()) {
            isoMsg.set(map.getKey().getIndexNumber(), map.getValue());
        }
        var result = isoMsg.pack();
        var iso8583MessageInHex = Hex.encodeHexString(result);
        if (isAdditionalHeaderExistInISO8583Message) {
            iso8583MessageInHex = this.generateHeaderForISOMessage(iso8583MessageInHex);
        }
        return iso8583MessageInHex;
    }

    public String generatePreAuthRequestISO8583Message(CommanderPreAuthRequest preAuthRequest) throws ISOException, DecoderException {

        Map<ISO8583DataElementType, String> isoDataElementValueMap = new HashMap<>();
        isoDataElementValueMap.put(ACCOUNT_NUMBER, preAuthRequest.getCardNumber());
        isoDataElementValueMap.put(PROCESSING_CODE, "000000");
        isoDataElementValueMap.put(TRANSACTION_VALUE, "123456789000");
        isoDataElementValueMap.put(TERMINAL_ID, "TT123456");
        isoDataElementValueMap.put(MERCHANT_ID_CODE, "M10983");
        isoDataElementValueMap.put(CURRENCY_CODE, "840");
        isoDataElementValueMap.put(ENTRY_MODE_IN_THE_POS, "123");
        isoDataElementValueMap.put(ACQUIRER_ID_CODE, "123456789");
        isoDataElementValueMap.put(SYSTEM_TRACE_NUMBER, "000004");
        isoDataElementValueMap.put(POS_CONDITION_CODE, preAuthRequest.getPosConditionCode());
        isoDataElementValueMap.put(TRANSACTION_LOCAL_HOUR, LocalTime.now().format(ISO8583_TIME_FORMAT));

        String fleetDataTwo = preAuthRequest.getDriverCode().concat(preAuthRequest.getVehicleCode()).concat(preAuthRequest.getOdoMeter());
        isoDataElementValueMap.put(FLEET_DATA_TWO, fleetDataTwo);

        var iso8583MessageInformationRequest = new ISO8583MessageInformationRequest(MessageTypeIndicator.PRE_AUTH_REQUEST, isoDataElementValueMap);
        return this.generateISO8583Message(iso8583MessageInformationRequest);
    }

    public String generateCompletionRequestISO8583Message(CommanderCompletionRequest completionRequest) throws ISOException, DecoderException {

        Map<ISO8583DataElementType, String> isoDataElementValueMap = new HashMap<>();
        isoDataElementValueMap.put(ACCOUNT_NUMBER, completionRequest.getCardNumber());
        isoDataElementValueMap.put(PROCESSING_CODE, completionRequest.getProcessingCode());
        isoDataElementValueMap.put(TRANSACTION_VALUE, "123456789000");
        isoDataElementValueMap.put(TERMINAL_ID, "TT123456");
        isoDataElementValueMap.put(MERCHANT_ID_CODE, "M10983");
        isoDataElementValueMap.put(CURRENCY_CODE, "840");
        isoDataElementValueMap.put(ENTRY_MODE_IN_THE_POS, "123");
        isoDataElementValueMap.put(ACQUIRER_ID_CODE, "123456789");
        isoDataElementValueMap.put(SYSTEM_TRACE_NUMBER, "000004");
        isoDataElementValueMap.put(POS_CONDITION_CODE, completionRequest.getPosConditionCode());
        isoDataElementValueMap.put(TRANSACTION_LOCAL_HOUR, LocalTime.now().format(ISO8583_TIME_FORMAT));
        isoDataElementValueMap.put(FLEET_DATA_TWO, completionRequest.getFleetDataTwo());

        var iso8583MessageInformationRequest = new ISO8583MessageInformationRequest(MessageTypeIndicator.COMPLETION_REQUEST, isoDataElementValueMap);
        return this.generateISO8583Message(iso8583MessageInformationRequest);
    }

    public String generateEchoTestRequestISO8583Message() throws ISOException, DecoderException {
        Map<ISO8583DataElementType, String> isoDataElementValueMap = new HashMap<>();
        isoDataElementValueMap.put(TRANSACTION_DATE_TIME, "0105163012");
        isoDataElementValueMap.put(SYSTEM_TRACE_NUMBER, "000004");
        isoDataElementValueMap.put(APPLICATION_SOFT_VERSION, "MOBILE_REFUELING_v0.0.1");
        isoDataElementValueMap.put(NETWORK_MANAGEMENT_INFORMATION_CODE, "003");

        var iso8583MessageInformationRequest = new ISO8583MessageInformationRequest(MessageTypeIndicator.NETWORK_MANAGEMENT_REQUEST, isoDataElementValueMap);
        return this.generateISO8583Message(iso8583MessageInformationRequest);
    }

    private String generateHeaderForISOMessage(String hexISOMessage) throws DecoderException {

        var tpduHeader = this.generateTPDU();
        System.out.println("TPDU Header " + tpduHeader);
        System.out.println("TPDU Header Length : " + Hex.decodeHex(tpduHeader).length + "Byte ");

        var tpduAndISOHexMessage = tpduHeader.concat(hexISOMessage);
        var messageByteLength = Hex.decodeHex(tpduAndISOHexMessage).length;
        System.out.println("TPDU And ISO(MTI+Data Element) Message Length : " + messageByteLength + "Byte ");

        String messageLengthHeaderFirstByte = Integer.toHexString(messageByteLength / 256);
        String messageLengthHeaderSecondByte = Integer.toHexString(messageByteLength % 256);


        messageLengthHeaderFirstByte = StringUtils.leftPad(messageLengthHeaderFirstByte, 2, '0');
        messageLengthHeaderSecondByte = StringUtils.leftPad(messageLengthHeaderSecondByte, 2, '0');

        var messageLengthInByte = messageLengthHeaderFirstByte.concat(messageLengthHeaderSecondByte);
        var hexISOMessageWithMsgLengthAndTPDUHeader = messageLengthInByte.concat(tpduAndISOHexMessage);
        System.out.println("ISO HexMessage WithMsgLengthAndTPDUHeader  : " + hexISOMessageWithMsgLengthAndTPDUHeader);
        // end region

        return hexISOMessageWithMsgLengthAndTPDUHeader;
    }

    private String generateTPDU() {
        String protocolID = "60";
        String terminalID = "0001";
        String destinationAddress = "0000";
        return protocolID.concat(terminalID).concat(destinationAddress);
    }
}