package com.piggymetrics.statistics.domain;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Saving {

	@NotNull
	private BigDecimal amount;

	@NotNull
	private Currency currency;

	@NotNull
	private BigDecimal interest;

	@NotNull
	private Boolean deposit;

	@NotNull
	private Boolean capitalization;

	@Builder
	public Saving(BigDecimal amount, Currency currency, BigDecimal interest, Boolean deposit, Boolean capitalization) {
		this.amount = amount;
		this.currency = currency;
		this.interest = interest;
		this.deposit = deposit;
		this.capitalization = capitalization;
	}
}
