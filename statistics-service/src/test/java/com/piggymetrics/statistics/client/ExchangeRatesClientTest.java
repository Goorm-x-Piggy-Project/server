package com.piggymetrics.statistics.client;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


import com.piggymetrics.statistics.domain.Currency;

import com.piggymetrics.statistics.domain.ExchangeRatesContainer.ExchangeRate;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(MongoServerConfig.class)
@SpringBootTest(properties = {"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration"})

public class ExchangeRatesClientTest {

	@Autowired
	private ExchangeRatesClient client;

	@Test
	public void testGetRates() {
		// given
		String authKey = "nY5T9XKTkiWkFBxsbrm4ZzUQZzPv1R7Z"; // 실제 API 키
		String today = LocalDate.now().minusDays(1).toString().replace("-", ""); // yyyyMMdd 형식
		String dataType = "AP01"; // 데이터 타입

		// when
		List<ExchangeRate> response = client.getRates(authKey, today, dataType);

		// then
		ExchangeRate firstRate = response.get(0);
		assertThat(firstRate.getCurrencyUnit()).isEqualTo("AED");
		assertThat(firstRate.getDealBaseRate()).isGreaterThan(BigDecimal.ZERO);
	}

	@Test
	void shouldRetrieveExchangeRates() {
		// given
		String authKey = "nY5T9XKTkiWkFBxsbrm4ZzUQZzPv1R7Z"; // 실제 API 키
		String today = LocalDate.now().minusDays(1).toString().replace("-", ""); // yyyyMMdd 형식
		String dataType = "AP01"; // 데이터 타입

		// when
		List<ExchangeRate> rates= client.getRates(authKey, today, dataType);

		// USD, EUR, JPY(100) 존재 여부 검증
		assertThat(rates).isNotNull();

		assertThat(rates.stream().anyMatch(rate -> "USD".equals(rate.getCurrencyUnit()))).isTrue();
		assertThat(rates.stream().anyMatch(rate -> "EUR".equals(rate.getCurrencyUnit()))).isTrue();
		assertThat(rates.stream().anyMatch(rate -> "JPY(100)".equals(rate.getCurrencyUnit()))).isTrue();
	}

	@Test
	void shouldRetrieveExchangeRatesForSpecifiedCurrency() {
		// given
		String authKey = "nY5T9XKTkiWkFBxsbrm4ZzUQZzPv1R7Z"; // 실제 API 키
		String today = LocalDate.now().minusDays(1).toString().replace("-", ""); // yyyyMMdd 형식
		String dataType = "AP01"; // 데이터 타입

		// when
		List<ExchangeRate> rates= client.getRates(authKey, today, dataType);

		// then: 리스트에서 요청된 통화(EUR)가 존재하는지 확인
		assertThat(rates).isNotNull();

		String requestedCurrency = Currency.EUR.toString();
		assertThat(rates.stream()
				.anyMatch(rate -> requestedCurrency.equals(rate.getCurrencyUnit())))
				.isTrue();
	}
}