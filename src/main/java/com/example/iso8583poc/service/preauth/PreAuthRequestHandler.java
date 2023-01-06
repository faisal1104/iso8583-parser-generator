package com.example.iso8583poc.service.preauth;

import com.example.iso8583poc.domain.iso8583.ISO8583DataElementType;
import com.example.iso8583poc.domain.preauth.CommanderPreAuthRequest;
import com.example.iso8583poc.service.fleet_data_decoder.FleetDataDecoderService;
import com.example.iso8583poc.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PreAuthRequestHandler {

    private final PreAuthResponseService preAuthResponseService;

    public String handlePreAuthRequest(Map<ISO8583DataElementType, String> isoDataElementValueMap) {

        var preAuthRequest = new CommanderPreAuthRequest();

        for (var map : isoDataElementValueMap.entrySet()) {
            var iso8583DataElementType = map.getKey();
            var value = map.getValue();
            switch (iso8583DataElementType) {
                case ACCOUNT_NUMBER -> preAuthRequest.setCardNumber(value);
                case PROCESSING_CODE -> preAuthRequest.setProcessingCode(value);
                case TRANSACTION_VALUE -> preAuthRequest.setTransactionValue(value);
                case SYSTEM_TRACE_NUMBER -> preAuthRequest.setSystemTraceNumber(value);
                case MERCHANT_ID_CODE -> preAuthRequest.setMerchantIdCode(value);
                case TERMINAL_ID -> preAuthRequest.setTerminalId(value);
                case CURRENCY_CODE -> preAuthRequest.setCurrencyCode(value);
                case POS_CONDITION_CODE -> preAuthRequest.setPosConditionCode(value);
                case ENTRY_MODE_IN_THE_POS -> preAuthRequest.setPosEntryMode(value);
                case ACQUIRER_ID_CODE -> preAuthRequest.setAcquirerIdCode(value);
                case TRANSACTION_LOCAL_HOUR ->
                    preAuthRequest.setTransactionLocalHour(Util.getLocalTimeFromString(value));
                case FLEET_DATA_TWO ->
                    FleetDataDecoderService.extractFleetDataTwoFromPreAuthRequest(preAuthRequest, value);
            }
        }
        System.out.println(preAuthRequest);
        return preAuthResponseService.generateResponseMessageAs8583(preAuthRequest);
    }
}
