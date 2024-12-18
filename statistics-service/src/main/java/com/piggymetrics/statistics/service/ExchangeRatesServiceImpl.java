package com.piggymetrics.statistics.service;

import com.google.common.collect.ImmutableMap;
import com.piggymetrics.statistics.client.ExchangeRatesClient;
import com.piggymetrics.statistics.domain.Currency;
import com.piggymetrics.statistics.domain.ExchangeRatesContainer;
import com.piggymetrics.statistics.domain.ExchangeRatesContainer.ExchangeRate;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Map;

@Service
public class ExchangeRatesServiceImpl implements ExchangeRatesService {

	private static final Logger log = LoggerFactory.getLogger(ExchangeRatesServiceImpl.class);

	private ExchangeRatesContainer container;

	private final ExchangeRatesClient client;

	@Value("${rates.authkey}")
	private String authKey; // Config Server에서 가져오는 인증키

	public ExchangeRatesServiceImpl(ExchangeRatesClient client) {
		this.client = client;
	}

	@Override
	public Map<Currency, BigDecimal> getCurrentRates() {

		if (container == null || container.getRates() == null || container.getRates().isEmpty()) {
			String today = LocalDate.now().toString().replace("-", ""); // yyyyMMdd 형식
			container = client.getRates(authKey, today, "AP01");
			log.info("Exchange rates updated: {}", container);
		}

		List<ExchangeRate> rates = container.getRates();
		// 각 통화의 매매기준율(deal_bas_r)을 가져와 Map으로 반환
		return rates.stream()
				.collect(Collectors.toMap(
						rate -> Currency.valueOf(rate.getCurrencyUnit().replace("(100)", "")), // 통화 단위 변환
						ExchangeRatesContainer.ExchangeRate::getDealBaseRate
				));
	}

	@Override
	public BigDecimal convert(Currency from, Currency to, BigDecimal amount) {

		Assert.notNull(amount, "The amount must not be null");

		Map<Currency, BigDecimal> rates = getCurrentRates();
		BigDecimal ratio = rates.get(from).divide(rates.get(to), 4, RoundingMode.HALF_UP);

		return amount.multiply(ratio);
	}

	/**
	 * 특정 통화의 환율(deal_bas_r)을 반환.
	 * @param rates 전체 환율 리스트
	 * @param currency 통화 코드
	 * @return 매매기준율 (deal_bas_r)
	 */
	private BigDecimal getRateForCurrency(List<ExchangeRatesContainer.ExchangeRate> rates, String currency) {
		return rates.stream()
				.filter(rate -> currency.equals(rate.getCurrencyUnit()))
				.findFirst()
				.map(ExchangeRatesContainer.ExchangeRate::getDealBaseRate)
				.orElseThrow(() -> new RuntimeException("Rate not found for currency: " + currency));
	}
}