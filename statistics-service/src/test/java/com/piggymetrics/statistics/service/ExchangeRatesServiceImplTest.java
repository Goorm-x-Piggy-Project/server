package com.piggymetrics.statistics.service;

import com.piggymetrics.statistics.client.ExchangeRatesClient;
import com.piggymetrics.statistics.domain.Currency;
import com.piggymetrics.statistics.domain.ExchangeRatesContainer;

import com.piggymetrics.statistics.domain.ExchangeRatesContainer.ExchangeRate;
import java.time.LocalDate;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.Map;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExchangeRatesServiceImplTest {

	@InjectMocks
	private ExchangeRatesService ratesService;

	@Mock
	private ExchangeRatesClient client;

	@BeforeEach
	public void setup() {
		// Mock 객체로 서비스 인스턴스 초기화
		ratesService = new ExchangeRatesService(client);

		// ReflectionTestUtils를 사용해 필드 주입
		ReflectionTestUtils.setField(ratesService, "authKey", "mockAuthKey");
	}

	@Test
	public void shouldReturnCurrentRatesWhenContainerIsEmptySoFar() {
		//given
		String mockAuthKey = "mockAuthKey"; // 테스트용 authKey
		String today = LocalDate.now().toString().replace("-", "");

		List<ExchangeRate> mockRates = createRates();
		when(client.getRates(eq(mockAuthKey), eq(today), eq("AP01"))).thenReturn(mockRates);

		//when
		Map<Currency, BigDecimal> result = ratesService.getCurrentRates();

		//then
		verify(client, times(1)).getRates(anyString(), anyString(), anyString());

		assertEquals(new BigDecimal("1.50735"), result.get(Currency.EUR), "EUR rate mismatch");
		assertEquals(new BigDecimal("1436.6"), result.get(Currency.USD), "USD rate mismatch");
		assertEquals(new BigDecimal("935.74"), result.get(Currency.JPY), "JPY rate mismatch");
		assertEquals(BigDecimal.ONE, result.get(Currency.KRW), "KRW rate mismatch");
	}

	@Test
	void shouldNotRequestRatesWhenTodaysContainerAlreadyExists() {
		// Given: Mock 데이터 설정
		String mockAuthKey = "mockAuthKey"; // 테스트용 authKey
		String today = LocalDate.now().toString().replace("-", "");

		List<ExchangeRate> mockRates = createRates();
		when(client.getRates(eq(mockAuthKey), eq(today), eq("AP01"))).thenReturn(mockRates);

		// When: 초기 데이터 요청 및 재요청
		ratesService.getCurrentRates(); // 첫 번째 호출
		ratesService.getCurrentRates(); // 두 번째 호출 (캐시 사용)

		// Then: API 호출은 한 번만 발생해야 함
		verify(client, times(1)).getRates(anyString(), anyString(), anyString());
	}

	@Test
	void shouldConvertCurrency() {
		// Given: Mock 데이터 설정
		String mockAuthKey = "mockAuthKey"; // 테스트용 authKey
		String today = LocalDate.now().toString().replace("-", "");

		List<ExchangeRate> mockRates = createRates();
		when(client.getRates(eq(mockAuthKey), eq(today), eq("AP01"))).thenReturn(mockRates);


		final BigDecimal amount = new BigDecimal(1);
		final BigDecimal expectedConversionResult = new BigDecimal("1436.6");

		// When: 환율 변환 호출
		BigDecimal result = ratesService.convert(Currency.USD, Currency.KRW, amount);

		// Then: 결과 검증
		assertThat(result)
				.isNotNull()
				.isEqualByComparingTo(expectedConversionResult); // 예상 결과와 비교
	}

	@Test
	void shouldFailToConvertWhenAmountIsNull() {
		// Given: 입력 값으로 null을 설정
		Currency fromCurrency = Currency.EUR;
		Currency toCurrency = Currency.USD;

		// When & Then: null 값으로 호출 시 예외 발생을 검증
		assertThatThrownBy(() -> ratesService.convert(fromCurrency, toCurrency, null))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("The amount must not be null");
	}
	private List<ExchangeRate> createRates() {
		return List.of(
				createRate("USD", "1436.6", "미국 달러"),
				createRate("EUR", "1.50735", "유로"),
				createRate("JPY(100)", "935.74", "일본 엔"),
				createRate("KRW", "1", "한국 원")
		);
	}

	private ExchangeRatesContainer.ExchangeRate createRate(String currency, String rate, String name) {
		ExchangeRatesContainer.ExchangeRate exchangeRate = new ExchangeRatesContainer.ExchangeRate();
		exchangeRate.setCurrencyUnit(currency);
		exchangeRate.setDealBaseRate(new BigDecimal(rate));
		exchangeRate.setCurrencyName(name);
		return exchangeRate;
	}
}