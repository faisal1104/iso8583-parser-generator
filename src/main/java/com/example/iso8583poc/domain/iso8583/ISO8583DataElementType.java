package com.example.iso8583poc.domain.iso8583;

import com.example.iso8583poc.util.Util;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ISO8583DataElementType {
    PRIMARY_BIT_MAP(0),
    SECONDARY_BIT_MAP(1),
    ACCOUNT_NUMBER(2),
    PROCESSING_CODE(3),
    TRANSACTION_VALUE(4),
    TRANSACTION_DATE_TIME(7),
    SYSTEM_TRACE_NUMBER(11),
    TRANSACTION_LOCAL_HOUR(12),
    TRANSACTION_LOCAL_DATE(13),
    EXPIRATION_DATE(14),
    CLOSING_DATE(15),
    CAPTURE_DATE(17),
    MERCHANT_TYPE(18),
    ENTRY_MODE_IN_THE_POS(22),
    CARD_SEQUENCE_NUMBER(23),
    NET_INTERNATIONAL_ID(24),
    POS_CONDITION_CODE(25),
    ACQUIRER_ID_CODE(32),
    TRACK_DATA_2(35),
    RETRIEVAL_REFERENCE_NUMBER(37),
    AUTHORIZATION_CODE(38),
    RESPONSE_CODE(39),
    TERMINAL_ID(41),
    MERCHANT_ID_CODE(42),
    FLEET_DATA_1_PRODUCT_RESTRICTION(48),
    CURRENCY_CODE(49),
    PIN_BLOCK(52),
    ICC_DATA(53),
    APPLICATION_SOFT_VERSION(60),
    BRAND_CODE_TO_SETTLEMENT(61),
    TICKET_NUMBER(63),
    FLEET_DATA_TWO(63),
    SETTLEMENT_INSTITUTION_COUNTRY_CODE(69),
    NETWORK_MANAGEMENT_INFORMATION_CODE(70),


    UNKNOWN_DATA(-1),
    ;


    private final int indexNumber;

    public static ISO8583DataElementType getByIndexNumber(int indexNumber) {
        if (Util.iso8583DataElementMap.containsKey(indexNumber))
            return Util.iso8583DataElementMap.get(indexNumber);
        return ISO8583DataElementType.UNKNOWN_DATA;
    }
}
