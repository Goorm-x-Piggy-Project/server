package com.piggymetrics.statistics.service;

import static com.piggymetrics.statistics.exception.ErrorMessages.*;

import com.piggymetrics.statistics.client.ExchangeRatesClient;
import com.piggymetrics.statistics.domain.Currency;

import com.piggymetrics.statistics.domain.ExchangeRate;
import com.piggymetrics.statistics.exception.CustomException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@RequiredArgsConstructor
public class ExchangeRatesService {

	private static final Logger log = LoggerFactory.getLogger(ExchangeRatesService.class);

	private final ExchangeRatesClient exchangeRatesClient;

	public Map<String, BigDecimal> getExchangeRates() {
		List<ExchangeRate> rates = exchangeRatesClient.getRates();

		// "USDKRW=X" -> "USD", "KRW=X" -> "KRW"로 변환 및 매핑
		return rates.stream()
				.collect(Collectors.toMap(
						rate -> parseCurrencyName(rate.getName()), // 키: "USD", "KRW" 등
						ExchangeRate::getRate // 값: 환율 값
				));
	}

	public Map<Currency, BigDecimal> getRates() {
		List<ExchangeRate> rates = exchangeRatesClient.getRates();

		// EnumMap을 사용하여 Currency를 키로 매핑
		return rates.stream()
				.filter(rate -> isValidCurrency(rate.getName())) // 유효한 Currency만 필터링
				.collect(Collectors.toMap(
						rate -> Currency.valueOf(parseCurrencyName(rate.getName())),
						ExchangeRate::getRate,
						(oldValue, newValue) -> newValue, // 중복 키 처리
						() -> new EnumMap<>(Currency.class) // EnumMap 사용
				));
	}

	public BigDecimal convert(Currency from, Currency to, BigDecimal amount) {

		Assert.notNull(amount, NULL_AMOUNT_ERROR);

		Map<Currency, BigDecimal> rates = getRates();

		BigDecimal fromRate = rates.get(from);
		BigDecimal toRate = rates.get(to);

		if (fromRate == null || toRate == null) {
			throw new CustomException(NULL_RATES_ERROR);
		}
		if (fromRate.compareTo(BigDecimal.ZERO) <= 0 || toRate.compareTo(BigDecimal.ZERO) <= 0) {
			throw new CustomException("환율은 0보다 작을 수 없습니다. fromRate: " + fromRate + ", toRate: " + toRate);
		}

		BigDecimal ratio = fromRate.divide(toRate, 4, RoundingMode.HALF_UP);

		return amount.multiply(ratio);
	}

	private String parseCurrencyName(String name) {
		name = name.replace("=X", "");

		if (name.equals("KRW")) {
			return name;
		}
		if (name.contains("KRW")) {
			return name.replace("KRW", "").trim();
		}
		return name.trim();
	}

	private boolean isValidCurrency(String name) {
		try {
			String parsedName = parseCurrencyName(name);
			return Currency.isSupported(parsedName);
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
}
