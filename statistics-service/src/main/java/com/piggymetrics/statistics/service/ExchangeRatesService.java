package com.piggymetrics.statistics.service;

import static com.piggymetrics.statistics.exception.ErrorMessages.*;

import com.piggymetrics.statistics.client.ExchangeRatesClient;
import com.piggymetrics.statistics.domain.Currency;

import com.piggymetrics.statistics.domain.ExchangeRatesContainer.ExchangeRate;
import com.piggymetrics.statistics.exception.CustomException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class ExchangeRatesService {

	private static final Logger log = LoggerFactory.getLogger(ExchangeRatesService.class);

	private List<ExchangeRate> exchangeRates; // 최신 환율 리스트 저장

	private final ExchangeRatesClient client;

	@Value("${rates.authkey}")
	private String authKey; // Config Server에서 가져오는 인증키

	public ExchangeRatesService(ExchangeRatesClient client) {
		this.client = client;
	}

	public Map<String, BigDecimal> getFilteredRates(List<String> currencies) {
		try {
			Map<Currency, BigDecimal> allRates = getCurrentRates();

			return allRates.entrySet().stream()
					.filter(entry -> currencies.contains(entry.getKey().name()))
					.collect(Collectors.toMap(
							entry -> entry.getKey().name(),
							Map.Entry::getValue
					));
		} catch (Exception e) {
			log.error("Error while fetching filtered rates", e);
			throw new CustomException(RATES_EXCHANGE_FAIL + e.getMessage());
		}
	}

	public Map<Currency, BigDecimal> getCurrentRates() {

		if (exchangeRates == null || exchangeRates.isEmpty()) {
			LocalDate now = LocalDate.now();
			LocalDate closestPastWeekday = getClosestPastWeekday(now);

			String searchDate = closestPastWeekday.toString().replace("-", "");
			exchangeRates = client.getRates(authKey, searchDate, "AP01");
			log.info("Exchange rates updated: {}", exchangeRates);
		}

		// Currency enum의 모든 값을 가져옵니다.
		return exchangeRates.stream()
				.filter(rate -> Currency.isSupported(rate.getCurrencyUnit().replace("(100)", "")))
				.collect(Collectors.toMap(
						rate -> Currency.valueOf(rate.getCurrencyUnit().replace("(100)", "")),
						ExchangeRate::getDealBaseRate
				));
	}

	public BigDecimal convert(Currency from, Currency to, BigDecimal amount) {

		Assert.notNull(amount, NULL_AMOUNT_ERROR);

		Map<Currency, BigDecimal> rates = getCurrentRates();

		BigDecimal fromRate = rates.get(from);
		BigDecimal toRate = rates.get(to);

		if (fromRate == null || toRate == null) {
			throw new CustomException(NULL_RATES_ERROR);
		}

		BigDecimal ratio = fromRate.divide(toRate, 4, RoundingMode.HALF_UP);

		return amount.multiply(ratio);
	}

}
