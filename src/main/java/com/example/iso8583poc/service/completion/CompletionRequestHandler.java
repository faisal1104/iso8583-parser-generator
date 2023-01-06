package com.example.iso8583poc.service.completion;

import com.example.iso8583poc.domain.completion.CommanderCompletionRequest;
import com.example.iso8583poc.domain.iso8583.ISO8583DataElementType;
import com.example.iso8583poc.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class CompletionRequestHandler {

    private final CompletionResponseService completionResponseService;

    public String handleCompletionRequest(Map<ISO8583DataElementType, String> isoDataElementValueMap) {

        var completionRequest = new CommanderCompletionRequest();

        for (var map : isoDataElementValueMap.entrySet()) {
            var iso8583DataElementType = map.getKey();
            var value = map.getValue();

            switch (iso8583DataElementType) {
                case ACCOUNT_NUMBER -> completionRequest.setCardNumber(value);
                case PROCESSING_CODE -> completionRequest.setProcessingCode(value);
                case TRANSACTION_VALUE -> completionRequest.setTransactionValue(value);
                case SYSTEM_TRACE_NUMBER -> completionRequest.setSystemTraceNumber(value);
                case MERCHANT_ID_CODE -> completionRequest.setMerchantIdCode(value);
                case TERMINAL_ID -> completionRequest.setTerminalId(value);
                case CURRENCY_CODE -> completionRequest.setCurrencyCode(value);
                case ENTRY_MODE_IN_THE_POS -> completionRequest.setPosEntryMode(value);
                case POS_CONDITION_CODE -> completionRequest.setPosConditionCode(value);
                case ACQUIRER_ID_CODE -> completionRequest.setAcquirerIdCode(value);
                case TRANSACTION_LOCAL_HOUR ->
                    completionRequest.setTransactionLocalHour(Util.getLocalTimeFromString(value));
                case FLEET_DATA_TWO -> log.info("Extract fleet data two of Completion Request not");
                //FleetDataDecoderService.extractFleetDataTwoFromCompletionRequest(completionRequest, value);
            }
        }

        System.out.println(completionRequest);
        return completionResponseService.generateResponseMessageAs8583(completionRequest);
    }
}
