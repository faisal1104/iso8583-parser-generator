package com.example.iso8583poc.service.preauth;

import com.example.iso8583poc.domain.iso8583.ISO8583DataElementType;
import com.example.iso8583poc.domain.preauth.CommanderPreAuthRequest;
import com.example.iso8583poc.service.fleet_data_decoder.FleetDataDecoderService;
import com.example.iso8583poc.util.Util;
import lombok.RequiredArgsConstructor;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PreAuthRequestHandler {

    private final PreAuthResponseService preAuthResponseService;

    public String handlePreAuthRequest(ISOMsg unPackedISOMsg) throws ISOException {

        var preAuthRequest = new CommanderPreAuthRequest();

        for (int fieldNumber = 1; fieldNumber <= unPackedISOMsg.getMaxField(); fieldNumber++) {
            if (unPackedISOMsg.hasField(fieldNumber)) {
                var iso8583DataElement = ISO8583DataElementType.getByIndexNumber(fieldNumber);
                var value = String.valueOf(unPackedISOMsg.getValue(fieldNumber));

                System.out.println("Field-" + fieldNumber + ": " + iso8583DataElement + ", Data: " + unPackedISOMsg.getValue(fieldNumber));

                switch (iso8583DataElement) {
                    case ACCOUNT_NUMBER -> preAuthRequest.setCardNumber(value);
                    case PROCESSING_CODE -> preAuthRequest.setProcessingCode(value);
                    case MERCHANT_ID_CODE -> preAuthRequest.setMerchantIdCode(value);
                    case TERMINAL_ID -> preAuthRequest.setTerminalId(value);
                    case CURRENCY_CODE -> preAuthRequest.setCurrencyCodeNumber(value);
                    case ENTRY_MODE_IN_THE_POS -> preAuthRequest.setPosEntryMode(value);
                    case ACQUIRER_ID_CODE -> preAuthRequest.setAcquirerIdCode(value);
                    case TRANSACTION_LOCAL_HOUR ->
                        preAuthRequest.setTransactionLocalHour(Util.getLocalTimeFromString(value));
                    case FLEET_DATA_TWO ->
                        FleetDataDecoderService.extractFleetDataTwoFromPreAuthRequest(preAuthRequest, value);
                }
            }
        }
        System.out.println(preAuthRequest);
        return preAuthResponseService.generateResponseMessageAs8583(preAuthRequest);
    }
}
