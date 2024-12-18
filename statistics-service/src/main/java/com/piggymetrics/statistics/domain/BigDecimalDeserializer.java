package com.piggymetrics.statistics.domain;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.math.BigDecimal;

public class BigDecimalDeserializer extends JsonDeserializer<BigDecimal> {

    @Override
    public BigDecimal deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String value = parser.getText();
        if (value != null) {
            // 컴마 제거 후 BigDecimal 변환
            return new BigDecimal(value.replace(",", ""));
        }
        return null;
    }
}
