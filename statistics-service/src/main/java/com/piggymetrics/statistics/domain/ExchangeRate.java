package com.piggymetrics.statistics.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeRate {

    @JsonProperty("date")
    private String date; // 응답의 "date" 필드

    @JsonProperty("name")
    private String name; // 응답의 "name" 필드 (통화 코드)

    @JsonProperty("rate")
    private BigDecimal rate; // 응답의 "rate" 필드 (환율 값)

    @JsonProperty("timestamp")
    private long timestamp; // 응답의 "timestamp" 필드 (UNIX 타임스탬프)
}

