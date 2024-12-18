package com.piggymetrics.statistics.client;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.piggymetrics.statistics.domain.Currency;
import com.piggymetrics.statistics.domain.ExchangeRatesContainer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ExchangeRatesClientTest {

	@Autowired
	private ExchangeRatesClient client;

	@Test
	void shouldRetrieveExchangeRates() {
		// Given: 필요한 입력값
		String baseCurrency = "KRW";
		String today = LocalDate.now().toString().replace("-", ""); // yyyyMMdd 형식
		String dataType = "AP01";

		// When: 환율 데이터를 요청
		ExchangeRatesContainer container = client.getRates(baseCurrency, today, dataType);

		// Then: 반환된 데이터 검증
		assertThat(container).isNotNull();
		assertThat(container.getRates())
				.isNotNull();

		// USD, EUR, JPY(100) 존재 여부 검증
		assertThat(container.getRates().stream().anyMatch(rate -> "USD".equals(rate.getCurrencyUnit())))
				.isTrue();
		assertThat(container.getRates().stream().anyMatch(rate -> "EUR".equals(rate.getCurrencyUnit())))
				.isTrue();
		assertThat(container.getRates().stream().anyMatch(rate -> "JPY(100)".equals(rate.getCurrencyUnit())))
				.isTrue();
	}

	@Test
	void shouldRetrieveExchangeRatesForSpecifiedCurrency() {
		// Given: 필요한 입력값 설정
		String baseCurrency = "KRW";
		String today = LocalDate.now().toString().replace("-", ""); // yyyyMMdd 형식
		String dataType = "AP01";

		Currency requestedCurrency = Currency.EUR;

		// When: 특정 통화의 환율 데이터를 요청
		ExchangeRatesContainer container = client.getRates(baseCurrency, today, dataType);

		// Then: 반환된 데이터 검증
		assertThat(container).isNotNull();
		assertThat(container.getRates())
				.isNotNull();

		// 리스트에서 요청된 통화(EUR)가 존재하는지 확인
		assertThat(container.getRates().stream()
				.anyMatch(rate -> requestedCurrency.name().equals(rate.getCurrencyUnit())))
				.isTrue();
	}
}