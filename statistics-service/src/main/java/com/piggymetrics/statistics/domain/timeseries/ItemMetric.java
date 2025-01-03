package com.piggymetrics.statistics.domain.timeseries;

import com.piggymetrics.statistics.domain.Currency;
import com.piggymetrics.statistics.domain.TimePeriod;

import java.math.BigDecimal;
import lombok.Getter;

/**
 * Represents normalized {@link com.piggymetrics.statistics.domain.Item} object
 * with {@link Currency#getBase()} currency and {@link TimePeriod#getBase()} time period
 */
@Getter
public class ItemMetric {

	private String title;
	private BigDecimal amount;

	public ItemMetric(String title, BigDecimal amount) {
		this.title = title;
		this.amount = amount;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ItemMetric that = (ItemMetric) o;

		return title.equalsIgnoreCase(that.title);

	}

	@Override
	public int hashCode() {
		return title.hashCode();
	}
}
