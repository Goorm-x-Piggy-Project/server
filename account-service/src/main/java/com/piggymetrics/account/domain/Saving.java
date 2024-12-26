package com.piggymetrics.account.domain;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Saving {

	@Id
	private ObjectId id;

	@NotNull
	private Long amount;

	@NotNull
	private Currency currency;

	@NotNull
	private Long interest;

	@NotNull
	private Boolean deposit;

	@NotNull
	private Boolean capitalization;

	@Builder
	public Saving(Long amount, Currency currency, Long interest, Boolean deposit, Boolean capitalization) {
		this.amount = amount;
		this.currency = currency;
		this.interest = interest;
		this.deposit = deposit;
		this.capitalization = capitalization;
	}
}
