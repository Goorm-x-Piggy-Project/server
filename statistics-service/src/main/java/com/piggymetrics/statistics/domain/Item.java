package com.piggymetrics.statistics.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

@Getter
public class Item {

	@NotNull
	@Length(min = 1, max = 20)
	private String title;

	@NotNull
	private BigDecimal amount;

	@NotNull
	private Currency currency;

	@NotNull
	private TimePeriod period;

	@Builder
	public Item(String title, BigDecimal amount, Currency currency, TimePeriod period) {
		this.title = title;
		this.amount = amount;
		this.currency = currency;
		this.period = period;
	}

}
