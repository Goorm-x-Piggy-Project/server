package com.piggymetrics.statistics.domain.timeseries;

import com.piggymetrics.statistics.domain.Currency;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

/**
 * Represents daily time series data point containing
 * current account state
 */

@Getter
@Document(collection = "datapoints")
public class DataPoint {

	@Id
	private final DataPointId id;

	private final Set<ItemMetric> incomes;

	private final Set<ItemMetric> expenses;

	private final Map<StatisticMetric, BigDecimal> statistics;

	private final Map<Currency, BigDecimal> rates;

	@Builder
	public DataPoint(DataPointId id, Set<ItemMetric> incomes, Set<ItemMetric> expenses,
					 Map<StatisticMetric, BigDecimal> statistics, Map<Currency, BigDecimal> rates) {
		this.id = id;
		this.incomes = incomes;
		this.expenses = expenses;
		this.statistics = statistics;
		this.rates = rates;
	}

}
