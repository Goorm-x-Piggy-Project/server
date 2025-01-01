package com.piggymetrics.statistics.controller;

import com.piggymetrics.statistics.domain.Currency;
import com.piggymetrics.statistics.exception.CustomException;
import com.piggymetrics.statistics.service.ExchangeRatesService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/statistics/rates")
public class ExchangeRatesController {

    private final ExchangeRatesService exchangeRatesService;

    public ExchangeRatesController(ExchangeRatesService exchangeRatesService) {
        this.exchangeRatesService = exchangeRatesService;
    }

    @GetMapping("/hello")
    public String testStatistics() {
        return "exchangeRates-controller";
    }

    // 주요 환율 반환 (기본)
    @GetMapping("/default")
    public Map<String, BigDecimal> getDefaultRates() {
        return exchangeRatesService.getFilteredRates(List.of("USD", "KRW"));
    }

    // 특정 환율 요청
    @GetMapping("/filtered")
    public Map<String, BigDecimal> getFilteredRates(@RequestParam List<String> currencies) {
        return exchangeRatesService.getFilteredRates(currencies);
    }

    // 전체 환율 반환
    @GetMapping("/all")
    public Map<Currency, BigDecimal> getAllRates() {
        return exchangeRatesService.getCurrentRates();
    }
}