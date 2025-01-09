package com.piggymetrics.statistics.client;

import com.piggymetrics.statistics.domain.ExchangeRate;

import java.util.List;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class ExchangeRatesClientFallback implements ExchangeRatesClient {

    @Override
    public List<ExchangeRate> getRates() {
        // Fallback 구현: 빈 리스트 반환
        return Collections.emptyList();
    }
}