package com.piggymetrics.account.domain;

public enum Currency {

	USD, EUR, RUB, KRW;

	public static Currency getDefault() {
		return KRW;
	}
}
