package com.piggymetrics.statistics.service;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.piggymetrics.statistics.domain.Account;
import com.piggymetrics.statistics.domain.Currency;
import com.piggymetrics.statistics.domain.ExchangeRatesContainer;
import com.piggymetrics.statistics.domain.ExchangeRatesContainer.ExchangeRate;
import com.piggymetrics.statistics.domain.Item;
import com.piggymetrics.statistics.domain.Saving;
import com.piggymetrics.statistics.domain.TimePeriod;
import com.piggymetrics.statistics.domain.timeseries.DataPoint;
import com.piggymetrics.statistics.domain.timeseries.ItemMetric;
import com.piggymetrics.statistics.domain.timeseries.StatisticMetric;
import com.piggymetrics.statistics.repository.DataPointRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@ExtendWith(MockitoExtension.class)
public class StatisticsServiceImplTest {

	@InjectMocks
	private StatisticsService statisticsService;

	@Mock
	private ExchangeRatesService ratesService;

	@Mock
	private DataPointRepository repository;

	@BeforeEach
	public void setup() {
		initMocks(this);
	}

	@Test
	public void shouldFindDataPointListByAccountName() {
		final List<DataPoint> list = ImmutableList.of(DataPoint.builder().build());
		when(repository.findByIdAccount("test")).thenReturn(list);

		List<DataPoint> result = statisticsService.findByAccountName("test");
		assertEquals(list, result);
	}

	@Test
	public void shouldFailToFindDataPointWhenAccountNameIsNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			statisticsService.findByAccountName(null);
		});
	}

	@Test
	public void shouldFailToFindDataPointWhenAccountNameIsEmpty() {
		assertThrows(IllegalArgumentException.class, () -> {
			statisticsService.findByAccountName("");
		});
	}

	@Test
	public void shouldSaveDataPoint() {

		/**
		 * Given
		 */

		Item salary = Item.builder()
				.title("Salary")
				.amount(new BigDecimal(9100))
				.currency(Currency.USD)
				.period(TimePeriod.MONTH)
				.build();

		Item grocery = Item.builder()
				.title("Grocery")
				.amount(new BigDecimal(500))
				.currency(Currency.KRW)
				.period(TimePeriod.DAY)
				.build();

		Item vacation = Item.builder()
				.title("Vacation")
				.amount(new BigDecimal(3400))
				.currency(Currency.EUR)
				.period(TimePeriod.YEAR)
				.build();

		Saving saving = Saving.builder()
				.amount(new BigDecimal(1000))
				.currency(Currency.EUR)
				.interest(new BigDecimal("3.2"))
				.deposit(true)
				.capitalization(false)
				.build();

		Account account = new Account();
		account.setIncomes(ImmutableList.of(salary));
		account.setExpenses(ImmutableList.of(grocery, vacation));
		account.setSaving(saving);

		final Map<Currency, BigDecimal> rates = ImmutableMap.of(
				Currency.EUR, new BigDecimal("1.50735"),
				Currency.USD, new BigDecimal("1436.6"),
				Currency.JPY, new BigDecimal("935.74"),
				Currency.KRW, BigDecimal.ONE
		);

		/**
		 * When
		 */

		when(ratesService.convert(any(Currency.class), any(Currency.class), any(BigDecimal.class)))
				.then(i -> {
					Currency from = i.getArgument(0); // 변환 전 통화
					Currency to = i.getArgument(1);   // 변환 후 통화
					BigDecimal amount = i.getArgument(2); // 변환할 금액

					// 변환 로직: 기준 통화(KRW)를 중심으로 처리
					BigDecimal fromRate = rates.get(from);
					BigDecimal toRate = rates.get(to);

					return amount.multiply(fromRate).divide(toRate, 4, RoundingMode.HALF_UP);
				});

		when(ratesService.getCurrentRates()).thenReturn(rates);

		when(repository.save(any(DataPoint.class))).then(returnsFirstArg());

		DataPoint dataPoint = statisticsService.save("test", account);

		/**
		 * Then
		 */

		final BigDecimal expectedExpensesAmount = new BigDecimal("514.0317");
		final BigDecimal expectedIncomesAmount = new BigDecimal("429514.9293");
		final BigDecimal expectedSavingAmount = new BigDecimal("1507.3500");

		final BigDecimal expectedNormalizedSalaryAmount = new BigDecimal("429514.9293");
		final BigDecimal expectedNormalizedVacationAmount = new BigDecimal("14.0317");
		final BigDecimal expectedNormalizedGroceryAmount = new BigDecimal("500.0000");

		assertEquals(dataPoint.getId().getAccount(), "test");
		assertEquals(dataPoint.getId().getDate(), Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));

		assertTrue(expectedExpensesAmount.compareTo(dataPoint.getStatistics().get(StatisticMetric.EXPENSES_AMOUNT)) == 0);
		assertTrue(expectedIncomesAmount.compareTo(dataPoint.getStatistics().get(StatisticMetric.INCOMES_AMOUNT)) == 0);
		assertTrue(expectedSavingAmount.compareTo(dataPoint.getStatistics().get(StatisticMetric.SAVING_AMOUNT)) == 0);

		ItemMetric salaryItemMetric = dataPoint.getIncomes().stream()
				.filter(i -> i.getTitle().equals(salary.getTitle()))
				.findFirst().get();

		ItemMetric vacationItemMetric = dataPoint.getExpenses().stream()
				.filter(i -> i.getTitle().equals(vacation.getTitle()))
				.findFirst().get();

		ItemMetric groceryItemMetric = dataPoint.getExpenses().stream()
				.filter(i -> i.getTitle().equals(grocery.getTitle()))
				.findFirst().get();

		assertTrue(expectedNormalizedSalaryAmount.compareTo(salaryItemMetric.getAmount()) == 0);
		assertTrue(expectedNormalizedVacationAmount.compareTo(vacationItemMetric.getAmount()) == 0);
		assertTrue(expectedNormalizedGroceryAmount.compareTo(groceryItemMetric.getAmount()) == 0);

		assertEquals(rates, dataPoint.getRates());

		verify(repository, times(1)).save(dataPoint);
	}

	private List<ExchangeRate> createRates() {
		return List.of(
				createRate("USD", "1436.6"),
				createRate("EUR", "1.50735"),
				createRate("JPY(100)", "935.74"),
				createRate("KRW", "1")
		);
	}

	private ExchangeRatesContainer.ExchangeRate createRate(String currency, String rate) {
		ExchangeRatesContainer.ExchangeRate exchangeRate = new ExchangeRatesContainer.ExchangeRate();
		exchangeRate.setCurrencyUnit(currency);
		exchangeRate.setDealBaseRate(new BigDecimal(rate));
		return exchangeRate;
	}
}