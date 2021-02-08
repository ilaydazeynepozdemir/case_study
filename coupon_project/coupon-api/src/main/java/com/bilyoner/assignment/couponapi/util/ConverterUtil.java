package com.bilyoner.assignment.couponapi.util;

import com.bilyoner.assignment.balance.api.BDecimal;
import com.google.protobuf.ByteString;

import java.math.BigDecimal;
import java.math.MathContext;

public class ConverterUtil {
    private ConverterUtil() {
    }

    public static BigDecimal bdecimalToBigDecimal(BDecimal bDecimal) {
        MathContext mc = new MathContext(bDecimal.getPrecision());
        return new BigDecimal(
                new java.math.BigInteger(bDecimal.getValue().toByteArray()),
                bDecimal.getScale(),
                mc);
    }

    public static BDecimal bigDecimalToBdecimal(BigDecimal bigDecimal) {
        return BDecimal.newBuilder()
                .setScale(bigDecimal.scale())
                .setPrecision(bigDecimal.precision())
                .setValue(ByteString.copyFrom(bigDecimal.unscaledValue().toByteArray()))
                .build();
    }
}
