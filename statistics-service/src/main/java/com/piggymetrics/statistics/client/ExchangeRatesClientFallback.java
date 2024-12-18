package com.piggymetrics.statistics.client;

import com.piggymetrics.statistics.domain.Currency;
import com.piggymetrics.statistics.domain.ExchangeRatesContainer;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class ExchangeRatesClientFallback implements ExchangeRatesClient {

    @Override
    public ExchangeRatesContainer getRates(String authKey, String searchDate, String dataType) {
        // Fallback 구현: 빈 ExchangeRatesContainer 반환
        ExchangeRatesContainer container = new ExchangeRatesContainer();
        container.setRates(Collections.emptyList()); // 빈 리스트로 설정
        return container;
    }
}